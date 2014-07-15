package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.TrackInfo;
import hn.join.fieldwork.service.TrackInfoService;
import hn.join.fieldwork.service.WorkOrderService;
import hn.join.fieldwork.utils.DateTimeUtil;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.WorkOrderLess;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
/**
 * 外勤人员轨迹管理控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/trackinfo/")
public class TrackInfoController extends BaseController {

	private static final Logger LOG = Logger
			.getLogger(TrackInfoController.class);

	public static final String listTrackInfoPermission = "trackinfo:list";

	public static final String exportTrackInfoPermission = "trackinfo:export";

	@Autowired
	private TrackInfoService trackInfoService;

	@Autowired
	private WorkOrderService workOrderService;

	@RequestMapping(method = RequestMethod.GET)
	public String trackInfoIndex() {
		return "trackMgr";
	}

	@PostConstruct
	public void init() {
		addPermission(listTrackInfoPermission);
		addPermission(exportTrackInfoPermission);
	}
	/**
	 * 查找员工轨迹
	 * @param currentUserId
	 * @param fieldWorkerId
	 * @param fromTime
	 * @param toTime
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/list", method = RequestMethod.GET, headers = "Accept=text/plain")
	@ResponseBody
	public String list(@PathVariable Long currentUserId,  @RequestParam(required = true) Long fieldWorkerId,
			@RequestParam(required = true) String fromTime,
			@RequestParam(required = true) String toTime) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			Date _fromTime = DateTimeUtil.parseAsYYYYMMddHHmmss(fromTime);
			Date _toTime = DateTimeUtil.parseAsYYYYMMddHHmmss(toTime);

			List<TrackInfo> trackInfoList = trackInfoService.findInTimeSpan(
					fieldWorkerId, _fromTime, _toTime);
			List<WorkOrderLess> workOrderLessList = workOrderService
					.findWorkOrderLessFinishTimeBetween(fieldWorkerId,
							_fromTime, _toTime);
			resultCode = SystemConstants.result_code_on_success;
			results.put("trackInfo", trackInfoList);
			results.put("workOrder", workOrderLessList);
		} catch (Exception ex) {
			LOG.error("查找员工轨迹失败,fieldWorkerId:" + fieldWorkerId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 导出轨迹文件
	 * @param fieldWorkerId
	 * @param fromTime
	 * @param toTime
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "{currentUserId}/export", method = RequestMethod.GET)
	public void export(@RequestParam(required = true) String fieldWorkerId,
			@RequestParam(required = true) String fromTime,
			@RequestParam(required = true) String toTime,
			HttpServletRequest request, HttpServletResponse response) {

		InputStream input = null;
		OutputStream output = null;
		try {
			File exportFile = trackInfoService.exportTrackInfo(fieldWorkerId,
					fromTime, toTime);
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
			LOG.error("导出轨迹文件失败,fieldWorkerId:" + fieldWorkerId + ",fromTime:"
					+ fromTime + ",toTime:" + toTime, ex);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}

	}
}
