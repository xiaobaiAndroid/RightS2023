package com.android.launcher.map;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.android.launcher.MainActivity;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;

import module.common.MessageEvent;
import module.common.utils.DensityUtil;
import module.common.utils.LogUtils;

/**
 * 地图导航页面
 * @date： 2023/11/30
 * @author: 78495
*/
public class MapHomeFragment extends FragmentBase implements AMap.OnMyLocationChangeListener, GeocodeSearch.OnGeocodeSearchListener {

    private MapView mMapView;
    private AMap aMap;
    private TextView searchTV;
    private GeocodeSearch geocoderSearch;
    private Location mLastLocation;
    private Location mCurrentLocation;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        //获取地图控件引用
        mMapView = (MapView) view.findViewById(R.id.mapView);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();

        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.setMinZoomLevel(3);
        aMap.setMaxZoomLevel(20);
        aMap.setTrafficEnabled(false);
        aMap.setTouchPoiEnable(false);
        //设置默认缩放
        aMap.moveCamera(CameraUpdateFactory.zoomBy(5));

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);

        UiSettings uiSettings = aMap.getUiSettings();
        //设置logo不可见
        uiSettings.setLogoBottomMargin(-DensityUtil.dip2px(getContext(),50));

        aMap.setOnMyLocationChangeListener(this);


        searchTV = view.findViewById(R.id.searchTV);
        searchTV.setVisibility(View.GONE);
        searchTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.toMapSearchFragment();
            }
        });

        try {
            geocoderSearch = new GeocodeSearch(getContext());
            geocoderSearch.setOnGeocodeSearchListener(this);
        } catch (AMapException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_map_home;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        try {
            aMap.removeOnMyLocationChangeListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }


    @Override
    public void onMyLocationChange(Location location) {
        try {
            if(mCurrentLocation != null){
                if (mCurrentLocation.getLatitude() == location.getLatitude() && mCurrentLocation.getLongitude() == location.getLongitude()) {
                    return;
                }
            }

            mCurrentLocation = location;
            // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        //返回结果成功或者失败的响应码。1000为成功
        try {
            if(searchTV.getVisibility() != View.VISIBLE){
                searchTV.setVisibility(View.VISIBLE);
            }
            LogUtils.printI(TAG,"onRegeocodeSearched---rCode="+rCode);
            if(rCode == 1000){
                 MainActivity.mRegeocodeAddress = result.getRegeocodeAddress();
                if(MainActivity.mRegeocodeAddress!=null){
                    LogUtils.printI(TAG, "onRegeocodeSearched---regeocodeAddress="+MainActivity.mRegeocodeAddress.getCity());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
