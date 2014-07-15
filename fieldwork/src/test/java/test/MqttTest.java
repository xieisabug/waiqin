package test;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.Task;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
/**
 * This is a mqtt test Class
 * if you dispatch workorder,please change "workCardId" in publicMessage function
 * @author chenjl
 *
 */
public class MqttTest {
	public static void main(String[] args) throws IOException {
		//create MQTT Instance
		MQTT mqtt=null;
		try {
			 mqtt = createClient();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		//create connection
		CallbackConnection connection =createConnection(mqtt);
		//public message
		publicMessage(connection);
	}
	//public message function
	private static void publicMessage(final CallbackConnection connection) throws IOException{
		
		//public notification
		//String topic="/notification";
		//String message = "{'id':48,'title':'test','content':'1-test','city':null,'publishDate':'2014-06-10 19:11','createTime':null}";
		
		//public order,please change a unique workCardId befor dispatch message!!!! 
		String topic="/orderinfo/154923/new";	
		String message="{'workCardId':35000062636,'serviceDate':'2014-06-10 19:06:19','workOrderType':1,'visitType':null,'customerDto':{'customerId':10182160,'linkPerson':'������','customerName':'��ɳ�ؼ��廨�ڳ�','taxCode':'430124675588607','departmentId':719,'departmentName':'���Ϻ�����Ϣ��ɳ�������','revenueId':null,'revenueName':null,'customerAddr':'��ɳ��������ú̿����ؼ�����԰��','customerMobile':'13952540259','customerTel':'073187906779','customerCounty':'��ɳ����һ���800����¡��ʴ���22¥','customerLatitude':null,'customerLongitude':null},'fieldWorkerId':154923,'fieldWorkerName':'����ӱ','workOrderDescription':null,'expectArriveTime':'2014-06-11 00:00:00','problemList':[{'problemId':35000210051,'questionTypId':2944,'questionTypPid':3013,'productId':729,'productName':'��α˰�ؿ�Ʊϵͳ','questionDesc':'�û���װ�˲���ϵͳ����ָ�A6��ҵ�������','solution':null}],'chargeType':2,'chargeMoney':0.0,'urgency':1,'city':'��ɳ����һ���8','orderToken':'WH20140600000','returnProductId':null,'returnProductName':null,'productStatus':null,'handleResult':null,'route':null}";
		
		final UTF8Buffer topicBuffer = new UTF8Buffer(topic);
		final Buffer messageBuffer = new Buffer(message.getBytes("UTF-8"));		
		System.out.println("begin task");
		Task task = new Task() {
			public void run() {
				System.out.println("task run");				
				connection.publish(topicBuffer,messageBuffer ,QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
					public void onSuccess(Void value) {
						System.out.println("publish success");
					}

					public void onFailure(Throwable ex) {
						System.out.println("public failure");
					}
				});
			}
		};
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(task);
	}
	
		
	//create mqtt client
	private static MQTT createClient() throws URISyntaxException{
		String uri="tcp://222.240.218.91:1883";
		String clientId="orderinfo-dispatch-5";
		MQTT mqtt = new MQTT();
		mqtt.setHost(uri);
		mqtt.setClientId(clientId);
		mqtt.setCleanSession(true);	   
		return mqtt;
	}
	//create mqtt connection
	private static CallbackConnection createConnection(MQTT mqtt){
		CallbackConnection connection=mqtt.callbackConnection();
		connection = mqtt.callbackConnection();
		//
		connection.listener(new org.fusesource.mqtt.client.Listener() {

			public void onConnected() {
				System.out.println("Connected");
			}

			public void onDisconnected() {
				System.out.println("Disconnected");
			}

			public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
			}

			public void onFailure(Throwable ex) {
				ex.printStackTrace();
			}
		});		
		connection.resume();
		connection.connect(new Callback<Void>() {
			public void onFailure(Throwable ex) {
				System.out.println("onFailure");
				ex.printStackTrace();
			}
			public void onSuccess(Void value) {
				System.out.println("onSuccess");
			}
		});
		return connection;
	}
	
	
}

