package hn.join.fieldwork.persistence;

import java.util.Collections;
import java.util.List;
/**
 * 查询结果泛型，包含记录总数以及数据记录
 * @author chenjinlong
 *
 * @param <T>
 */
public class SQLQueryResult<T> {
	
	public final static SQLQueryResult EMPTY_RESULT=new SQLQueryResult(0,Collections.EMPTY_LIST);

	public final long total;

	public final List<T> rows;

	public SQLQueryResult(long totalRecords, List<T> data) {
		super();
		this.total = totalRecords;
		this.rows = data;
	}

	public long getTotal() {
		return total;
	}

	public List<T> getRows() {
		return rows;
	}

	
}
