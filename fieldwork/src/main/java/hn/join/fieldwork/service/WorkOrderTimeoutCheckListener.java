package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.WorkOrder;
import hn.join.fieldwork.service.SystemEventBus.WorkOrderConfirmRequest;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;

/**
 * 工单超时检查
 * @author aisino_lzw
 *
 */
@Component
public class WorkOrderTimeoutCheckListener {

	private static final Logger LOG = Logger
			.getLogger(WorkOrderTimeoutCheckListener.class);

	@Autowired
	private SystemEventBus systemEventBus;

	@Autowired
	private WorkOrderService workOrderService;

	@Autowired
	private SystemParameterService systemParameterService;
	
	private DelayQueue<DelayItem<WorkOrderConfirmRequest>> delayQueue = new DelayQueue<DelayItem<WorkOrderConfirmRequest>>();

	private ExecutorService workOrderTimeoutExecutor;
	


//	private volatile int workOrderTimeout;
	
	

	/**
	 * 初始化
	 */
	@PostConstruct
	public void init() {
		systemEventBus.getEventBus().register(this);
		workOrderTimeoutExecutor = Executors.newSingleThreadExecutor();
		workOrderTimeoutExecutor.submit(new WorkOrderTimeoutTask());
//		workOrderTimeout = SystemConstants.getWorkOrderTimeoutMinutes();
	}

	/**
	 * 销毁
	 */
	public void destory() {
		systemEventBus.getEventBus().unregister(this);
		workOrderTimeoutExecutor.shutdownNow();
	}
	
//	public void changeWorkOrderTimeout(int workOrderTimeout) throws IOException{
//		this.workOrderTimeout=workOrderTimeout;
//		SystemConstants.changeWorkOrderTimeout(workOrderTimeout);
//	}
	
//	public int getWorkOrderTimeout(){
//		return workOrderTimeout;
//	}

	/**
	 * 确认工单
	 */
	@Subscribe
	public void onWorkOrderConfirm(
			WorkOrderConfirmRequest workOrderConfirmRequest) {
		int orderTimeout= Integer.parseInt(systemParameterService.getValueByName(SystemParameterService.PARAM_NAME_ORDER_TIMEOUT));
		DelayItem<WorkOrderConfirmRequest> item = new DelayItem<WorkOrderConfirmRequest>(
				workOrderConfirmRequest, orderTimeout * 60 * 1000L);
		delayQueue.add(item);

	}

	/**
	 * 工单超时检查线程
	 * @author aisino_lzw
	 *
	 */
	private class WorkOrderTimeoutTask implements Runnable {

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					DelayItem<WorkOrderConfirmRequest> delayItem = delayQueue
							.take();
					WorkOrder workOrder = workOrderService
							.getByWorkOrderNo(delayItem.item.getWorkOrderNo());
					if (workOrder.getAcceptTime() == null
							&& workOrder.getReassignTime() == null) {
						workOrderService.timeout(workOrder.getFieldWorkerId(),workOrder.getWorkOrderNo());
						
					}

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} catch (Exception ex) {
					LOG.error("", ex);
				}
			}

		}

	}

	/**
	 * 延期类
	 * @author aisino_lzw
	 *
	 * @param <T>
	 */
	private static class DelayItem<T> implements Delayed {
		private static final long MILLORIGIN = System.currentTimeMillis();

		final static long now() {
			return System.currentTimeMillis() - MILLORIGIN;
		}

		private static final AtomicLong sequencer = new AtomicLong(0);

		private final long sequenceNumber;

		private final long time;

		private final T item;

		public DelayItem(T submit, long timeout) {
			this.time = now() + timeout;
			this.item = submit;
			this.sequenceNumber = sequencer.getAndIncrement();
		}

		public T getItem() {
			return this.item;
		}

		public long getDelay(TimeUnit unit) {
			long d = unit.convert(time - now(), TimeUnit.MILLISECONDS);
			return d;
		}

		public int compareTo(Delayed other) {
			if (other == this) // compare zero ONLY if same object
				return 0;
			if (other instanceof DelayItem) {
				DelayItem x = (DelayItem) other;
				long diff = time - x.time;
				if (diff < 0)
					return -1;
				else if (diff > 0)
					return 1;
				else if (sequenceNumber < x.sequenceNumber)
					return -1;
				else
					return 1;
			}
			long d = (getDelay(TimeUnit.MILLISECONDS) - other
					.getDelay(TimeUnit.MILLISECONDS));
			return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((item == null) ? 0 : item.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DelayItem other = (DelayItem) obj;
			if (item == null) {
				if (other.item != null)
					return false;
			} else if (!item.equals(other.item))
				return false;
			return true;
		}

	}

}
