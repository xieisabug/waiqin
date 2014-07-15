package hn.join.fieldwork.web;

import hn.join.fieldwork.service.SystemEventBus.DataUpdateRequest.DataType;
import hn.join.fieldwork.utils.SystemConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.alibaba.druid.util.StringUtils;
/**
 * 数据同步Servlet
 * @author chenjinlong
 *
 */
public class DataUpdateFilesServlet extends HttpServlet {
	
	private static final long serialVersionUID = -5527774835954290022L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String dataType = req.getParameter("dataType");
		// long version = Long.parseLong(req.getParameter("version"));
		if (!StringUtils.isEmpty(dataType)) {
			DataType _dataType = DataType.valueOf(dataType);
			File _file = null;
			switch (_dataType) {
			case problem_category: {
				_file = SystemConstants.getProblemCategoryUpdateFile();
				break;
			}
			case problem_type: {
				_file = SystemConstants.getProblemTypeUpdateFile();
				break;
			}
			case revenue: {
				_file = SystemConstants.getRevenueUpdateFile();
				break;
			}
			case expense_item: {
				_file = SystemConstants.getExpenseItemUpdateFile();
				break;
			}
			default: {
				_file = null;
				break;
			}
			}
			if (_file != null && _file.exists()) {
				FileInputStream input=new FileInputStream(_file);
				OutputStream output= resp.getOutputStream();
				try {
					IOUtils.copy(input, output);
					output.flush();
				} finally {
					IOUtils.closeQuietly(input);
					IOUtils.closeQuietly(output);
				}
			}
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
