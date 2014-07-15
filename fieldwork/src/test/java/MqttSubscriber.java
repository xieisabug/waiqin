import java.net.URISyntaxException;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

public class MqttSubscriber {

	private static MQTT mqtt;
	private static CallbackConnection connection;

	private static void buildMqtt() throws URISyntaxException {
		mqtt = new MQTT();
		mqtt.setHost("tcp://192.168.1.123:1883");
		mqtt.setClientId("test-1");
		mqtt.setCleanSession(false);
		mqtt.setKeepAlive((short) 60);
	}
	
	private static void subscriber() throws Exception{
		FutureConnection connection = mqtt.futureConnection();
		Future<Void> f1 = connection.connect();
		f1.await();

		Future<byte[]> f2 = connection.subscribe(new Topic[]{new Topic("/orderinfo/000001/#", QoS.EXACTLY_ONCE)});
		byte[] qoses = f2.await();

		// We can start future receive..
		
		while(true){
			Future<Message> receive = connection.receive();
			Message message=receive.await();
			System.out.println(message.getTopic()+":"+new String(message.getPayload()));
			message.ack();
		}
	}

	private static void buildCallbackConnection() {
		connection = mqtt.callbackConnection();
		connection.listener(new Listener() {

			public void onDisconnected() {
				System.out.println("disconnected");
			}

			public void onConnected() {
				System.out.println("connected");
			}

			public void onSuccess(UTF8Buffer topic, Buffer payload, Runnable ack) {
				System.out.println("onSuccess");
				System.out.println(new String(topic.getData()));
				System.out.println(new String(payload.getData()));
				ack.run();
			}

			public void onFailure(Throwable value) {
				value.printStackTrace();
			}

			@Override
			public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
				System.out.println("onPublish");
				System.out.println(new String(topic.getData()));
				System.out.println(new String(body.getData()));
				ack.run();

			}
		});
	}

	private static void buildSubscriber() {
		connection.connect(new Callback<Void>() {
			public void onFailure(Throwable value) {
				System.out.println("onFailure");
				value.printStackTrace();
			}

			// Once we connect..
			public void onSuccess(Void v) {

				// Subscribe to a topic
				Topic[] topics = { new Topic("/orderinfo/000002/1",
						QoS.AT_LEAST_ONCE) };
				connection.subscribe(topics, new Callback<byte[]>() {
					public void onSuccess(byte[] qoses) {
						System.out.println("onSuccess");
//						connection.
						System.out.println(new String(qoses));
					}

					public void onFailure(Throwable value) {
						System.out.println("onFailure");
						value.printStackTrace();
					}
				});

				// Send a message to a topic
//				connection.publish("foo", "Hello".getBytes(),
//						QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
//							public void onSuccess(Void v) {
//								// the pubish operation completed successfully.
//							}
//
//							public void onFailure(Throwable value) {
//								System.out.println("onFailure");
//								value.printStackTrace();
//							}
//						});

				// To disconnect..
				connection.disconnect(new Callback<Void>() {
					public void onSuccess(Void v) {
						System.out.println("onSuccess");
						// called once the connection is disconnected.
					}

					public void onFailure(Throwable value) {
						System.out.println("onFailure");
						value.printStackTrace();
					}
				});
			}
		});
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		buildMqtt();
		subscriber();
//		buildCallbackConnection();
//		buildSubscriber();
		Thread.sleep(3600*1000*1000L);
	}

}
