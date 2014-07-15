package hn.join.fieldwork.web;

import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.CheckinService;
import hn.join.fieldwork.utils.DateTimeUtil;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.CheckinCommand;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/web/checkin/")
public class CheckinController extends BaseController {

	private static final Logger LOG = Logger.getLogger(CheckinController.class);

	public static final String listCheckinPermission = "checkin:list";

	public CheckinController() {

	}

	@PostConstruct
	public void init() {
		this.addPermission(listCheckinPermission);
	}

	@Autowired
	private CheckinService checkinService;
	/**
	 * 查看签到
	 * @param currentUserId
	 * @param fromDateString
	 * @param toDateString
	 * @param showAll
	 * @param fieldWorkerName
	 * @param city
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String findCheckin(
			@PathVariable Long currentUserId,
			@RequestParam(required = false, value = "fromDate") String fromDateString,
			@RequestParam(required = false, value = "toDate") String toDateString,
			@RequestParam(defaultValue = "false") Boolean showAll,
			@RequestParam(required = false) String fieldWorkerName,
			@RequestParam(required = false) String city,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		Date fromDate = null;
		Date toDate = null;
		LocalDate now = LocalDate.now();
		if (!StringUtils.isEmpty(fromDateString)) {
			fromDate = DateTimeUtil.parseAsYYYYMMdd(fromDateString);
		} else {
			fromDate = now.minusDays(6).toDate();
		}
		if (!StringUtils.isEmpty(toDateString)) {
			toDate = DateTimeUtil.parseAsYYYYMMdd(toDateString);
		} else {
			toDate = now.toDate();
		}
		try {
			if (fromDate.after(toDate)) {
				results.put("error", "起始日期大于结束日期");
			} else {
				SQLQueryResult<CheckinCommand> queryResult = checkinService
						.findCheckinBy(fromDate, toDate, showAll,
								fieldWorkerName, city, page, rows);
				results.put("total", queryResult.getTotal());
				results.put("rows", queryResult.getRows());
				resultCode = SystemConstants.result_code_on_success;
			}
		} catch (Exception ex) {
			LOG.error("获取签到信息失败,userId:" + currentUserId + "fromDate"
					+ fromDateString + ",toDate:" + toDateString + "showAll:"
					+ showAll + ",fieldWorkerName:" + fieldWorkerName
					+ ",city:" + city + ",page:" + page + ",rows" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	
	/**
	 * 签到记录导出
	 * @param currentUserId
	 * @param fromDateString
	 * @param toDateString
	 * @param showAll
	 * @param fieldWorkerName
	 * @param city
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "{currentUserId}/export")
	@ResponseBody
	public void export(
			@PathVariable Long currentUserId,
			@RequestParam(required = false, value = "fromDate") String fromDateString,
			@RequestParam(required = false, value = "toDate") String toDateString,
			@RequestParam(defaultValue = "false") Boolean showAll,
			@RequestParam(required = false) String fieldWorkerName,
			@RequestParam(required = false) String city,
			HttpServletRequest request, HttpServletResponse response){
		
		Date fromDate = null;
		Date toDate = null;
		LocalDate now = LocalDate.now();
		if (!StringUtils.isEmpty(fromDateString)) {
			fromDate = DateTimeUtil.parseAsYYYYMMdd(fromDateString);
		} else {
			fromDate = now.minusDays(6).toDate();
		}
		if (!StringUtils.isEmpty(toDateString)) {
			toDate = DateTimeUtil.parseAsYYYYMMdd(toDateString);
		} else {
			toDate = now.toDate();
		}
		
		InputStream input = null;
		OutputStream output = null;
		try {
			File exportFile = checkinService.exportCheckin(fromDate, toDate, showAll, fieldWorkerName, city);
			input = new FileInputStream(exportFile);
			output = response.getOutputStream();

			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "No-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("application/octet-stream");
			String fileName = FilenameUtils.getName(exportFile.getPath());
			String agent = request.getHeader("USER-AGENT");
			if (null != agent && -1 != agent.indexOf("MSIE")) {
				fileName = URLEncoder.encode(fileName, "UTF8");
			} else {
				fileName = "=?UTF-8?B?"
						+ (new String(Base64.encodeBase64(fileName
								.getBytes("UTF-8")))) + "?=";
			}

			response.setHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			IOUtils.copy(input, output);
		} catch (Exception ex) {
			LOG.error("导出签到查询结果失败,fromDate:" + fromDateString + ",toDate:"
					+ toDateString + ",showAll:" + showAll+",fieldWorkerName:"+fieldWorkerName+",city"+city, ex);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
		
	}

}
