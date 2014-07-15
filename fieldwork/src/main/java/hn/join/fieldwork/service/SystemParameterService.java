package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.SystemParameter;
import hn.join.fieldwork.persistence.SystemParameterMapper;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统设置服务类
 * @author aisino_lzw
 *
 */
@Component
public class SystemParameterService {
	
	public static final String PARAM_NAME_CHECKIN_AHEAD_MINUTES="checkin_ahead_minutes";
	
	public static final String PARAM_NAME_CHECKIN_SMS_ENABLE="checkin_sms_enable";
	
	public static final String PARAM_NAME_CHECKIN_SMS_MESSAGE="checkin_sms_message";
	
	public static final String PARAM_NAME_CHECKIN_TIME="checkin_time";
	
	public static final String PARAM_NAME_ORDER_TIMEOUT="order_timeout";
	
	public static final String PARAM_NAME_VISIT_TOKEN_PREFIX="visit_token_prefix";
	
	public static final String PARAM_NAME_REPAIR_TOKEN_PREFIX="repair_token_prefix";
	
	public static final String PARAM_NAME_SMS_CUSTOMER_ON_ACCEPT_MESSAGE="sms_customer_on_accept_message";
	
	public static final String PARAM_NAME_SMS_CUSTOMER_ON_ACCEPT_ENABLE="sms_customer_on_accept_enable";
	
	public static final String PARAM_NAME_SMS_FIELDWORKER_ON_TIMEOUT_MESSAGE="sms_fieldworker_on_timeout_message";
	
	public static final String PARAM_NAME_SMS_FIELDWORKER_ON_TIMEOUT_ENABLE="sms_fieldworker_on_timeout_enable";

	private ScheduledExecutorService loadSystemParameterExecutor = Executors
			.newScheduledThreadPool(1);

	@Autowired
	private SystemParameterMapper systemParamterMapper;

	private volatile List<SystemParameter> systemParameters;

	@PostConstruct
	public void init() {
		systemParameters = systemParamterMapper.loadAll();
		loadSystemParameterExecutor.scheduleAtFixedRate(
				new LoadSystemParameterTask(), 0, 30, TimeUnit.SECONDS);
	}

	@PreDestroy
	public void destroy() {
		loadSystemParameterExecutor.shutdownNow();
	}

	/**
	 * 查询所有系统参数
	 * @return
	 */
	public List<SystemParameter> loadAll() {
		return systemParameters;
	}

	/**
	 * 根据参数名获取参数值
	 * @param paramName
	 * @return
	 */
	public String getValueByName(String paramName) {
		for (SystemParameter _param : systemParameters) {
			if (paramName.equals(_param.getParamName())) {
				return _param.getParamValue();
			}
		}
		return null;
	}

	/**
	 * 更新系统参数
	 * @param paramName
	 * @param paramValue
	 */
	public void updateParameter(String paramName, String paramValue) {
		for (SystemParameter _param : systemParameters) {
			if (paramName.equals(_param.getParamName())) {
				_param.setParamValue(paramValue);
				systemParamterMapper.updateSystemParameter(paramName,
						paramValue);
				break;
			}
		}
	}

	/**
	 * 加载系统参数
	 * @author aisino_lzw
	 *
	 */
	private class LoadSystemParameterTask implements Runnable {

		@Override
		public void run() {
			systemParameters = systemParamterMapper.loadAll();
		}

	}

}
