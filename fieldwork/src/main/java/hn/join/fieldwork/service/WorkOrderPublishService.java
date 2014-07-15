package hn.join.fieldwork.service;

import hn.join.fieldwork.service.MqttListener.PublishFailureCallback;
import hn.join.fieldwork.web.dto.WorkOrderDto;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 工单发布
 * @author aisino_lzw
 *
 */
@Service
public class WorkOrderPublishService {
	
	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private MqttListener publishMessageListener;

	private final BlockingQueue<WorkOrderDto> workOrderQueue = new LinkedBlockingQueue<WorkOrderDto>(
			1000);
	
	private ExecutorService dispatchWorkOrderExecutor = Executors.newSingleThreadExecutor();
	
	/**
	 * 初始化
	 */
	@PostConstruct
	public void init(){
		dispatchWorkOrderExecutor.execute(new PublishWorkOrderTask());
	}
	
	/**
	 * 销毁
	 */
	@PreDestroy
	public void destroy(){
		dispatchWorkOrderExecutor.shutdownNow();
	}
	

	/**
	 * 发布工单
	 * @param workOrderDto
	 */
	public void publishWorkOrder(WorkOrderDto workOrderDto) {
		try {
			workOrderQueue.put(workOrderDto);
            logger.info("publishWorkOrder QueueSize:" + workOrderQueue.size() + ", this order is:" + workOrderDto);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	
	
	/**
	 * 发布工单线程
	 * @author aisino_lzw
	 *
	 */
	private class PublishWorkOrderTask implements Runnable {

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					WorkOrderDto dto = workOrderQueue.take();
					publishMessageListener.onWorkOrderDispatch(dto,
							new PublishWorkOrderFailureCallback(dto));
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} catch (Exception ex) {
					logger.error("publish workOrder error",ex);
				}
			}

		}

	}

	/**
	 * 工单发布失败处理
	 * @author aisino_lzw
	 *
	 */
	public class PublishWorkOrderFailureCallback implements
			PublishFailureCallback {

		private final WorkOrderDto workOrderDto;

		private PublishWorkOrderFailureCallback(WorkOrderDto workOrderDto) {
			this.workOrderDto = workOrderDto;
		}

		public void onFailure(Throwable ex) {
			System.out.println("workOrderPushService-->:"+ex);
			workOrderQueue.add(workOrderDto);
		}

	}

}
