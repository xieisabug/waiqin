package hn.join.fieldwork.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
/**
 * 系统常量类，用于存储系统静态变量
 * 部分变量通过读取system-configuration.properties属性文件获取
 * @author chenjinlong
 *
 */
public class SystemConstants {

	private static Properties systemConfigurationProps = new Properties();

	private static final String SYSTEM_CONFIGURATION_PROPERTIES = "system-configuration.properties";

	public static final int result_code_on_success = 1;

	public static final int result_code_on_failure = 2;
	
	public static final int result_code_on_auth_fail=3;

	public static final int result_code_on_required_more_info = 4;

	public static final int ajax_timeout = -1;

	private static File upload_repository;

	private static boolean is_master = false;

	private static File data_update_repository;

	private static final String filename_data_update_problem_category;

	private static final String filename_data_update_problem_type;

	private static final String filename_data_update_revenue;

	private static final String filename_data_update_expense_item;

	private static final String feedback_url;

	private static final int upload_server_socket_port;

	private static final String sms_url;

	private static final String sms_username;

	private static final String sms_password;


	static {
		InputStream systemConfigurationInputStream = null;
		try {
			systemConfigurationInputStream = SystemConstants.class
					.getClassLoader().getResourceAsStream(
							SYSTEM_CONFIGURATION_PROPERTIES);

			systemConfigurationProps.load(systemConfigurationInputStream);

			String upload_repository = systemConfigurationProps
					.getProperty("upload_repository");
			readyUploadRepository(upload_repository);



			String _is_master = systemConfigurationProps.getProperty(
					"is_master", "false");
			is_master = Boolean.parseBoolean(_is_master);

			String data_update_repository = systemConfigurationProps
					.getProperty("data_update_repository");
			readyDataUpdateRepository(data_update_repository);

			filename_data_update_problem_category = systemConfigurationProps
					.getProperty("filename_data_update_problem_category");

			filename_data_update_problem_type = systemConfigurationProps
					.getProperty("filename_data_update_problem_type");

			filename_data_update_revenue = systemConfigurationProps
					.getProperty("filename_data_update_revenue");

			filename_data_update_expense_item = systemConfigurationProps
					.getProperty("filename_data_update_expense_item");

			feedback_url = systemConfigurationProps.getProperty("feedback_url");
			// readyImageUploadRepository();
			upload_server_socket_port = Integer
					.parseInt(systemConfigurationProps
							.getProperty("upload_server_socket_port"));

			sms_url = systemConfigurationProps.getProperty("sms_url");

			sms_username = systemConfigurationProps.getProperty("sms_username");

			sms_password = systemConfigurationProps.getProperty("sms_password");

		} catch (Exception ex) {
			throw new RuntimeException("读取配置文件出错了..." + ex.getMessage(), ex);
		} finally {
			IOUtils.closeQuietly(systemConfigurationInputStream);
		}
	}
    /**
     * 获取文件上传目录
     * @param uploadRepositoryLocation
     */
	private static void readyUploadRepository(String uploadRepositoryLocation) {
		upload_repository = new File(uploadRepositoryLocation);
		if (!upload_repository.exists()) {
			upload_repository.mkdir();
		}
	}
	/**
	 * 获取数据更新目录
	 * @param dataUpdateRepositoryLocation
	 */
	private static void readyDataUpdateRepository(
			String dataUpdateRepositoryLocation) {
		data_update_repository = new File(dataUpdateRepositoryLocation);
		if (!data_update_repository.exists())
			data_update_repository.mkdir();
	}
	/**
	 * 获取文件上传目录文件操作对象
	 * @return File
	 */
	public static File getUploadRepository() {
		return upload_repository;
	}
	/**
	 * 获取数据同步JSON文件存放目录
	 * @return File
	 */
	public static File getUpdateDataRepository() {
		return data_update_repository;
	}

	/**
	 * 获取上传端口
	 * @return
	 */
	public static int getUploadServerSocketPort() {
		return upload_server_socket_port;
	}
	/**
	 * 叛断是否主机
	 * @return
	 */
	public static boolean isMaster() {
		return is_master;
	}
	/**
	 * 获取问题类别操作对象
	 * @return
	 */
	public static File getProblemCategoryUpdateFile() {
		return new File(data_update_repository,
				filename_data_update_problem_category);
	}
	/**
	 * 获取问题类型操作对象
	 * @return
	 */
	public static File getProblemTypeUpdateFile() {
		return new File(data_update_repository,
				filename_data_update_problem_type);
	}
	/**
	 * 获取税务局文件操作对象
	 * @return
	 */
	public static File getRevenueUpdateFile() {
		return new File(data_update_repository, filename_data_update_revenue);
	}
	/**
	 * 获取出差补贴项目文件操作对象
	 * @return
	 */
	public static File getExpenseItemUpdateFile() {
		return new File(data_update_repository,
				filename_data_update_expense_item);
	}
	/**
	 * 获取配置文件中的回调URL
	 * @return
	 */
	public static String getFeedbackUrl() {
		return feedback_url;
	}
	/**
	 * 获取短消息发送URL
	 * @return
	 */
	public static String getSmsUrl() {
		return sms_url;
	}
	/**
	 * 获取短消息用户名
	 * @return
	 */
	public static String getSmsUsername() {
		return sms_username;
	}
	/**
	 * 获取短消息密码
	 * @return
	 */
	public static String getSmsPassword() {
		return sms_password;
	}


}
