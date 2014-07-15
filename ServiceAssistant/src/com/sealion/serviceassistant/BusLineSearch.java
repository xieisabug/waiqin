package com.sealion.serviceassistant;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.RouteOverlay;
import com.sealion.serviceassestant.app.ServiceManageApplication;
import com.sealion.serviceassistant.entity.LocationEntity;
import com.sealion.serviceassistant.gps.GpsLocation;
import com.sealion.serviceassistant.widget.PopMenu;


/**   
* �ٶȵ�ͼ���ṩ������·���ҵȹ��ܣ���λ�ȹ���.
*/
public class BusLineSearch extends MapActivity
{
	private static final String TAG = BusLineSearch.class.getSimpleName();
	
	private ImageView topbar_dropdown_btn = null;
	private PopMenu popMenu = null;
	private TextView top_bar_title = null;
	private LinearLayout bus_line_layout = null;
	private LinearLayout route_layout = null;
	private ImageView back_img_btn = null;
	private ImageView top_bar_logo = null;
	protected SharedPreferences sp = null;
	private int sign = 0; //0:�ݳ�·��  1: ����·��
	private ImageView sign_in = null;
	
	Button mBtnSearch = null; // ������ť
	Button mBtnTransit = null;	// �ݳ����� // ��������
	
	MapView mMapView = null; // ��ͼView
	MKSearch mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	String mCityName = null;
	LocationListener mLocationListener = null;//createʱע���listener��Destroyʱ��ҪRemove
	private EditText editCity = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buslinesearch);
		
		editCity = (EditText) findViewById(R.id.city);		
		
		sign_in = (ImageView)this.findViewById(R.id.sign_in);
		sign_in.setVisibility(View.GONE);
		
		back_img_btn = (ImageView)this.findViewById(R.id.back_img_btn);
		back_img_btn.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		top_bar_logo = (ImageView)this.findViewById(R.id.top_bar_logo);
		top_bar_logo.setVisibility(View.GONE);
		
		topbar_dropdown_btn = (ImageView)this.findViewById(R.id.topbar_dropdown_btn);
		topbar_dropdown_btn.setBackgroundResource(R.drawable.road_line_btn);
		
		top_bar_title = (TextView)this.findViewById(R.id.top_bar_title);
		bus_line_layout = (LinearLayout)this.findViewById(R.id.bus_line_layout);
		route_layout = (LinearLayout)this.findViewById(R.id.route_layout);
		
		sp = getSharedPreferences("USER_CARD", Context.MODE_PRIVATE);
		editCity.setText(sp.getString("AREAID", ""));
		// ��ʼ�������˵�
		popMenu = new PopMenu(this);
		final String[] typeArray = new String[]{"������·�߲�ѯ", "���ݳ�·�߲�ѯ","������·�߲�ѯ"};
		popMenu.addItems(typeArray);
		
		// �����˵�������
		OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				popMenu.ClearAllSelectItem();
				ImageView iView = (ImageView)view.findViewById(R.id.select_item);
				iView.setBackgroundResource(R.drawable.pop_up_check_box);
				
				if (position == 0) //������·�߲�ѯ
				{
					top_bar_title.setText(typeArray[0]);
					bus_line_layout.setVisibility(View.VISIBLE);
					route_layout.setVisibility(View.GONE);
				}
				else if (position == 1) //���ݳ�·�߲�ѯ
				{
					top_bar_title.setText(typeArray[1]);
					bus_line_layout.setVisibility(View.GONE);
					route_layout.setVisibility(View.VISIBLE);
					sign = 0;
				}
				else if (position == 2)//������·�߲�ѯ
				{
					top_bar_title.setText(typeArray[2]);
					bus_line_layout.setVisibility(View.GONE);
					route_layout.setVisibility(View.VISIBLE);
					sign = 1;
				}
				
				popMenu.dismiss();
			}
		};

		// ��ť������
		OnClickListener onViewClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				popMenu.showAsDropDown(v);			
			}
		};
		
		// �˵�����������
		popMenu.setOnItemClickListener(popmenuItemClickListener);
		topbar_dropdown_btn.setOnClickListener(onViewClick);
		
		ServiceManageApplication app = (ServiceManageApplication) this.getApplication();
		if (app.mBMapMan == null)
		{
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new ServiceManageApplication.MyGeneralListener());
		}
		app.mBMapMan.start();
		// ���ʹ�õ�ͼSDK�����ʼ����ͼActivity 
		super.initMapActivity(app.mBMapMan);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.setBuiltInZoomControls(true);
		// ���������Ŷ���������Ҳ��ʾoverlay,Ĭ��Ϊ������
		mMapView.setDrawOverlayWhenZooming(true);
		
//		GpsLocation gps = new GpsLocation(this);
//		if (!gps.isGPSEnable())
//		{
//			gps.toggleGPS();						
//		}
//
//		LocationEntity lEntity = gps.getLocation();
//
//		MapController mMapController = mMapView.getController();  // �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
//		GeoPoint point = new GeoPoint((int) (lEntity.getLatitude() * 1E6),
//		        (int) (lEntity.getLongitude() * 1E6));  //�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
		
		 // ע�ᶨλ�¼�
        mLocationListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				if(location != null){
					
					MapController mMapController = mMapView.getController();  // �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
					GeoPoint point = new GeoPoint((int)(location.getLatitude() * 1E6),(int) (location.getLongitude() * 1E6));  //�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
					mMapController.setCenter(point);  //���õ�ͼ���ĵ�
					mMapController.setZoom(12);    //���õ�ͼzoom����
				}
			}
        };
        
		
		// ��ʼ������ģ�飬ע���¼�����
		mSearch = new MKSearch();
		mSearch.init(app.mBMapMan, new MKSearchListener()
		{

			public void onGetPoiResult(MKPoiResult res, int type, int error)
			{
				// ����ſɲο�MKEvent�еĶ���
				if (error != 0 || res == null)
				{
					Toast.makeText(BusLineSearch.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
					return;
				}

				// �ҵ�����·��poi node
				MKPoiInfo curPoi = null;
				int totalPoiNum = res.getNumPois();
				for (int idx = 0; idx < totalPoiNum; idx++)
				{
					Log.d("busline", "the busline is " + idx);
					curPoi = res.getPoi(idx);
					if (2 == curPoi.ePoiType)
					{
						break;
					}
				}

				mSearch.busLineSearch(mCityName, curPoi.uid);
			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error)
			{
				// ����ſɲο�MKEvent�еĶ���
				if (error != 0 || res == null) {
					Toast.makeText(BusLineSearch.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
					return;
				}
				RouteOverlay routeOverlay = new RouteOverlay(BusLineSearch.this, mMapView);
			    // �˴���չʾһ��������Ϊʾ��
			    routeOverlay.setData(res.getPlan(0).getRoute(0));
			    mMapView.getOverlays().clear();
			    mMapView.getOverlays().add(routeOverlay);
			    mMapView.invalidate();
			    
			    mMapView.getController().animateTo(res.getStart().pt);
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res, int error)
			{
				
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error)
			{
				if (error != 0 || res == null) {
					Toast.makeText(BusLineSearch.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
					return;
				}
				RouteOverlay routeOverlay = new RouteOverlay(BusLineSearch.this, mMapView);
			    // �˴���չʾһ��������Ϊʾ��
			    routeOverlay.setData(res.getPlan(0).getRoute(0));
			    mMapView.getOverlays().clear();
			    mMapView.getOverlays().add(routeOverlay);
			    mMapView.invalidate();
			    
			    mMapView.getController().animateTo(res.getStart().pt);
			}

			public void onGetAddrResult(MKAddrInfo res, int error)
			{
				if (error != 0) {
					String str = String.format("����ţ�%d", error);
					Toast.makeText(BusLineSearch.this, str, Toast.LENGTH_LONG).show();
					return;
				}

				mMapView.getController().animateTo(res.geoPt);
					
				String strInfo = String.format("γ�ȣ�%f ���ȣ�%f\r\n", res.geoPt.getLatitudeE6()/1e6, 
							res.geoPt.getLongitudeE6()/1e6);

				Toast.makeText(BusLineSearch.this, strInfo, Toast.LENGTH_LONG).show();
				Drawable marker = getResources().getDrawable(R.drawable.iconmarka);  //�õ���Ҫ���ڵ�ͼ�ϵ���Դ
				marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
						.getIntrinsicHeight());   //Ϊmaker����λ�úͱ߽�
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(new OverItemT(marker, BusLineSearch.this, res.geoPt, res.strAddr));
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError)
			{
				if (iError != 0 || result == null)
				{
					Toast.makeText(BusLineSearch.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
					return;
				}

				RouteOverlay routeOverlay = new RouteOverlay(BusLineSearch.this, mMapView);
				// �˴���չʾһ��������Ϊʾ��
				routeOverlay.setData(result.getBusRoute());
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(routeOverlay);
				mMapView.invalidate();

				mMapView.getController().animateTo(result.getBusRoute().getStart());
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1)
			{
				// TODO Auto-generated method stub

			}

		});

		// �趨������ť����Ӧ
		mBtnSearch = (Button) findViewById(R.id.search);
		mBtnTransit = (Button)findViewById(R.id.transit);
		 
		OnClickListener clickListener = new OnClickListener()
		{
			public void onClick(View v)
			{
				SearchButtonProcess(v);
			}
		};
		
		mBtnSearch.setOnClickListener(clickListener);
		mBtnTransit.setOnClickListener(clickListener);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		String city = sp.getString("AREAID", "");
		String customer_address = this.getIntent().getExtras().getString("customer_address");
		if (city != null && !city.equals("") && customer_address != null && !customer_address.equals(""))
		{
			mSearch.geocode(customer_address, city);
		}
	}
	
	void SearchButtonProcess(View v)
	{
		if (mBtnSearch.equals(v))
		{
			EditText editSearchKey = (EditText) findViewById(R.id.searchkey);
			mCityName = editCity.getText().toString();
			String searchKey = editSearchKey.getText().toString();
			if (mCityName == null || mCityName.equals(""))
			{
				Toast.makeText(this, "���������ڳ���", Toast.LENGTH_LONG).show();
				return;
			}
			
			if (searchKey == null || searchKey.equals(""))
			{
				Toast.makeText(this, "������·������", Toast.LENGTH_LONG).show();
				return;
			}
			mSearch.poiSearchInCity(mCityName, editSearchKey.getText().toString());
		}
		else if (mBtnTransit.equals(v))
		{
			// ����������ť��Ӧ
			EditText editSt = (EditText)findViewById(R.id.start);
			EditText editEn = (EditText)findViewById(R.id.end);
			
			// ������յ��name���и�ֵ��Ҳ����ֱ�Ӷ����긳ֵ����ֵ�����򽫸��������������
			MKPlanNode stNode = new MKPlanNode();
			stNode.name = editSt.getText().toString();
			MKPlanNode enNode = new MKPlanNode();
			enNode.name = editEn.getText().toString();
			
			if (sign == 0)
			{
				mSearch.drivingSearch("��ɳ", stNode, "����", enNode);
			}
			else if (sign == 1)
			{
				mSearch.walkingSearch("��ɳ", stNode, "��ɳ", enNode);
			}
			
		}
	}

	@Override
	protected void onPause()
	{
		ServiceManageApplication app = (ServiceManageApplication) this.getApplication();
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		app.mBMapMan.stop();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		ServiceManageApplication app = (ServiceManageApplication) this.getApplication();
		app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
	
	class OverItemT extends ItemizedOverlay<OverlayItem>{
		private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();

		public OverItemT(Drawable marker, Context context, GeoPoint pt, String title) {
			super(boundCenterBottom(marker));
			
			mGeoList.add(new OverlayItem(pt, title, null));

			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mGeoList.get(i);
		}

		@Override
		public int size() {
			return mGeoList.size();
		}

		@Override
		public boolean onSnapToItem(int i, int j, Point point, MapView mapview) {
			Log.e("ItemizedOverlayDemo","enter onSnapToItem()!");
			return false;
		}
	}

}
