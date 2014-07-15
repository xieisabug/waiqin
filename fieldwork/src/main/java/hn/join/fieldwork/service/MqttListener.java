package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.Notification;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.web.dto.WorkOrderDto;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.Task;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;


/**
 * mqtt监听类
 * @author aisino_lzw
 *
 */
@Component
public class MqttListener {

	private MQTT mqtt;
    public final Logger logger = Logger.getLogger(MqttListener.class);
	@Autowired
	private MqttConfiguration mqttConfiguration;

	private CallbackConnection connection;

	private ExecutorService executor = Executors.newSingleThreadExecutor();

	@Autowired
	private SystemEventBus systemEventBus;

	private final PublishFailureCallback defaultPublishFailureCallback = new DefaultPublishFailureCallback();

	/**
	 * 数据初始化
	 * @throws URISyntaxException
	 */
	@PostConstruct
	public void init() throws URISyntaxException {
        logger.info("init");
		buildMQTT();
        logger.info("register");
        systemEventBus.getEventBus().register(this);
	}

	/**
	 * 销毁处理
	 * @throws Exception
	 */
	@PreDestroy
	public void destroy() throws Exception {
        logger.info("destroy");
		executor.shutdown();
	}

	/**
	 * 派送工单
	 * @param workOrderDto
	 * @param failureCallback
	 */
	public void onWorkOrderDispatch(WorkOrderDto workOrderDto,
			PublishFailureCallback failureCallback) {
		String topic = "/orderinfo/" + workOrderDto.getFieldWorkerId() + "/new";
		String message=JsonUtil.toJson(workOrderDto);
		doPublicMessage(topic, message, failureCallback);
	}

	/**
	 * 发布公告
	 * @param notification
	 */
	@Subscribe
	public void onNotification(Notification notification) {
		String topic = "/notification";
		String message = JsonUtil.toJson(notification);
		doPublicMessage(topic, message, defaultPublishFailureCallback);
	}

	// @Subscribe
	// public void onWorkOrderSubstituteRequest(WorkOrderSubstituteRequest
	// request){
	// ApplySubstitutionCommand command=request.getApplySubstitutionCommand();
	// String topic = "/orderinfo/" + command.getApplyWorkerNo() + "/cancel";
	// String message=JsonUtil.toJson(command);
	// doPublicMessage(topic,message);
	// }

	/**
	 * 发布信息
	 */
	private void doPublicMessage(final String topic, final String message,
			final PublishFailureCallback failureCallback) {
		Task task = new Task() {
			public void run() {
				Buffer _message = new UTF8Buffer(message);
//				System.out.println("---------topic:"+topic +"     message:"+message);
                logger.info("doPublicMessage topic:" + topic);
                logger.info("doPublicMessage message:" + message);
				connection.publish(new UTF8Buffer(topic), _message,
						QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
							public void onSuccess(Void value) {
                                logger.info("doPublicMessage publish success");
                            }

							public void onFailure(Throwable ex) {
                                logger.info("doPublicMessage publish fail" );
                                failureCallback.onFailure(ex);
							}
						});
			}
		};
		executor.submit(task);
	}

	/**
	 * 构建mqtt对象
	 * @throws URISyntaxException
	 */
	private void buildMQTT() throws URISyntaxException {
		mqtt = new MQTT();
		mqttConfiguration.applyMQTT(mqtt);
        logger.info("buildMQTT mqttConfiguration:" + mqttConfiguration);
		connection = mqtt.callbackConnection();
		connection.listener(new org.fusesource.mqtt.client.Listener() {

			public void onConnected() {
                logger.info("buildMQTT Connected");
			}

			public void onDisconnected() {
                logger.info("buildMQTT Disconnected");
            }

			public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
                logger.info("buildMQTT onPublish");
            }

			public void onFailure(Throwable ex) {
                logger.info("buildMQTT onFailure");
                ex.printStackTrace();
			}
		});

		connection.resume();
		connection.connect(new Callback<Void>() {
			public void onFailure(Throwable ex) {
                logger.info("buildMQTT connect onFailure");
                ex.printStackTrace();
			}

			public void onSuccess(Void value) {
                logger.info("buildMQTT connect Connected");
            }
		});
        logger.info("MqttListener buildMQTT finish");
    }

	public static interface PublishFailureCallback {
		public void onFailure(Throwable ex);
	}

	public class DefaultPublishFailureCallback implements
			PublishFailureCallback {
		public void onFailure(Throwable ex) {

		}
	}

}
