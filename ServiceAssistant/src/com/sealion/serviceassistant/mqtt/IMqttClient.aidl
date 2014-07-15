package com.sealion.serviceassistant.mqtt;

interface IMqttClient
{
	void sendMessageByMqtt(in byte[] message);
}