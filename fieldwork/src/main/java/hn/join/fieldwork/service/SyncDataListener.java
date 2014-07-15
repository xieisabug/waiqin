package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.SyncDataTransferObject;
import hn.join.fieldwork.persistence.SyncDataTransferObjectMapper;
import hn.join.fieldwork.utils.SystemConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;


/**
 * 数据同步(向crm发送数据线程)
 * @author aisino_lzw
 *
 */
@Component
public class SyncDataListener {

	private final Logger logger = Logger.getLogger(getClass());

	private String url;

	private final HttpClient httpClient = new HttpClient();

	@Autowired
	private SyncDataTransferObjectMapper syncDtoMapper;

	@Autowired
	private SystemEventBus systemEventBus;

	private ExecutorService loadSyncDataExecutor = Executors
			.newSingleThreadExecutor();

	private ExecutorService feedbackThreadPoolExecutor = new ThreadPoolExecutor(
			4, 8, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(
					500), new CallerRunsPolicy());

	private ScheduledExecutorService sweepStaleDataExecutor = Executors
			.newScheduledThreadPool(1);

	@Subscribe
	public void onSyncData(SyncDataTransferObject syncObject) {
		syncDtoMapper.insertSyncDataTransferObject(syncObject);

	}

	
	/**
	 * 数据初始化
	 */
	@PostConstruct
	public void init() {
		url = SystemConstants.getFeedbackUrl();
		setupHttpClient();
		systemEventBus.getEventBus().register(this);
		sweepStaleDataExecutor.schedule(new SweepStaleDataTask(), 1,
				TimeUnit.DAYS);
		 loadSyncDataExecutor.submit(new LoadSyncDataTask());
	}

	/**
	 * 销毁处理
	 */
	public void destroy() {
		systemEventBus.getEventBus().unregister(this);
		 sweepStaleDataExecutor.shutdown();
		loadSyncDataExecutor.shutdown();
		feedbackThreadPoolExecutor.shutdown();

	}

	/**
	 * 设置客户端
	 */
	private void setupHttpClient() {
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		httpClient.getParams().setCookiePolicy(CookiePolicy.DEFAULT);
		params.setSoTimeout(5000);
		params.setConnectionTimeout(60 * 1000);
		params.setDefaultMaxConnectionsPerHost(4);
		params.setMaxTotalConnections(16);
		MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		manager.setParams(params);
		httpClient.setHttpConnectionManager(manager);
	}

	
	/**
	 * 清除陈旧任务线程
	 * @author aisino_lzw
	 *
	 */
	private class SweepStaleDataTask implements Runnable {
		public void run() {
			try {
				syncDtoMapper.deleteStaleData();
			} catch (Exception ex) {
				logger.error("删除过期的同步数据失败", ex);
			}
		}
	}

	/**
	 * 加载同步任务线程
	 * @author aisino_lzw
	 *
	 */
	private class LoadSyncDataTask implements Runnable {

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					List<SyncDataTransferObject> syncObjectList = syncDtoMapper
							.findDataTransferObjectForSync();
					if (!syncObjectList.isEmpty()) {
						List<Integer> syncIdList = Lists
								.newArrayListWithCapacity(syncObjectList.size());
						for (SyncDataTransferObject syncObject : syncObjectList) {
							syncIdList.add(syncObject.getId());
						}
						syncDtoMapper.updateOnSync(syncIdList);
						for (SyncDataTransferObject syncObject : syncObjectList) {
							feedbackThreadPoolExecutor
									.submit(new DoFeedbackTask(syncObject));
						}
					} else {
						Thread.sleep(10000L);
					}
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				} catch (Exception ex) {
					logger.error("", ex);
				}
			}
		}

	}

	
	/**
	 * 反馈任务线程
	 * @author aisino_lzw
	 *
	 */
	private class DoFeedbackTask implements Runnable {

		private final SyncDataTransferObject syncObject;

		public DoFeedbackTask(SyncDataTransferObject syncObject) {
			this.syncObject = syncObject;
		}

		@Override
		public void run() {
			String feedbackXml = syncObject.getDto();
			String flag = null;
			switch (syncObject.getDtoType()) {
			case order_view:
				flag = "a";
				break;
			case order_received:
				flag = "b";
				break;
			case order_reassign:
				flag = "c";
				break;
			case customer_call:
				flag = "d";
				break;
			case solution_by_remote:
				flag = "e";
				break;
			case dest_arrive:
				flag = "f";
				break;
			case order_finish:
				flag = "g";
				break;
			case receipt_feed:
				flag = "h";
				break;
			case cust_info:
				flag="i";

			}
			if (feedbackXml != null && flag != null) {
				try {
					String result = feedbackByHttpClient(feedbackXml, flag);
					if ("1".equals(result)) {
						syncDtoMapper.updateOnSucc(syncObject.getId());
					} else {
						logger.error("同步工单状态失败,feedbackXml:" + feedbackXml
								+ "\n 返回值:" + result);
					}
				} catch (IOException e) {
					logger.error("同步工单状态失败,feedbackXml:" + feedbackXml, e);
				}
			}

		}

	}

	/**
	 * 通过http协议反馈任务
	 * @param feedbackXml
	 * @param flag
	 * @return
	 * @throws IOException
	 */
	private String feedbackByHttpClient(String feedbackXml, String flag)
			throws IOException {
		String _url = url + "?flag=" + flag;
		PostMethod post = new PostMethod(_url);
		RequestEntity requestEntity = new ByteArrayRequestEntity(
				feedbackXml.getBytes("UTF-8"));
		post.setRequestEntity(requestEntity);
		post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		int result = 0;
		InputStream in = null;
		byte[] resultBytes = null;
		try {
			result = httpClient.executeMethod(post);
			if (result == HttpStatus.SC_OK) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				in = post.getResponseBodyAsStream();
				IOUtils.copy(in, baos);
				resultBytes = baos.toByteArray();
			} else {
				throw new IOException("远程服务器错误,返回 HTTP状态码:" + result);
			}

		} catch (HttpException e) {
			throw new IOException(e);
		} finally {
			IOUtils.closeQuietly(in);
			post.releaseConnection();
		}
		return new String(resultBytes, "UTF-8");
	}

}
