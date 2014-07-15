package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.FieldWorker;
import hn.join.fieldwork.domain.TrackInfo;
import hn.join.fieldwork.persistence.FieldWorkerMapper;
import hn.join.fieldwork.persistence.TrackInfoMapper;
import hn.join.fieldwork.service.SystemEventBus.TrackInfoUpdateRequest;
import hn.join.fieldwork.utils.DateTimeUtil;
import hn.join.fieldwork.utils.DateTimeUtil.OneDayTimeSpan;
import hn.join.fieldwork.utils.ExcelUtil;
import hn.join.fieldwork.utils.ExcelUtil.ExcelExportContext;
import hn.join.fieldwork.utils.ExcelUtil.RowFiller;
import hn.join.fieldwork.utils.ExcelUtil.SheetDataSource;
import hn.join.fieldwork.utils.ExcelUtil.SheetItem;
import hn.join.fieldwork.utils.FileUtil;
import hn.join.fieldwork.utils.StringUtil;
import hn.join.fieldwork.utils.ZipUtil;
import hn.join.fieldwork.web.command.CreateTrackInfoCommand;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;


/**
 * 轨迹信息服务类
 */
@Component
public class TrackInfoService {

	@Autowired
	private TrackInfoMapper trackInfoMapper;

	@Autowired
	private FieldWorkerMapper fieldWorkerMapper;

	@Autowired
	private SystemEventBus systemEventBus;

	/**
	 * 新建轨迹信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public void newTrackInfo(CreateTrackInfoCommand command)
			throws DataAccessException {
//        System.out.println(command);
        if(command.getFieldWorkerNo()==null){
			return;
		}
		TrackInfo trackInfo = fromTrackInfo(command);
		FieldWorker fieldWorker = fieldWorkerMapper.getById(trackInfo
				.getFieldWorkerId());
//        System.out.printf(fieldWorker.toString());
        trackInfoMapper.insertTrackInfo(trackInfo);
//        System.out.println(trackInfo.toString());
        systemEventBus
				.postFieldWorkerUpdateRequestOnTrackInfo(new TrackInfoUpdateRequest(
                        trackInfo.getAreaCode(), trackInfo.getFieldWorkerId(),
                        fieldWorker.getFullname(), trackInfo.getCreateTime()
                        .getTime(), trackInfo.getLatitude(), trackInfo
                        .getLongitude(), trackInfo.getAddress()
                ));

	}

	/**
	 *根据时间段查询轨迹
	 */
	public List<TrackInfo> findInTimeSpan(Long fieldWorkerId,
			Date fromTime, Date toTime) {
		return trackInfoMapper.findInTimeSpan(fieldWorkerId, fromTime,
				toTime);
	}

	/**
	 * 导出轨迹信息
	 */
	public File exportTrackInfo(String fieldWorkerId, String fromTime,
			String toTime) throws IOException {
		List<OneDayTimeSpan> timeSpanList = DateTimeUtil.divideIntoTimeSpan(
				fromTime, toTime);
		List<Long> workerIdList = StringUtil.fromStringToLongList(
				fieldWorkerId, ",");
		List<String> header = Lists.newArrayList("纬度", "经度", "地点名称", "登记时间");
		RowFiller<TrackInfo> trackInfoRowFiller = new TrackInfoRowFiller();
		// List<File>
		// excelFiles=Lists.newArrayListWithExpectedSize(timeSpanList.size());
		File tempDir = FileUtil.createTempDir(String.valueOf(System
				.currentTimeMillis()));
		for (OneDayTimeSpan _timeSpan : timeSpanList) {
			TrackInfoSheetDataSource sheetDataSource = new TrackInfoSheetDataSource(
					workerIdList, _timeSpan.getFromTime().toDate(), _timeSpan
							.getToTime().toDate());
			File _excelFile = new File(tempDir, _timeSpan.getFromTime()
					.toString(DateTimeUtil.getYmdFormatter()) + ".xlsx");
			ExcelExportContext<TrackInfo> _exportContext = new ExcelExportContext<TrackInfo>(
					_excelFile, header, sheetDataSource, trackInfoRowFiller);
			ExcelUtil.exportTrackInfo(_exportContext);
			// excelFiles.add(_excelFile);
		}
//		File zipFile=new File(FileUtil.getSystemTempDir(),"服务人员轨迹("+fromTime+"---"+toTime+").zip");
		File zipFile=new File(FileUtil.getSystemTempDir(),"服务人员轨迹_"+String.valueOf(System.currentTimeMillis())+".zip");
		ZipUtil.createZip(zipFile, tempDir,"**/*.xlsx");
		return zipFile;

	}

	
	/**
	 * 根据表单创建轨迹信息
	 * @param command
	 * @return
	 */
	private TrackInfo fromTrackInfo(CreateTrackInfoCommand command) {
		TrackInfo trackInfo = new TrackInfo();
		trackInfo.setFieldWorkerId(Long.parseLong(command.getFieldWorkerNo()));
		trackInfo.setAreaCode(command.getAreaCode());
		trackInfo.setLatitude(command.getLatitude());
		trackInfo.setLongitude(command.getLongitude());
		trackInfo.setAddress(command.getAddress());
		Date _createTime = null;
		if (StringUtils.isEmpty(command.getCreateTime())) {
			_createTime = DateTimeUtil.parseAsYYYYMMddHHmmss(command
					.getCreateTime());
		} else {
			_createTime = new Date();
		}
		trackInfo.setCreateTime(_createTime);
		return trackInfo;
	}

	/**
	 * 轨迹覆盖信息
	 * @author aisino_lzw
	 *
	 */
	private class TrackInfoSheetDataSource implements
			SheetDataSource<TrackInfo> {

		private final Date fromTime;

		private final Date toTime;

		private Iterator<Long> fieldWorkerIdIterator;

		public TrackInfoSheetDataSource(List<Long> fieldWorkerIdList,
				Date fromTime, Date toTime) {
			this.fieldWorkerIdIterator = fieldWorkerIdList.iterator();
			this.fromTime = fromTime;
			this.toTime = toTime;
		}

		@Override
		public boolean hasNext() {
			return fieldWorkerIdIterator.hasNext();
		}

		@Override
		public SheetItem<TrackInfo> next() {
			Long fieldWorkerId = fieldWorkerIdIterator.next();
			FieldWorker fieldWorker = fieldWorkerMapper.getById(fieldWorkerId);
			List<TrackInfo> trackInfoList = trackInfoMapper.findInTimeSpan(
					fieldWorkerId, fromTime, toTime);
			return new SheetItem<TrackInfo>(fieldWorker.getFullname(),
					trackInfoList);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * 轨迹显示
	 * @author aisino_lzw
	 *
	 */
	private class TrackInfoRowFiller implements RowFiller<TrackInfo> {

		@Override
		public void fillRow(TrackInfo t, Row row) {
			Cell _cell0 = row.createCell(0);
			_cell0.setCellValue(t.getLatitude());

			Cell _cell1 = row.createCell(1);
			_cell1.setCellValue(t.getLongitude());


			Cell _cell2 = row.createCell(2);
			_cell2.setCellValue(t.getAddress());
			
			Cell _cell3 = row.createCell(3);
			_cell3.setCellValue(DateTimeUtil.formatAsYYYYMMddHHmmss(t
					.getCreateTime()));
		}

	}

}
