package com.barrery.parkbuddy.PoiSearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Interpolator;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.barrery.parkbuddy.CarLocationInformation.CarLocationInfoActivity;
import com.barrery.parkbuddy.DepotInfo.MainActivity;
import com.barrery.parkbuddy.R;
import com.barrery.parkbuddy.util.AmapTTSController;
import com.barrery.parkbuddy.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/*
实现poi周边搜索、地图显示、地图定位和导航功能
 */
public class PoiAroundSearchActivity extends AppCompatActivity implements OnClickListener,
		OnMapClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnMarkerClickListener,
		OnPoiSearchListener,INaviInfoCallback{
	/*
	用于显示地图
	 */
	private MapView mapview;
	private AMap mAMap;
	/*
	用于地图定位
	 */
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	/*
	用于导航语音播报
	 */
	AmapTTSController amapTTSController;
	/*
	用于poi周边搜索
	 */
	private PoiResult poiResult;		// poi返回的结果
	private int currentPage = 0;		// 当前页面，从0开始计数
	private PoiSearch.Query query;	// poi查询条件类
	private LatLonPoint lp = new LatLonPoint(39.993743, 116.472995);//poi周边搜索初始化的经纬度

	private Marker locationMarker;	// 选择的点
	private Marker detailMarker;
	private Marker mlastMarker;
	private PoiSearch poiSearch;
	private myPoiOverlay poiOverlay;	// poi图层
	private List<PoiItem> poiItems;	// poi数据

	private RelativeLayout mPoiDetail;
	private TextView mPoiName, mPoiAddress;
	private String keyWord = "";
	private EditText mSearchText;

	/*
	用于判断按钮的状态
	 */
//	private FloatingActionButton fabAll,fabNavi,fabInfo;
	private  int FAB_STATE=1;
	/*
	用于导航栏的侧滑
	 */
	private DrawerLayout mDrawerLayout;

//	39.083749,121.81327

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poiaroundsearch_activity);        //显示

		ActionBar actionbar = getSupportActionBar();
		if (actionbar != null) {                          //隐藏系统自带的标题栏
			actionbar.hide();
		}

		/*
		实现显示地图功能
		 */
		mapview = (MapView) findViewById(R.id.mapView);            //显示地图
		mapview.onCreate(savedInstanceState);                        //重写onCreate(savedInstanceState)
		/*
		实现初始化定位、启动定位功能
		 */
		initLocation();
		startLocation();
		/*
		实现初始化poi周边搜索功能
		 */
		init();
		/*
		实现导航功能
		 */
		amapTTSController = AmapTTSController.getInstance(getApplicationContext());        //初始化导航语音播报
		amapTTSController.init();
		/*
		实现两个按钮选择
		 */
		final FloatingActionButton fabAll = (FloatingActionButton) findViewById(R.id.fab_all);
		final FloatingActionButton fabInfo = (FloatingActionButton) findViewById(R.id.fab_info);
		final FloatingActionButton fabNavi = (FloatingActionButton) findViewById(R.id.fab_navi);
		final FloatingActionButton fabLike = (FloatingActionButton) findViewById(R.id.fab_like);
		fabAll.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				//如果没有打开，则弹出
				if(FAB_STATE == 1) {
					fabAll.setImageResource(R.drawable.delete_3);
					fabNavi.setVisibility(v.VISIBLE);
					fabInfo.setVisibility(v.VISIBLE);
					fabLike.setVisibility(v.VISIBLE);
					FAB_STATE = 0;
				}
				else {
					fabAll.setImageResource(R.drawable.unfold);
					fabNavi.setVisibility(v.GONE);
					fabInfo.setVisibility(v.GONE);
					fabLike.setVisibility(v.GONE);
					FAB_STATE = 1;
				}
			}
		});

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_1);
		fabInfo.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				mDrawerLayout.openDrawer(GravityCompat.START);
			}
		});

		fabNavi.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null),PoiAroundSearchActivity.this);
			}
		});
		fabLike.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(PoiAroundSearchActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});

		/*
		实现查看车位信息详情功能
		 */
		Button depotInfo = (Button) findViewById(R.id.depot_info);
		depotInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				PoiItem mCurrentPoi = (PoiItem) mlastMarker.getObject();
				String Title =mCurrentPoi.getTitle().toString();
				Intent intent = new Intent(PoiAroundSearchActivity.this,CarLocationInfoActivity.class);
				intent.putExtra("mCPoi",Title);
				startActivity(intent);
			}
		});
		/*
		实现一键导航功能
		 */
		Button naviOneButton = (Button) findViewById(R.id.navi_one_button);
		naviOneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mlastMarker == null) {
					AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null), PoiAroundSearchActivity.this);
				} else {
					Poi epoi = new Poi(mlastMarker.getTitle(), mlastMarker.getPosition(), "");

					AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(epoi), PoiAroundSearchActivity.this);

				}
			}
		});
	}

	/*****************************************************地图定位功能*****************************************************/
	/*
	初始化定位
	 */
	private void initLocation(){
		//初始化client
		locationClient = new AMapLocationClient(this.getApplicationContext());
		locationOption = getDefaultOption();
		//设置定位参数
		locationClient.setLocationOption(locationOption);
		// 设置定位监听
		locationClient.setLocationListener(locationListener);


	}

	/*
	 默认的定位参数
	 */
	private AMapLocationClientOption getDefaultOption(){
		AMapLocationClientOption mOption = new AMapLocationClientOption();
		mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
		mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
		mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
		mOption.setInterval(2000000);//可选，设置定位间隔。默认为2秒
		mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
		mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
		mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
		AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
		mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
		mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
		mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
		return mOption;
	}

	/*
	 定位监听
	 */
	AMapLocationListener locationListener = new AMapLocationListener() {
		@Override
		public void onLocationChanged(final AMapLocation location) {
			if (null != location) {
				StringBuffer sb = new StringBuffer();
				//errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
				if(location.getErrorCode() == 0){
					lp.setLongitude(location.getLongitude());
					lp.setLatitude(location.getLatitude());
					mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), 14));

				} else {
					//定位失败
					sb.append("定位失败" + "\n");
					sb.append("错误码:" + location.getErrorCode() + "\n");
					sb.append("错误信息:" + location.getErrorInfo() + "\n");
					sb.append("错误描述:" + location.getLocationDetail() + "\n");
					Toast.makeText(getApplicationContext(),sb.toString(),Toast.LENGTH_SHORT).show();
				}
			} else {
				//Toast.makeText(getApplicationContext(),sb.toString(),Toast.LENGTH_SHORT).show();
			}
		}
	};
	/*
	 开始定位
	 */
	private void startLocation(){
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();
	}

	/*****************************************************poi搜索功能*****************************************************/
	/*
	 初始化AMap对象
	 */
	private void init() {
		if (mAMap == null) {
			mAMap = mapview.getMap();
			mAMap.setOnMapClickListener(this);
			mAMap.setOnMarkerClickListener(this);
			mAMap.setOnInfoWindowClickListener(this);
			mAMap.setInfoWindowAdapter(this);
			TextView searchButton = (TextView) findViewById(R.id.btn_search);
			searchButton.setOnClickListener(this);
			locationMarker = mAMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.point4)))
					.position(new LatLng(lp.getLatitude(), lp.getLongitude())));
			locationMarker.showInfoWindow();

			mAMap = mapview.getMap();
			//设置显示定位按钮 并且可以点击
			UiSettings settings = mAMap.getUiSettings();
			// 是否显示定位按钮
			settings.setMyLocationButtonEnabled(true);
			mAMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase


		}
		setup();
//		mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), 14));



	}

	private void setup() {
		mPoiDetail = (RelativeLayout) findViewById(R.id.poi_detail);
		mPoiDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(PoiSearchActivity.this,
//						SearchDetailActivity.class);
//				intent.putExtra("poiitem", mPoi);
//				startActivity(intent);

			}
		});
		mPoiName = (TextView) findViewById(R.id.poi_name);
		mPoiAddress = (TextView) findViewById(R.id.poi_address);

	}

	/*
	 开始进行poi搜索
	 */
	protected void doSearchQuery() {
		keyWord = "停车场";
		currentPage = 0;
		query = new PoiSearch.Query(keyWord, "停车场", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(20);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页

		if (lp != null) {
			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.setBound(new SearchBound(lp, 5000, true));//
			// 设置搜索区域为以lp点为圆心，其周围5000米范围
			poiSearch.searchPOIAsyn();// 异步搜索
		}
	}

	/*
	 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapview.onResume();
		whetherToShowDetailInfo(false);
	}

	/*
	 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapview.onPause();
	}

	/*
	 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapview.onSaveInstanceState(outState);
	}

	/*
	 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapview.onDestroy();
	}

	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPoiSearched(PoiResult result, int rcode) {
		if (rcode == AMapException.CODE_AMAP_SUCCESS) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
					if (poiItems != null && poiItems.size() > 0) {
						//清除POI信息显示
						whetherToShowDetailInfo(false);
						//并还原点击marker样式
						if (mlastMarker != null) {
							resetlastmarker();
						}
						//清理之前搜索结果的marker
						if (poiOverlay !=null) {
							poiOverlay.removeFromMap();
						}
						mAMap.clear();
						poiOverlay = new myPoiOverlay(mAMap, poiItems);
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();

						mAMap.addMarker(new MarkerOptions()
								.anchor(0.5f, 0.5f)
								.icon(BitmapDescriptorFactory
										.fromBitmap(BitmapFactory.decodeResource(
												getResources(), R.drawable.point4)))
								.position(new LatLng(lp.getLatitude(), lp.getLongitude())));

						mAMap.addCircle(new CircleOptions()
								.center(new LatLng(lp.getLatitude(),
										lp.getLongitude())).radius(5000)
								.strokeColor(Color.BLUE)
								.fillColor(Color.argb(50, 1, 1, 1))
								.strokeWidth(2));

					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						ToastUtil.show(PoiAroundSearchActivity.this,
								R.string.no_result);
					}
				}
			} else {
				ToastUtil
						.show(PoiAroundSearchActivity.this, R.string.no_result);
			}
		} else  {
			ToastUtil.showerror(this.getApplicationContext(), rcode);
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {

		if (marker.getObject() != null) {
			whetherToShowDetailInfo(true);
			try {
				PoiItem mCurrentPoi = (PoiItem) marker.getObject();
				if (mlastMarker == null) {
					mlastMarker = marker;
				} else {
					// 将之前被点击的marker置为原来的状态
					resetlastmarker();
					mlastMarker = marker;
				}
				detailMarker = marker;
				detailMarker.setIcon(BitmapDescriptorFactory
						.fromBitmap(BitmapFactory.decodeResource(
								getResources(),
								R.drawable.poi_marker_pressed)));

				setPoiItemDisplayContent(mCurrentPoi);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else {
			whetherToShowDetailInfo(false);
			resetlastmarker();
		}

		return true;
	}

	// 将之前被点击的marker置为原来的状态
	private void resetlastmarker() {
		int index = poiOverlay.getPoiIndex(mlastMarker);
		if (index < 10) {
			mlastMarker.setIcon(BitmapDescriptorFactory
					.fromBitmap(BitmapFactory.decodeResource(
							getResources(),
							markers[index])));
		}else {
			mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
					BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight)));
		}
		mlastMarker = null;

	}

	private void setPoiItemDisplayContent(final PoiItem mCurrentPoi) {
		mPoiName.setText(mCurrentPoi.getTitle());
		mPoiAddress.setText(mCurrentPoi.getSnippet()+mCurrentPoi.getDistance()+"米");
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_search:
				doSearchQuery();
				break;
			default:
				break;
		}
	}

	private int[] markers = {R.drawable.poi_marker_1,
			R.drawable.poi_marker_2,
			R.drawable.poi_marker_3,
			R.drawable.poi_marker_4,
			R.drawable.poi_marker_5,
			R.drawable.poi_marker_6,
			R.drawable.poi_marker_7,
			R.drawable.poi_marker_8,
			R.drawable.poi_marker_9,
			R.drawable.poi_marker_10
	};

	private void whetherToShowDetailInfo(boolean isToShow) {
		if (isToShow) {
			mPoiDetail.setVisibility(View.VISIBLE);
		} else {
			mPoiDetail.setVisibility(View.GONE);
		}
	}

	@Override
	public void onMapClick(LatLng arg0) {
		whetherToShowDetailInfo(false);
		if (mlastMarker != null) {
			resetlastmarker();
		}
	}

	/*
	 poi没有搜索到数据，返回一些推荐城市的信息
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
		ToastUtil.show(this, infomation);

	}

	/*
	 自定义PoiOverlay
	 */
	private class myPoiOverlay {
		private AMap mamap;
		private List<PoiItem> mPois;
		private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
		public myPoiOverlay(AMap amap ,List<PoiItem> pois) {
			mamap = amap;
			mPois = pois;
		}

		/*
	     添加Marker到地图中。
	     */
		public void addToMap() {
			for (int i = 0; i < mPois.size(); i++) {
				Marker marker = mamap.addMarker(getMarkerOptions(i));
				PoiItem item = mPois.get(i);
				marker.setObject(item);
				mPoiMarks.add(marker);
			}
		}

		/*
         去掉PoiOverlay上所有的Marker。
         */
		public void removeFromMap() {
			for (Marker mark : mPoiMarks) {
				mark.remove();
			}
		}

		/*
         移动镜头到当前的视角。
         */
		public void zoomToSpan() {
			if (mPois != null && mPois.size() > 0) {
				if (mamap == null)
					return;
				LatLngBounds bounds = getLatLngBounds();
				mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
			}
		}

		private LatLngBounds getLatLngBounds() {
			LatLngBounds.Builder b = LatLngBounds.builder();
			for (int i = 0; i < mPois.size(); i++) {
				b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
						mPois.get(i).getLatLonPoint().getLongitude()));
			}
			return b.build();
		}

		private MarkerOptions getMarkerOptions(int index) {
			return new MarkerOptions()
					.position(
							new LatLng(mPois.get(index).getLatLonPoint()
									.getLatitude(), mPois.get(index)
									.getLatLonPoint().getLongitude()))
					.title(getTitle(index)).snippet(getSnippet(index))
					.icon(getBitmapDescriptor(index));
		}

		protected String getTitle(int index) {
			return mPois.get(index).getTitle();
		}

		protected String getSnippet(int index) {
			return mPois.get(index).getSnippet();
		}

		/**
		 * 从marker中得到poi在list的位置。
		 * @param marker 一个标记的对象。
		 * @return 返回该marker对应的poi在list的位置。
		 * @since V2.1.0
		 */
		public int getPoiIndex(Marker marker) {
			for (int i = 0; i < mPoiMarks.size(); i++) {
				if (mPoiMarks.get(i).equals(marker)) {
					return i;
				}
			}
			return -1;
		}

		/**
		 * 返回第index的poi的信息。
		 * @param index 第几个poi。
		 * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
		 * @since V2.1.0
		 */
		public PoiItem getPoiItem(int index) {
			if (index < 0 || index >= mPois.size()) {
				return null;
			}
			return mPois.get(index);
		}

		protected BitmapDescriptor getBitmapDescriptor(int arg0) {
			if (arg0 < 10) {
				BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
						BitmapFactory.decodeResource(getResources(), markers[arg0]));
				return icon;
			}else {
				BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
						BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight));
				return icon;
			}
		}
	}

	/*****************************************************导航功能*****************************************************/
	/*
	实现导航功能
	 */
	@Override
	public void onInitNaviFailure() {

	}

	@Override
	public void onGetNavigationText(String s) {
		amapTTSController.onGetNavigationText(s);
	}

	@Override
	public void onStopSpeaking() {
		amapTTSController.stopSpeaking();
	}

	@Override
	public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

	}

	@Override
	public void onArriveDestination(boolean b) {

	}

	@Override
	public void onStartNavi(int i) {

	}

	@Override
	public void onCalculateRouteSuccess(int[] ints) {

	}

	@Override
	public void onCalculateRouteFailure(int i) {

	}



}
