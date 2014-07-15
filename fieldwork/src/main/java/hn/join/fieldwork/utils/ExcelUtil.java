package hn.join.fieldwork.utils;

import hn.join.fieldwork.domain.TrackInfo;
import hn.join.fieldwork.web.command.CheckinCommand;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * Excel文件操作工具类
 * @author chenjinlong
 *
 */
public class ExcelUtil {

	public final static void exportTrackInfo(
			ExcelExportContext<TrackInfo> context) throws IOException {
		
		Workbook _workbook = new XSSFWorkbook();
		doExport(_workbook,context);
		FileOutputStream fos = new FileOutputStream(context.excelFile);
		try {
			_workbook.write(fos);
		} finally {
			IOUtils.closeQuietly(fos);
		}

	}
	
	public final static void exportCheckin(
			ExcelExportContext<CheckinCommand> context) throws IOException {
		
		Workbook _workbook = new XSSFWorkbook();
		doExport(_workbook,context);
		FileOutputStream fos = new FileOutputStream(context.excelFile);
		try {
			_workbook.write(fos);
		} finally {
			IOUtils.closeQuietly(fos);
		}

	}
	
	


	private static <T> void doExport(Workbook workbook,
			ExcelExportContext<T> context) {

		SheetDataSource<T> _sheetDataSource = context.sheetDataSource;
		RowFiller<T> _rowFiller = context.rowFiller;
		while (_sheetDataSource.hasNext()) {
			SheetItem<T> _sheetItem = _sheetDataSource.next();
			Sheet _sheet = workbook.createSheet(_sheetItem.getSheetName());
			int rowIndex = 0;
			outputHeader(_sheet, context.header, rowIndex);
			List<T> data = _sheetItem.getSheetData();
			if (data != null && !data.isEmpty()) {
				for (T t : data) {
					Row _row = _sheet.createRow(++rowIndex);
					_rowFiller.fillRow(t, _row);
				}
			}
		}
	}
	
	

	private static void outputHeader(Sheet sheet, List<String> header,
			int rowIndex) {
		Row headerRow = sheet.createRow(rowIndex);
		Iterator<String> it = header.iterator();
		int i = 0;
		for (; it.hasNext();) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(it.next());
			i++;
		}

	}

	public static class ExcelExportContext<T> {

		private final File excelFile;

		private final List<String> header;

		private final SheetDataSource<T> sheetDataSource;

		private final RowFiller<T> rowFiller;

		public ExcelExportContext(File excelFile, List<String> header,
				SheetDataSource<T> sheetDataSource, RowFiller<T> rowFiller) {
			super();
			this.excelFile = excelFile;
			this.header = header;
			this.sheetDataSource = sheetDataSource;
			this.rowFiller = rowFiller;
		}

	}

	public interface SheetDataSource<T> extends Iterator<SheetItem<T>> {

	}

	public interface RowFiller<T> {

		void fillRow(T t, Row row);

	}

	public static class SheetItem<T> {
		private final String sheetName;

		private final List<T> sheetData;

		public SheetItem(String sheetName, List<T> sheetData) {
			super();
			this.sheetName = sheetName;
			this.sheetData = sheetData;
		}

		public String getSheetName() {
			return sheetName;
		}

		public List<T> getSheetData() {
			return sheetData;
		}

	}

}
