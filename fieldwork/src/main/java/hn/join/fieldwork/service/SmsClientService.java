package hn.join.fieldwork.service;

import hn.join.fieldwork.utils.SystemConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.hnjoin.sms2g.interfaces.hgt.HgtResultCode;
import com.hnjoin.sms2g.interfaces.hgt.cli.HgtClient;
import com.hnjoin.sms2g.interfaces.hgt.dto.MultiSendRequest;
import com.hnjoin.sms2g.interfaces.hgt.dto.SendRequest;


/**
 * 短信客户端服务类
 * @author aisino_lzw
 *
 */
@Service
public class SmsClientService {

	private final Logger logger = Logger.getLogger(getClass());

	private String smsUrl;

	private String smsUsername;

	private String smsPassword;

	private HgtClient hgtClient;

	private final BlockingQueue<SmsMessage> smsQueue = new LinkedBlockingQueue<SmsMessage>(
			1000);
	
	private final ExecutorService sendSmsExecutor=Executors.newSingleThreadExecutor();

	/**
	 * 数据初始化
	 */
	@PostConstruct
	public void init() {
		this.smsUrl = SystemConstants.getSmsUrl();
		this.smsUsername = SystemConstants.getSmsUsername();
		this.smsPassword = SystemConstants.getSmsPassword();
		this.hgtClient = HgtClient.getClientByHttp(smsUrl);
		sendSmsExecutor.execute(new SendMessageTask());
	}
	
	/**
	 * 销毁处理
	 */
	@PreDestroy
	public void destroy(){
		sendSmsExecutor.shutdownNow();
	}

	/**
	 * 发送短信
	 * @param receiver
	 * @param message
	 */
	public void sendMessage(String receiver, String message) {
		if(receiver != null && (receiver.startsWith("1")||receiver.startsWith("861")))
			try {
				smsQueue.put(new SimpleSmsMessage(Lists.newArrayList(receiver),
						message));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
	}

	/**
	 * 群发短信
	 * @param receivers
	 * @param message
	 */
	public void sendMultiMessage(List<String> receivers, String message) {
		try {
			smsQueue.put(new SimpleSmsMessage(receivers, message));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * 发送任务消息
	 * @author aisino_lzw
	 *
	 */
	private class SendMessageTask implements Runnable {

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				SmsMessage smsMessage;
				try {
					smsMessage = smsQueue.take();
					smsMessage.doSendMessage();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

			}

		}

	}

	public void sendP2MMessage(List<String> receivers, List<String> messages) {
		if (receivers.size() != messages.size()) {
			throw new IllegalStateException(
					"sendMessageP2M fail.receivers size not match messages size."
							+ "receivers:" + receivers + ",messages:"
							+ messages);
		}
		try {
			smsQueue.put(new P2MSmsMessage(receivers, messages));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private static interface SmsMessage {

		public void doSendMessage();

	}

	private class SimpleSmsMessage implements SmsMessage {
		private final List<String> receivers;
		private final String message;

		private SimpleSmsMessage(List<String> receivers, String message) {
			super();
			this.receivers = receivers;
			this.message = message;
		}

		/**
		 * 发送消息
		 */
		@Override
		public void doSendMessage() {
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String timestamp = df.format(new Date()); // 设置时间戳
			SendRequest request = new SendRequest(smsUsername, smsPassword,
					timestamp);
			request.appendEntries(receivers);
			request.content(message);
			try {
				HgtResultCode resultCode = hgtClient.sendSms(request);
				if (resultCode != HgtResultCode.code_succ) {
					logger.equals("sendMessage fail return " + resultCode
							+ ". receivers:" + receivers + ",message:"
							+ message);
				}
			} catch (Exception e) {
				logger.equals("sendMessage fail occur exception.receivers:"
						+ receivers + ",message:" + message);
			}

		}

	}

	private class P2MSmsMessage implements SmsMessage {
		private final List<String> receivers;
		private final List<String> messages;

		private P2MSmsMessage(List<String> receivers, List<String> messages) {
			super();
			this.receivers = receivers;
			this.messages = messages;
		}

		@Override
		public void doSendMessage() {
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String timestamp = df.format(new Date()); // 设置时间戳
			MultiSendRequest request = new MultiSendRequest(smsUsername,
					smsPassword, timestamp);
			for (int i = 0; i < receivers.size(); i++) {
				String phoneNo = receivers.get(i);
				if(phoneNo!=null && (phoneNo.startsWith("1") || phoneNo.startsWith("861")))
					request.appendEntry(phoneNo, messages.get(i));
			}
			try {
				HgtResultCode resultCode = hgtClient.sendSms(request);
				if (resultCode != HgtResultCode.code_succ) {

					logger.equals("sendMessageP2M fail return " + resultCode
							+ ". receivers:" + receivers + ",messages:"
							+ messages);
				}
			} catch (Exception e) {
				logger.equals("sendMessageP2M fail occur exception.receivers:"
						+ receivers + ",messages:" + messages);
			}

		}

	}

}
