package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.Checkin;
import hn.join.fieldwork.domain.CheckinDay;
import hn.join.fieldwork.domain.CheckinSetting;
import hn.join.fieldwork.domain.CheckinSetting.CheckinStatus;
import hn.join.fieldwork.domain.FieldWorker;
import hn.join.fieldwork.persistence.CheckinDayMapper;
import hn.join.fieldwork.persistence.CheckinMapper;
import hn.join.fieldwork.persistence.CheckinSettingMapper;
import hn.join.fieldwork.persistence.FieldWorkerMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.utils.DateTimeUtil;
import hn.join.fieldwork.utils.DateTimeUtil.OneDayTimeSpan;
import hn.join.fieldwork.utils.ExcelUtil;
import hn.join.fieldwork.utils.ExcelUtil.ExcelExportContext;
import hn.join.fieldwork.utils.ExcelUtil.RowFiller;
import hn.join.fieldwork.utils.ExcelUtil.SheetDataSource;
import hn.join.fieldwork.utils.ExcelUtil.SheetItem;
import hn.join.fieldwork.utils.FileUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.CheckinCommand;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Service
public class CheckinService {

	private ScheduledExecutorService outTimeNotifyExecutor = Executors
			.newScheduledThreadPool(1);

	private Future<Void> outTimeNotifyResult = null;

	private volatile CheckinDay currentCheckinDay;

	private volatile DateTime currentSystemCheckinTime;

	// 按时签到的外勤人员ID
	private Set<Long> onTimeFieldWorkerId = Sets.newHashSet();

	private Set<Long> todayCheckinFieldWorkerId = Sets.newHashSet();

	@Autowired
	private CheckinMapper checkinMapper;

	@Autowired
	private FieldWorkerMapper fieldWorkerMapper;

	@Autowired
	private CheckinSettingMapper checkinSettingMapper;

	@Autowired
	private CheckinDayMapper checkinDayMapper;

	@Autowired
	private SmsClientService smsClientService;

	@Autowired
	private SystemParameterService systemParameterService;

	@PostConstruct
	public void init() throws IOException {
		initCurrentCheckinDay();
	}

	private void initCurrentCheckinDay() {
		createCheckinDay(new LocalDate());
	}
	/**
	 * 签到处理
	 * @param fieldWorkerId
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public CheckinResult newCheckin(Long fieldWorkerId) {
		DateTime now = DateTime.now();
		int aheadInMills = Integer
				.parseInt(systemParameterService
						.getValueByName(SystemParameterService.PARAM_NAME_CHECKIN_AHEAD_MINUTES)) * 60 * 1000;
		CheckinResult result;
		//如果日期为不必签到的日期，则返回结果为“不必签到”
		if (currentCheckinDay == CheckinDay.NO_CHECKIN_DAY) {
			result = CheckinResult.checkin_fail_disable;
		} else {
			//如果提交签到
			LocalTime systemCheckinTime = currentSystemCheckinTime
					.toLocalTime();

			LocalTime userCheckinTime = now.toLocalTime();
			int diff = systemCheckinTime.millisOfDay().get()
					- userCheckinTime.millisOfDay().get();
			if (diff > aheadInMills) {
				result = CheckinResult.checkin_fail_advance;
			} else {
				//修改签到时间或者新增签到记录
				if (!todayCheckinFieldWorkerId.contains(fieldWorkerId)) {
					todayCheckinFieldWorkerId.add(fieldWorkerId);
					Date _systemCheckinTime = currentSystemCheckinTime.toDate();
					Date _usercheckinDate = now.toDate();
					Long checkinId = checkinMapper
							.getCheckinIdByFieldWorkerIdAndSystemCheckinDateTime(
									fieldWorkerId, _systemCheckinTime);
					if (checkinId != null) {
						checkinMapper.updateUserCheckinDateTimeByCheckinId(
								checkinId, _usercheckinDate);
					} else {
						Checkin _checkin = new Checkin();
						_checkin.setFieldWorkerId(fieldWorkerId);
						_checkin.setUserCheckinDateTime(_usercheckinDate);
						_checkin.setSystemCheckinDateTime(_systemCheckinTime);
						checkinMapper.insertCheckin(_checkin);
					}

					result = CheckinResult.checkin_succ;
					if (diff > 0) {
						onTimeFieldWorkerId.add(fieldWorkerId);
					}
				} else {
					result = CheckinResult.checkin_exist;
				}
			}

		}

		return result;

	}
	/**
	 * 获得外出（OutTime）业务员人员列表
	 * @return
	 */
	private List<FieldWorker> getOutTimeFieldWorkers() {
		List<FieldWorker> outTimeFieldWorkerList;
		outTimeFieldWorkerList = fieldWorkerMapper
				.findOutTimeFieldWorkers(new ArrayList<Long>(
						onTimeFieldWorkerId));

		return outTimeFieldWorkerList;

	}
	/**
	 * 增加签到设置
	 * @param fromDate
	 * @param toDate
	 * @param checkStatus
	 * @param creatorId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void newCheckinSetting(String fromDate, String toDate,
			String checkStatus, Long creatorId) {
		List<CheckinSetting> results = fromCreateCheckinSetting(fromDate,
				toDate, checkStatus);
		for (CheckinSetting _checkinSetting : results)
			checkinSettingMapper.insertCheckinSetting(_checkinSetting,
					creatorId);

	}
	/**
	 * 分布查询签到设置
	 * @param fromDate
	 * @param toDate
	 * @param page
	 * @param rows
	 * @return
	 */
	public SQLQueryResult<CheckinSetting> findCheckinSettingBy(String fromDate,
			String toDate, Integer page, Integer rows) {

		SQLQueryResult<CheckinSetting> queryResult = null;

		long _count = checkinSettingMapper.countBy(fromDate, toDate);
		if (_count != 0) {
			Integer offset = (page - 1) * rows;
			List<CheckinSetting> _result = checkinSettingMapper.findBy(
					fromDate, toDate, offset, rows);
			queryResult = new SQLQueryResult<CheckinSetting>(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}
	/**
	 * 按条件查询签到结果
	 * @param fromDate
	 * @param toDate
	 * @param showAll
	 * @param fieldWorkerName
	 * @param city
	 * @param page
	 * @param rows
	 * @return
	 */
	public SQLQueryResult<CheckinCommand> findCheckinBy(Date fromDate,
			Date toDate, Boolean showAll, String fieldWorkerName, String city,
			Integer page, Integer rows) {
		SQLQueryResult<CheckinCommand> queryResult = null;
		long _count = checkinMapper.countBy(fromDate, toDate, showAll,
				fieldWorkerName, city);
		if (_count != 0) {
			Integer offset = null;
			if (page != null && rows != null)
				offset = (page - 1) * rows;
			List<CheckinCommand> _result = checkinMapper.findBy(fromDate,
					toDate, showAll, fieldWorkerName, city, offset, rows);
			queryResult = new SQLQueryResult<CheckinCommand>(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;

	}
	/**
	 * 创建签到日期时间
	 * @param localDate
	 */
	public void createCheckinDay(LocalDate localDate) {
		if (SystemConstants.isMaster()
				&& outTimeNotifyResult != null
				&& (!outTimeNotifyResult.isCancelled() && !outTimeNotifyResult
						.isDone()))
			outTimeNotifyResult.cancel(true);
		onTimeFieldWorkerId.clear();
		todayCheckinFieldWorkerId.clear();

		LocalTime localTime = LocalTime
				.parse(systemParameterService
						.getValueByName(SystemParameterService.PARAM_NAME_CHECKIN_TIME));
		DateTime checkinDateTime = new DateTime(localDate.getYear(),
				localDate.getMonthOfYear(), localDate.getDayOfMonth(),
				localTime.getHourOfDay(), localTime.getMinuteOfHour(),
				localTime.getSecondOfMinute());

		DateTime now = DateTime.now();
		String date = localDate.toString("yyyy-MM-dd");
		String time = localTime.toString("HH:mm:ss");
		CheckinDay checkinDay = null;
		CheckinSetting checkinSetting = checkinSettingMapper.getByDate(date);
		if (checkinSetting != null) {
			if (checkinSetting.getStatus() == CheckinStatus.DISABLE) {
				checkinDay = CheckinDay.NO_CHECKIN_DAY;
			} else if (checkinSetting.getStatus() == CheckinStatus.ENABLE) {
				checkinDay = new CheckinDay(date, time);
			}
		} else {
			if (localDate.getDayOfWeek() < 6) {
				checkinDay = new CheckinDay(date, time);
			} else {
				checkinDay = CheckinDay.NO_CHECKIN_DAY;
			}
		}
		if (checkinDay != CheckinDay.NO_CHECKIN_DAY) {
			currentSystemCheckinTime = checkinDateTime;
			CheckinDay exist = checkinDayMapper.findByDate(date);
			if (checkinDateTime.isAfter(now)
					&& checkinDateTime.toLocalDate().equals(now.toLocalDate())) {
				if (exist == null)
					checkinDayMapper.insertCheckinDay(checkinDay);
				long delay = checkinDateTime.getMillis() - now.getMillis();
				if (SystemConstants.isMaster())
					outTimeNotifyResult = outTimeNotifyExecutor.schedule(
							new NotifyTask(), delay, TimeUnit.MILLISECONDS);
			}

		}
		currentCheckinDay = checkinDay;
	}
	/**
	 * 根据输入参数生成签到设置
	 * @param fromDate
	 * @param toDate
	 * @param checkStatus
	 * @return
	 */
	private List<CheckinSetting> fromCreateCheckinSetting(String fromDate,
			String toDate, String checkStatus) {
		List<CheckinSetting> results = Lists.newArrayList();
		DateTimeFormatter ymdFormatter = DateTimeUtil.getYmdFormatter();
		DateTime _fromDate = ymdFormatter.parseDateTime(fromDate);
		DateTime _toDate = ymdFormatter.parseDateTime(toDate);
		long interval = ((_toDate.getMillis() - _fromDate.getMillis()) / DateTimeUtil.ONE_DAY_IN_MILLS) + 1;
		for (int i = 0; i < interval; i++) {
			CheckinSetting _checkinSetting = new CheckinSetting();
			_checkinSetting.setStatus(CheckinStatus.valueOf(checkStatus));
			_checkinSetting.setSettingDate(_fromDate.plusDays(i).toString(
					ymdFormatter));
			results.add(_checkinSetting);
		}
		return results;

	}
	/**
	 * 获取当前签到日期
	 * @return
	 */
	public CheckinDay getCurrentCheckinDay() {
		return currentCheckinDay;
	}
	/**
	 * 导出签到记录
	 * @param fromDate
	 * @param toDate
	 * @param showAll
	 * @param fieldWorkerName
	 * @param city
	 * @return
	 * @throws IOException
	 */
	public File exportCheckin(Date fromDate, Date toDate, Boolean showAll,
			String fieldWorkerName, String city) throws IOException {
		List<OneDayTimeSpan> timeSpanList = DateTimeUtil.divideIntoTimeSpan(
				fromDate, toDate);
		List<String> header = Lists.newArrayList("员工姓名", "个人签到时间", "系统设置时间");
		RowFiller<CheckinCommand> checkinRowFiller = new CheckinRowFiller();
		File tempDir = FileUtil.createTempDir(String.valueOf(System
				.currentTimeMillis()));
		CheckinSheetDataSource sheetDataSource = new CheckinSheetDataSource(
				showAll, fieldWorkerName, city, timeSpanList.iterator());
		File _excelFile = new File(tempDir, "签到查询结果.xlsx");
		ExcelExportContext<CheckinCommand> _exportContext = new ExcelExportContext<CheckinCommand>(
				_excelFile, header, sheetDataSource, checkinRowFiller);
		ExcelUtil.exportCheckin(_exportContext);
		return _excelFile;
	}
	
	private class CheckinSheetDataSource implements
			SheetDataSource<CheckinCommand> {
		private final Boolean showAll;
		private final String fieldWorkerName;
		private final String city;
		private final Iterator<OneDayTimeSpan> dateIterator;

		public CheckinSheetDataSource(Boolean showAll, String fieldWorkerName,
				String city, Iterator<OneDayTimeSpan> dateIterator) {
			super();
			this.showAll = showAll;
			this.fieldWorkerName = fieldWorkerName;
			this.city = city;
			this.dateIterator = dateIterator;
		}

		@Override
		public boolean hasNext() {
			return dateIterator.hasNext();
		}

		@Override
		public SheetItem<CheckinCommand> next() {
			OneDayTimeSpan dayTimeSpan = dateIterator.next();
			Date checkinDate = dayTimeSpan.getFromTime().toDate();
			List<CheckinCommand> checkinList = checkinMapper.findBy(
					checkinDate, checkinDate, showAll, fieldWorkerName, city,
					null, null);
			return new SheetItem<CheckinCommand>(
					DateTimeUtil.formatAsYYYYMMdd(checkinDate), checkinList);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private class CheckinRowFiller implements RowFiller<CheckinCommand> {

		@Override
		public void fillRow(CheckinCommand checkin, Row row) {
			Cell _cell0 = row.createCell(0);
			_cell0.setCellValue(checkin.getFieldWorkerName());

			Cell _cell1 = row.createCell(1);
			_cell1.setCellValue(DateTimeUtil.formatAsYYYYMMddHHmmss(checkin
					.getUserCheckinDateTime()));

			Cell _cell2 = row.createCell(2);
			_cell2.setCellValue(DateTimeUtil.formatAsYYYYMMddHHmmss(checkin
					.getSystemCheckinDateTime()));
		}

	}

	public static enum CheckinResult {
		checkin_succ(1, "签到成功"), checkin_fail_advance(2, "提前签到"), checkin_fail_disable(
				3, "不必签到"), checkin_fail_ex(4, "服务器异常"), checkin_exist(5,
				"当天已签到");

		private final int code;
		private final String message;

		private CheckinResult(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}
	}

	private class NotifyTask implements Callable<Void> {

		@Override
		public Void call() {
			try {
				List<FieldWorker> outTimeFieldWorkerList = getOutTimeFieldWorkers();
				if (outTimeFieldWorkerList != null
						&& outTimeFieldWorkerList.size() > 0) {
					List<Checkin> outTimeCheckinList = Lists.newLinkedList();
					for (FieldWorker _worker : outTimeFieldWorkerList) {
						Checkin _checkin = new Checkin();
						_checkin.setFieldWorkerId(_worker.getId());
						_checkin.setSystemCheckinDateTime(currentSystemCheckinTime
								.toDate());
						_checkin.setUserCheckinDateTime(null);
						outTimeCheckinList.add(_checkin);
					}
					checkinMapper.insertCheckinList(outTimeCheckinList);
				}
				if ("1".equals(systemParameterService
						.getValueByName(SystemParameterService.PARAM_NAME_CHECKIN_SMS_ENABLE))) {
					Set<String> phones = Sets
							.newHashSetWithExpectedSize(outTimeFieldWorkerList
									.size());
					for (FieldWorker worker : outTimeFieldWorkerList) {
						if (worker.getDevice() != null)
							phones.add(worker.getDevice().getPhoneNo());
					}
					String message = systemParameterService
							.getValueByName(SystemParameterService.PARAM_NAME_CHECKIN_SMS_MESSAGE);
					smsClientService.sendMultiMessage(new ArrayList<String>(
							phones), message);

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

	}

}
