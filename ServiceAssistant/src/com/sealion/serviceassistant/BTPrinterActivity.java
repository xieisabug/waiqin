package com.sealion.serviceassistant;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.sealion.serviceassistant.db.CommonOrderComplishDB;
import com.sealion.serviceassistant.db.OrderDetailListDB;
import com.sealion.serviceassistant.db.OrderListDB;
import com.sealion.serviceassistant.db.QuestionListDB;
import com.sealion.serviceassistant.entity.CommonOrderComplishEntity;
import com.sealion.serviceassistant.entity.QuestionEntity;
import com.sealion.serviceassistant.print.BluetoothService;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**   
* ���������ӡ���ܣ��ṩGUI���û�����.
*/
public class BTPrinterActivity extends Activity
{
		// Debugging
		private static final String TAG = "BTPrinter";
		private static final boolean D = true;

		// Message types sent from the BluetoothService Handler
		public static final int MESSAGE_STATE_CHANGE = 1;
		public static final int MESSAGE_READ = 2;
		public static final int MESSAGE_WRITE = 3;
		public static final int MESSAGE_DEVICE_NAME = 4;
		public static final int MESSAGE_TOAST = 5;

		// Key names received from the BluetoothService Handler
		public static final String DEVICE_NAME = "device_name";
		public static final String TOAST = "toast";

		// Intent request codes
		private static final int REQUEST_CONNECT_DEVICE = 1;
		private static final int REQUEST_ENABLE_BT = 2;

		// Layout Views
		private TextView mTitle;
		private TextView mOutTextView;
		//private Button mSendButton;
		private Button mPrintButton;

		// Name of the connected device
		private String mConnectedDeviceName = null;
		// Local Bluetooth adapter
		private BluetoothAdapter mBluetoothAdapter = null;
		// Member object for the services
		private BluetoothService mService = null;
		
		private ImageView back_image = null;
		private TextView top_bar_title = null;
		
		private String order_num;
		
		StringBuilder sBuilder = null;
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			if (D)
				Log.e(TAG, "+++ ON CREATE +++");

			// Set up the window layout
			//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.btprint);
			//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
			order_num = this.getIntent().getExtras().getString("order_num");
			
			// Set up the custom title
			mTitle = (TextView) findViewById(R.id.title_left_text);
			mTitle.setText(R.string.Print);
			mTitle = (TextView) findViewById(R.id.title_right_text);

			// Get local Bluetooth adapter
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			// If the adapter is null, then Bluetooth is not supported
			if (mBluetoothAdapter == null)
			{
				Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
				finish();
			}
			
			top_bar_title = (TextView)this.findViewById(R.id.top_bar_title);
			top_bar_title.setText(this.getResources().getString(R.string.conplete_panel_btn5));
			back_image = (ImageView)this.findViewById(R.id.back_image);
			back_image.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					finish();
				}
			});
			
			//��ʼ����ӡ��Ϣ----------------------------------------
			
			OrderDetailListDB odB = new OrderDetailListDB(this);
			String arriveDate = odB.getArriveTargetTimeByOrderNum(order_num,FinalVariables.ORDER_STATE_ARRIVE_TARGET);
			String complishDate = odB.getArriveTargetTimeByOrderNum(order_num,FinalVariables.ORDER_STATE_ORDER_COMPLISH);
						
			CommonOrderComplishDB cocDB = new CommonOrderComplishDB(this);
			CommonOrderComplishEntity ccEntity = cocDB.SelectById(order_num);
			
			QuestionListDB questionDB = new QuestionListDB(this);
			ArrayList<QuestionEntity> qList = questionDB.getQuestionByOrderNum(order_num);
			StringBuilder qBuilder = new StringBuilder();
			for (QuestionEntity qEntity : qList)
			{
				qBuilder.append(qEntity.getProductName()+",");
			}
			
			boolean service_or_visit = true;
			
			sBuilder = new StringBuilder();

			if ((ccEntity.getVisit_dispose_result() == null || ccEntity.getVisit_dispose_result().equals(""))
					||(ccEntity.getVisit_product_case() == null || ccEntity.getVisit_product_case().equals(""))
					||(ccEntity.getVisit_type() == null || ccEntity.getVisit_type().equals("")))
			{
				service_or_visit = true;
				sBuilder.append("                    ������Ϣ��Ʒά����\n");
			}
			else
			{
				service_or_visit = false;
				sBuilder.append("                    ������Ϣ��Ʒ�طõ�\n");
			}
//			sBuilder.append("�ɹ���ID:"+ccEntity.getOrder_num());
//			sBuilder.append("\n");
			sBuilder.append("ֽ�ʵ��ݺ�:"+ccEntity.getNumber_value());
			sBuilder.append("\n");
			sBuilder.append("�˿�����:"+ccEntity.getCustomer_name());
			sBuilder.append("\n");
			sBuilder.append("˰��:"+ccEntity.getCustomer_tax());
			sBuilder.append("\n");
//			sBuilder.append("�ɹ�˵��:"+ccEntity.getCustomer_remark());
//			sBuilder.append("\n");
			sBuilder.append("���ŵ�ַ:"+ccEntity.getService_address());
			sBuilder.append("\n");
			sBuilder.append("��ϵ��:"+ccEntity.getContact_name());
			sBuilder.append("\n");
			sBuilder.append("�̶��绰:"+FliterString(ccEntity.getCustomer_tel()));
			sBuilder.append("\n");
			sBuilder.append("�ƶ��ֻ�:"+FliterString(ccEntity.getCustomer_mobile()));
			sBuilder.append("\n");
//			sBuilder.append("�����Ʒ:"+);
//			sBuilder.append("\n");
			if (service_or_visit)
			{
				sBuilder.append("Ӳ���ͺż����:"+ccEntity.getSoftware_type());
				sBuilder.append("\n");
				sBuilder.append("����汾��:"+ccEntity.getSoftware_version());
				sBuilder.append("\n");
				sBuilder.append("ʹ�û���:"+ccEntity.getSoftware_env_value()+"\n");
				sBuilder.append("����ʽ:���ŷ���\n");
				sBuilder.append("�����Ʒ:"+qBuilder.toString() == null?"":qBuilder.toString() +"\n");
				sBuilder.append("��������:\n");
				QuestionListDB qlDB = new QuestionListDB(this);
				ArrayList<QuestionEntity> qlList = qlDB.getQuestionByOrderNum(order_num);
				for (QuestionEntity qEntity : qlList)
				{
					sBuilder.append(qEntity.getQ_content());
				}
				sBuilder.append("\n");
				sBuilder.append("����취:\n");
				for (QuestionEntity qEntity : qlList)
				{
					sBuilder.append(qEntity.getQ_answer());				
				}
				sBuilder.append("\n");
				
				
				if(ccEntity.getIs_charge() == 0)
				{
					sBuilder.append("�Ƿ��շ�:��\n");
				}
				else
				{
					sBuilder.append("�Ƿ��շ�:��\n");
					sBuilder.append("�շѽ��:"+ccEntity.getIs_charge_value()+"\n");
				}
				sBuilder.append("����ʱ��:"+arriveDate+"\n");
				if (complishDate == null || complishDate.equals(""))
				{
					sBuilder.append("���ʱ��:"+complishDate+"\n");
				}
				else
				{
					sBuilder.append("���ʱ��:"+DateTools.getFormatDateAndTime()+"\n");
				}
				
				sBuilder.append("����λ����:���Ϻ�����Ϣ���޹�˾\n");
				sBuilder.append("��ϵ�绰:400-8877-660\n");
				sBuilder.append("��������ʦǩ��:"+ccEntity.getTec_name());
				sBuilder.append("\n");
				sBuilder.append("�������񹤳�ʦ��ά��������ʼǰ�����������Ը������ݽ��б���");
				sBuilder.append("\n");
				sBuilder.append("�ͻ�ǩ��:"+ccEntity.getCustomer_sign());
				sBuilder.append("\n");
				sBuilder.append("��ע:"+ccEntity.getCustomer_remark());
				sBuilder.append("\n");
			}
			else
			{				
				sBuilder.append("��������ʦǩ��:"+ccEntity.getTec_name());
				sBuilder.append("\n");
				sBuilder.append("�ͻ�ǩ��:"+ccEntity.getCustomer_sign());
				sBuilder.append("\n");
				sBuilder.append("��ע:"+ccEntity.getCustomer_remark());
				sBuilder.append("\n");
//				sBuilder.append("�ط�����:"+ccEntity.getVisit_type());
//				sBuilder.append("\n");
				OrderListDB orderDB = new OrderListDB(this);
				String productName = orderDB.SelectProductNameByOrderID(order_num);
				sBuilder.append("�����Ʒ:"+FliterString(productName)+"\n");				
				sBuilder.append("��Ʒ�������:"+ccEntity.getVisit_product_case());
				sBuilder.append("\n");
				sBuilder.append("������:"+ccEntity.getVisit_dispose_result());
				sBuilder.append("\n");
				sBuilder.append("�����ҹ�˾�ķ��������������:");
				if (ccEntity.getService_evaluate() == 0)
				{
					sBuilder.append("������");
				}
				else if(ccEntity.getService_evaluate() == 1)
				{
					sBuilder.append("����");
				}
				else if(ccEntity.getService_evaluate() == 2)
				{
					sBuilder.append("������");
				}
				sBuilder.append("\n");
				sBuilder.append("�����ҹ�˾��Ʒ������������:");
				if (ccEntity.getProduct_evaluate() == 0)
				{
					sBuilder.append("������");
				}
				else if(ccEntity.getProduct_evaluate() == 1)
				{
					sBuilder.append("����");
				}
				else if(ccEntity.getProduct_evaluate() == 2)
				{
					sBuilder.append("������");
				}
				sBuilder.append("\n");
				
				sBuilder.append("�ط�����:"+arriveDate.substring(0, 10));
				sBuilder.append("\n");
				sBuilder.append("��ʼʱ��:"+arriveDate.substring(11, arriveDate.length()));
				sBuilder.append("\n");
				sBuilder.append("����ʱ��:"+complishDate.substring(11, complishDate.length()));
				sBuilder.append("\n");
				sBuilder.append("����λ����:���Ϻ�����Ϣ���޹�˾\n");
				sBuilder.append("��ϵ�绰:400-8877-660\n");
			}
			
			sBuilder.append("������Ϣȫ������ල�绰:400-810-6116\n");
			
		}

		@Override
		public void onStart()
		{
			super.onStart();
			if (D)
				Log.e(TAG, "++ ON START ++");

			// If BT is not on, request that it be enabled.
			// setupChat() will then be called during onActivityResult
			if (!mBluetoothAdapter.isEnabled())
			{
				Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
				// Otherwise, setup the session
			}
			else
			{
				if (mService == null)
					init();
			}
		}

		@Override
		public synchronized void onResume()
		{
			super.onResume();
			if (D)
				Log.e(TAG, "+ ON RESUME +");

			// Performing this check in onResume() covers the case in which BT was
			// not enabled during onStart(), so we were paused to enable it...
			// onResume() will be called when ACTION_REQUEST_ENABLE activity
			// returns.
			if (mService != null)
			{
				// Only if the state is STATE_NONE, do we know that we haven't
				// started already
				if (mService.getState() == BluetoothService.STATE_NONE)
				{
					// Start the Bluetooth services
					Log.d(TAG, "start service....");
					mService.start();
				}
			}
		}

		private void init()
		{
			Log.d(TAG, "setupChat()");

			// Initialize the compose field with a listener for the return key
			mOutTextView = (TextView) findViewById(R.id.edit_text_out);
			if (sBuilder != null)
			{
				mOutTextView.setText(sBuilder.toString());
			}
			
			// Initialize the send button with a listener that for click events
//			mSendButton = (Button) findViewById(R.id.button_send);
//			mSendButton.setOnClickListener(new View.OnClickListener()
//			{
//				@Override
//				public void onClick(View v)
//				{
//					// Send a message using content of the edit text widget
//					// TextView fontGrayView = (TextView)
//					// findViewById(R.id.fontGrayscaleValue);
//					// String message = mOutEditText.getText().toString();
//					String message = "���Ϻ�����Ϣ�������޹�˾��ӡ������Ϣ\r\n";
//					sendMessage(message);
//				}
//			});
			// Initialize the print button with a listener that for click events
			mPrintButton = (Button) findViewById(R.id.button_print);
			mPrintButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// TextView fontGrayView = (TextView)
					// findViewById(R.id.fontGrayscaleValue);
					String message = mOutTextView.getText().toString();
					sendMessage(message);
					
				}
			});
			
			// Initialize the BluetoothService to perform bluetooth connections
			mService = new BluetoothService(this, mHandler);
		}

		@Override
		public synchronized void onPause()
		{
			super.onPause();
			if (D)
				Log.e(TAG, "- ON PAUSE -");
		}

		@Override
		public void onStop()
		{
			super.onStop();
			if (D)
				Log.e(TAG, "-- ON STOP --");
		}

		@Override
		public void onDestroy()
		{
			super.onDestroy();
			// Stop the Bluetooth services
			if (mService != null)
				mService.stop();
			if (D)
				Log.e(TAG, "--- ON DESTROY ---");
		}

		
		/**
		 * Sends a message.
		 * 
		 * @param message
		 *            A string of text to send.
		 * 
		 */
		private void sendMessage(String message)
		{
			// Check that we're actually connected before trying anything
			if (mService.getState() != BluetoothService.STATE_CONNECTED)
			{
				Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
				return;
			}

			// Check that there's actually something to send
			if (message.length() > 0)
			{
				// Get the message bytes and tell the BluetoothService to write
				byte[] send;
				try
				{
					send = message.getBytes("GBK");
				}
				catch (UnsupportedEncodingException e)
				{
					send = message.getBytes();
				}

				mService.write(send);
				//
				// // Reset out string buffer to zero and clear the edit text field
				// mOutStringBuffer.setLength(0);
				// mOutEditText.setText(mOutStringBuffer);
			}
		}


		// The Handler that gets information back from the BluetoothService
		private final Handler mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case MESSAGE_STATE_CHANGE:
					if (D)
						Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
					switch (msg.arg1)
					{
					case BluetoothService.STATE_CONNECTED:
						mTitle.setText(R.string.title_connected_to);
						mTitle.append(mConnectedDeviceName);
						break;
					case BluetoothService.STATE_CONNECTING:
						mTitle.setText(R.string.title_connecting);
						break;
					case BluetoothService.STATE_LISTEN:
					case BluetoothService.STATE_NONE:
						mTitle.setText(R.string.title_not_connected);
						break;
					}
					break;
				case MESSAGE_WRITE:
					// byte[] writeBuf = (byte[]) msg.obj;
					// construct a string from the buffer
					// String writeMessage = new String(writeBuf);
					break;
				case MESSAGE_READ:
					// byte[] readBuf = (byte[]) msg.obj;
					// construct a string from the valid bytes in the buffer
					// String readMessage = new String(readBuf, 0, msg.arg1);
					break;
				case MESSAGE_DEVICE_NAME:
					// save the connected device's name
					mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
					Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
					break;
				case MESSAGE_TOAST:
					Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data)
		{
			if (D)
				Log.d(TAG, "onActivityResult " + resultCode);
			switch (requestCode)
			{
			case REQUEST_CONNECT_DEVICE:
				// When DeviceListActivity returns with a device to connect
				if (resultCode == Activity.RESULT_OK)
				{
					// Get the device MAC address
					String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
					// Get the BLuetoothDevice object
					if (BluetoothAdapter.checkBluetoothAddress(address))
					{
						BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
						// Attempt to connect to the device
						mService.connect(device);
					}
				}
				break;
			case REQUEST_ENABLE_BT:
				// When the request to enable Bluetooth returns
				if (resultCode == Activity.RESULT_OK)
				{
					// Bluetooth is now enabled, so set up a session
					init();
				}
				else
				{
					// User did not enable Bluetooth or an error occured
					Log.d(TAG, "BT not enabled");
					Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu)
		{
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.option_menu, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			switch (item.getItemId())
			{
			case R.id.scan:
				// Launch the DeviceListActivity to see devices and do scan
				Intent serverIntent = new Intent(this, DeviceListActivity.class);
				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
				return true;
			case R.id.disconnect:
				// disconnect
				mService.stop();
				return true;
			}
			return false;
		}
		
		public String FliterString(String str)
		{
			if (str == null || str.trim().equals("") || str.trim().equals("null"))
			{
				return "";
			}
			else
			{
				return str;
			}
		}
}
