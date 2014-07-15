package hn.join.fieldwork.service;

import hn.join.fieldwork.service.SystemEventBus.FinishOrderRequest;
import hn.join.fieldwork.service.SystemEventBus.NewOrderRequest;
import hn.join.fieldwork.service.SystemEventBus.TrackInfoUpdateRequest;
import hn.join.fieldwork.utils.JsonUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;


/**
 * 员工数据更新
 * @author aisino_lzw
 *
 */
public class FieldWorkerUpdateListener implements InitializingBean {

	private static final Logger LOG = Logger
			.getLogger(FieldWorkerUpdateListener.class);

	private IMap<Long, String> latestFieldWorkerInfoMap;

	private MultiMap<String, Long> areaCode2FieldWorkerMap;

	private ILock fieldWorkerWriteLock;

	@Autowired
	private SystemEventBus systemEventBus;

	@Autowired
	private WorkOrderService workOrderService;

	@Autowired
	private WorkOrderPublishService workOrderPublishService;

	
	/**
	 * 注册员工活动路线
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		systemEventBus.getEventBus().register(this);

	}

	public IMap<Long, String> getLatestFieldWorkerInfoMap() {
		return latestFieldWorkerInfoMap;
	}

	public void setLatestFieldWorkerInfoMap(
			IMap<Long, String> latestFieldWorkerInfoMap) {
		this.latestFieldWorkerInfoMap = latestFieldWorkerInfoMap;
	}

	
	/**
	 * 根据区域编码获取员工
	 */
	public Collection<Long> getFieldWorkerIdByAreaCode(String areaCode) {
		return areaCode2FieldWorkerMap.get(areaCode);
	}

	// public MultiMap<String, Long> getAreaCode2FieldWorkerMap() {
	// return areaCode2FieldWorkerMap;
	// }

	
	public void setAreaCode2FieldWorkerMap(
			MultiMap<String, Long> areaCode2FieldWorkerMap) {
		this.areaCode2FieldWorkerMap = areaCode2FieldWorkerMap;
	}

	public ILock getFieldWorkerWriteLock() {
		return fieldWorkerWriteLock;
	}

	public void setFieldWorkerWriteLock(ILock fieldWorkerWriteLock) {
		this.fieldWorkerWriteLock = fieldWorkerWriteLock;
	}

	
	/**
	 * 根据员工id获取最新员工信息
	 * @param fieldWorkerId
	 * @return
	 */
	public LatestFieldWorkerInfo getLatestFieldWorkerInfoByWorkerNo(
			Long fieldWorkerId) {
		LatestFieldWorkerInfo info = null;
		try {
			info = doGetLatestFieldWorkerInfo(fieldWorkerId);
		} catch (Exception ex) {
			LOG.error("不能获取最新的外勤员工信息,fieldWorkerId:" + fieldWorkerId, ex);
		}
		return info;
	}

	
	/**
	 * 下发工单
	 */
	@Subscribe
	public void onNewOrder(NewOrderRequest request) throws InterruptedException {
		Long fieldWorkerNo = request.getFieldWorkerId();
		LatestFieldWorkerInfo info = doGetLatestFieldWorkerInfo(fieldWorkerNo);
		if (info == null) {
			throw new IllegalFieldWorkerStateException(fieldWorkerNo,
					"newOrder", "下发工单找不到对应的外勤员工");
		}
		info.setLastUpdateTime(request.getUpdateTime());
		info.getPendingWorkOrderNo().add(request.getWorkOrderNo());
		info.setFieldWorkerId(request.getFieldWorkerId());
		info.setFieldWorkerName(request.getFieldWorkerName());
		doPutLatestFieldWorkerInfo(request.getAreaCode(), info);
		workOrderPublishService.publishWorkOrder(request.getDto());
	}

	/**
	 * 完成工单
	 * @param request
	 * @throws InterruptedException
	 */
	@Subscribe
	public void onFinishOrder(FinishOrderRequest request)
			throws InterruptedException {
		Long fieldWorkerId = request.getFieldWorkerId();
		LatestFieldWorkerInfo info = doGetLatestFieldWorkerInfo(fieldWorkerId);
		if (info == null) {
			throw new IllegalFieldWorkerStateException(fieldWorkerId,
					"finishOrder", "完成工单时找不到对应的外勤员工");
		}
		info.setFieldWorkerId(request.getFieldWorkerId());
		info.setLastUpdateTime(request.getUpdateTime());
		info.getPendingWorkOrderNo().remove(request.getWorkOrderNo());
		info.setFieldWorkerName(request.getFieldWorkerName());
		doPutLatestFieldWorkerInfo(request.getAreaCode(), info);
	}

	
	/**
	 * 设置轨迹信息
	 * @param request
	 * @throws InterruptedException
	 */
	@Subscribe
	public void onTrackInfo(TrackInfoUpdateRequest request)
			throws InterruptedException {
		Long fieldWorkerId = request.getFieldWorkerId();
		LatestFieldWorkerInfo info = doGetLatestFieldWorkerInfo(fieldWorkerId);
		if (info == null) {
			info = new LatestFieldWorkerInfo();
			List<String> pendingWorkOrderNo = workOrderService
					.findUnfinishedWorkOrdersByFieldWorkerId(fieldWorkerId);
			info.setPendingWorkOrderNo(pendingWorkOrderNo);
		}
		info.setLastLocateTimeInMill(request.getUpdateTime());
		info.setLastUpdateTime(request.getUpdateTime());
		info.setLatestLatitude(request.getLatestLatitude());
		info.setLatestLongitude(request.getLatestLongitude());
		info.setLatestAddress(request.getLatestAddress());
		info.setFieldWorkerId(request.getFieldWorkerId());
		info.setFieldWorkerName(request.getFieldWorkerName());
		
		doPutLatestFieldWorkerInfo(request.getAreaCode(), info);

	}

	/**
	 * 返回指定员工信息
	 * @param fieldWorkerId 员工id
	 * @return
	 */
	private LatestFieldWorkerInfo doGetLatestFieldWorkerInfo(Long fieldWorkerId) {
        /*System.out.println("doGetLatestFieldWorkerInfo :");
        for (Map.Entry<Long, String> longStringEntry : latestFieldWorkerInfoMap.entrySet()) {
            System.out.println(longStringEntry.getKey() + ":" + longStringEntry.getValue());
        }*/
        String latestFieldWorkerInfoJson = latestFieldWorkerInfoMap
				.get(fieldWorkerId);
		if (!StringUtils.isEmpty(latestFieldWorkerInfoJson)) {
			return (LatestFieldWorkerInfo) JsonUtil.fromJson(
					latestFieldWorkerInfoJson, LatestFieldWorkerInfo.class);
		}
		return null;
	}

	/**
	 * 将员工信息加入hazelcast缓存
	 * @param areaCode
	 * @param object
	 * @throws InterruptedException
	 */
	private void doPutLatestFieldWorkerInfo(String areaCode,
			LatestFieldWorkerInfo object) throws InterruptedException {
		if (!StringUtils.isEmpty(areaCode) && object != null) {
			String latestFieldWorkerInfoJson = JsonUtil.toJson(object);
			Long fieldWorkerId = object.getFieldWorkerId();
			if (fieldWorkerWriteLock.tryLock(5000, TimeUnit.MILLISECONDS)) {
				try {
					areaCode2FieldWorkerMap.put(areaCode, fieldWorkerId);
					latestFieldWorkerInfoMap.put(fieldWorkerId,
							latestFieldWorkerInfoJson);
				} finally {
					fieldWorkerWriteLock.unlock();
				}
			}

		}

	}

	/**
	 * 外勤员工最新信息
	 * @author aisino_lzw
	 *
	 */
	public static class LatestFieldWorkerInfo {

		private String fieldWorkerName;

		private Long fieldWorkerId;

		private long lastUpdateTime;

		private List<String> pendingWorkOrderNo = Lists.newArrayList();

		private float latestLatitude;

		private float latestLongitude;
		
		private String latestAddress;

		private long lastLocateTimeInMill;
		
	

		public String getFieldWorkerName() {
			return fieldWorkerName;
		}

		public void setFieldWorkerName(String fieldWorkerName) {
			this.fieldWorkerName = fieldWorkerName;
		}

		public Long getFieldWorkerId() {
			return fieldWorkerId;
		}

		public void setFieldWorkerId(Long fieldWorkerId) {
			this.fieldWorkerId = fieldWorkerId;
		}

		public List<String> getPendingWorkOrderNo() {
			return pendingWorkOrderNo;
		}

		public long getLastUpdateTime() {
			return lastUpdateTime;
		}

		public void setLastUpdateTime(long lastUpdateTime) {
			this.lastUpdateTime = lastUpdateTime;
		}

		public void setPendingWorkOrderNo(List<String> pendingWorkOrderNo) {
			this.pendingWorkOrderNo = pendingWorkOrderNo;
		}

		public float getLatestLatitude() {
			return latestLatitude;
		}

		public void setLatestLatitude(float latestLatitude) {
			this.latestLatitude = latestLatitude;
		}

		public float getLatestLongitude() {
			return latestLongitude;
		}

		public void setLatestLongitude(float latestLongitude) {
			this.latestLongitude = latestLongitude;
		}

		
		public String getLatestAddress() {
			return latestAddress;
		}

		public void setLatestAddress(String latestAddress) {
			this.latestAddress = latestAddress;
		}

		public long getLastLocateTimeInMill() {
			return lastLocateTimeInMill;
		}

		public void setLastLocateTimeInMill(long lastLocateTimeInMill) {
			this.lastLocateTimeInMill = lastLocateTimeInMill;
		}

	}

}
