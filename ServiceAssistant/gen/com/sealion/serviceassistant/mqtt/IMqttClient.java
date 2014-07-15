/*___Generated_by_IDEA___*/

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: G:\\waiqin\\trunk\\ServiceAssistant\\src\\com\\sealion\\serviceassistant\\mqtt\\IMqttClient.aidl
 */
package com.sealion.serviceassistant.mqtt;
public interface IMqttClient extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.sealion.serviceassistant.mqtt.IMqttClient
{
private static final java.lang.String DESCRIPTOR = "com.sealion.serviceassistant.mqtt.IMqttClient";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.sealion.serviceassistant.mqtt.IMqttClient interface,
 * generating a proxy if needed.
 */
public static com.sealion.serviceassistant.mqtt.IMqttClient asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.sealion.serviceassistant.mqtt.IMqttClient))) {
return ((com.sealion.serviceassistant.mqtt.IMqttClient)iin);
}
return new com.sealion.serviceassistant.mqtt.IMqttClient.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_sendMessageByMqtt:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
this.sendMessageByMqtt(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.sealion.serviceassistant.mqtt.IMqttClient
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void sendMessageByMqtt(byte[] message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(message);
mRemote.transact(Stub.TRANSACTION_sendMessageByMqtt, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_sendMessageByMqtt = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void sendMessageByMqtt(byte[] message) throws android.os.RemoteException;
}
