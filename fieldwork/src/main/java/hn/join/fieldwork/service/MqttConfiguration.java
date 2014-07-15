package hn.join.fieldwork.service;

import java.net.URISyntaxException;

import org.fusesource.mqtt.client.MQTT;

/**
 * mqtt配置类
 * @author aisino_lzw
 *
 */
public class MqttConfiguration {
	
	private String uri;
	
	private String clientId;
	
	private boolean retain;


	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public boolean isRetain() {
		return retain;
	}

	public void setRetain(boolean retain) {
		this.retain = retain;
	}
	
	public void applyMQTT(MQTT mqtt) throws URISyntaxException{
		mqtt.setHost(uri);
		mqtt.setClientId(clientId);
		mqtt.setCleanSession(false);
	}

    @Override
    public String toString() {
        return "MqttConfiguration{" +
                "uri='" + uri + '\'' +
                ", clientId='" + clientId + '\'' +
                ", retain=" + retain +
                '}';
    }
}
