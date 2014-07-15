package hn.join.fieldwork.web;

import hn.join.fieldwork.service.FieldWorkerService;
import hn.join.fieldwork.service.FieldWorkerService.LatestFieldWorkerInfoCommand;
import hn.join.fieldwork.service.WorkOrderService;
import hn.join.fieldwork.utils.FileUtil;
import hn.join.fieldwork.utils.HashUtil;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.utils.XmlUtil;
import hn.join.fieldwork.utils.ZipUtil;
import hn.join.fieldwork.web.dto.FieldWorkerDto;
import hn.join.fieldwork.web.dto.WorkOrderDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
/**
 * 对外接口控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/interface/*")
public class InterfaceController {

	private final static Logger LOG = Logger
			.getLogger(InterfaceController.class);

	@Autowired
	private FieldWorkerService fieldWorkerService;

	@Autowired
	private WorkOrderService workOrderService;
	/**
	 * 接收工单XML
	 * @param areaCode
	 * @param customerAddress
	 * @param latitude
	 * @param longitude
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "assist")
	public String assist(@RequestParam(required = true) String areaCode,
			@RequestParam(required = true) String customerAddress,
			@RequestParam(required =false) Float latitude,
			@RequestParam(required= false) Float longitude,			
			Model model) {
		try {
			
//			String areaCode2 = new String(areaCode.getBytes("UTF-8"),"UTF-8");
//			customerAddress = new String(customerAddress.getBytes("UTF-8"),"UTF-8");
//			LOG.error("UTF-8 areaCode="+areaCode2);
		
			
			
			
			
			// System.out.println(new String(areaCode.getBytes("gbk")));
			Collection<Long> fieldWorkerIdCollection = fieldWorkerService
					.getFieldWorkerIdByAreaCodeOnLine(areaCode);
			if (fieldWorkerIdCollection != null) {
				List<LatestFieldWorkerInfoCommand> fieldWorkerInfoList = fieldWorkerService
						.findLatestFieldWorkerInfoOnLine(fieldWorkerIdCollection);
				model.addAttribute("fieldWorkerInfoList",
						JsonUtil.toJson(fieldWorkerInfoList));
			}

		} catch (Exception ex) {
			model.addAttribute("fieldWorkerInfoList", Collections.EMPTY_LIST);
		}
		model.addAttribute("areaCode", areaCode);
		model.addAttribute("customerAddress", customerAddress);
		model.addAttribute("latitude", latitude);
		model.addAttribute("longitude", longitude);
		return "getRoute";
	}

	@RequestMapping(value = "work-order")
	@ResponseBody
	public String onWorkOrder(HttpServletRequest request) {
		int resultCode = SystemConstants.result_code_on_failure;
		String xml = null;
		try {
			byte[] base64Data = IOUtils.toByteArray(request.getInputStream());
			xml = new String(HashUtil.decodeBase64(base64Data), "utf-8");
//			System.out.println(new String(HashUtil.decodeBase64(base64Data),"GBK"));
			System.out.println(xml);
			WorkOrderDto workOrderDto = XmlUtil.toObject(WorkOrderDto.class,
					xml);
			workOrderService.createWorkOrder(workOrderDto);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("接受新工单失败,xml:" + xml, ex);
		}
		return String.valueOf(resultCode);
	}
	/**
	 * 同步外勤人员xml
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "fieldworker")
	@ResponseBody
	public String onFieldworker(HttpServletRequest request) {
		int resultCode = SystemConstants.result_code_on_failure;
		String xml = null;
		try {
			byte[] base64Data = IOUtils.toByteArray(request.getInputStream());
			xml = new String(HashUtil.decodeBase64(base64Data), "UTF-8");
			FieldWorkerDto fieldWorkerDto = XmlUtil.toObject(
					FieldWorkerDto.class, xml);
			fieldWorkerService.syncFieldWorker(fieldWorkerDto);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("同步外勤人员失败,xml:" + xml, ex);
		}
		return String.valueOf(resultCode);
	}
	
	
	/**
	 * 获取订单附属信息
	 * @param workOrderNo
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="assets")
	public void viewOrderAssets(@RequestParam(required = true) String workOrderNo,HttpServletRequest request, HttpServletResponse response){
		InputStream input = null;
		OutputStream output = null;
		try{
			File assertDir = new File(SystemConstants.getUploadRepository(), workOrderNo);
			File zipFile=new File(FileUtil.getSystemTempDir(),workOrderNo+".zip");
			ZipUtil.createZip(zipFile, assertDir,null);
			
			input = new FileInputStream(zipFile);
			output = response.getOutputStream();
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "No-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("application/octet-stream");
			String fileName = FilenameUtils.getName(zipFile.getPath());
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
			
		}catch(Exception ex){
			LOG.error("获取订单附属信息失败", ex);
		}finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
		
	}
	
	

}
