package com.android.launcher.map;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.ParallelRoadListener;
import com.amap.api.navi.enums.AMapNaviParallelRoadStatus;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItemV2;
import com.android.launcher.MainActivity;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.android.launcher.util.BigDecimalUtils;
import com.android.launcher.view.LinearDividerDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import module.common.MessageEvent;
import module.common.utils.DensityUtil;
import module.common.utils.LogUtils;

import static com.amap.api.navi.AMapNaviView.CAR_UP_MODE;

/**
 * 导航路径选择
 *
 * @date： 2023/11/30
 * @author: 78495
 */
public class MapNavPathSelectFragment extends FragmentBase implements AMapNaviListener {

    private AMapNaviView aMapNaviView;

    private AMapNavi mAMapNavi;

    private NavStrategyAdapter navStrategyAdapter = new NavStrategyAdapter(new ArrayList<>());

    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
    private RecyclerView contentRV;
    private TextView startNavTv;
    private int selectPosition;

    private boolean isRefresh = true;
    private AMap aMap;
    private int strategyConvert;
    private int selectRouteId;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);


        contentRV = (RecyclerView) view.findViewById(R.id.contentRV);
        startNavTv = (TextView) view.findViewById(R.id.startNavTv);
        aMapNaviView = (AMapNaviView) view.findViewById(R.id.aMapNaviView);

        aMap = aMapNaviView.getMap();

        contentRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        contentRV.setAdapter(navStrategyAdapter);

        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.cl_cccccc));
        int padding = DensityUtil.dip2px(getContext(), 8f);
        LinearDividerDecoration<ColorDrawable> decoration = new LinearDividerDecoration<>(navStrategyAdapter, colorDrawable, DensityUtil.dip2px(getContext(), 1f), padding, padding, LinearLayout.HORIZONTAL);
        contentRV.addItemDecoration(decoration);

        initNavParams(savedInstanceState);


        Bundle arguments = getArguments();
        PoiItemV2 poiItemV2 = arguments.getParcelable("poiItemV2");
        LatLonPoint latLonPoint = poiItemV2.getLatLonPoint();

        LogUtils.printI(TAG, "poiItemV2=" + poiItemV2.getTitle() + ", " + poiItemV2.getProvinceName() + poiItemV2.getCityName() + ", 经度：" + poiItemV2.getLatLonPoint().getLongitude() + ", 纬度：" + poiItemV2.getLatLonPoint().getLatitude());

        //进行算路策略转换，将传入的特定规则转换成PathPlanningStrategy的枚举值。 注意：该接口仅驾车模式有效
        //参数1：是否躲避拥堵
        //参数2：是否不走高速
        //参数3：是否避免收费
        //参数4：是否高速优先
        //参数5：单路径or多路径
        strategyConvert = mAMapNavi.strategyConvert(true, false, false, true, true);

        NaviLatLng naviLatLng = new NaviLatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
        eList.add(naviLatLng);
        mAMapNavi.calculateDriveRoute(eList, null, strategyConvert);

        startNavTv.setVisibility(View.INVISIBLE);
        startNavTv.setOnClickListener(v -> {
            sendDataToLeft(latLonPoint);
            MainActivity activity = (MainActivity) getActivity();
            activity.toMapNavFragment();
        });

        navStrategyAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if (selectPosition == position) {
                return;
            }

            StrategyItem strategyItem = navStrategyAdapter.getItem(position);
            strategyItem.setSelected(true);
            navStrategyAdapter.notifyItemChanged(position);

            StrategyItem lastSelectedItem = navStrategyAdapter.getItem(selectPosition);
            lastSelectedItem.setSelected(false);
            navStrategyAdapter.notifyItemChanged(selectPosition);

            selectPosition = position;

            selectRouteId = strategyItem.getRouteId();
            //必须告诉AMapNavi 你最后选择的哪条路
            mAMapNavi.selectRouteId(selectRouteId);
        });
    }

    private void sendDataToLeft(LatLonPoint latLonPoint) {
        mExecutor.execute(() -> {
            try {
                double latitude = latLonPoint.getLatitude();
                double longitude = latLonPoint.getLongitude();
                String latitudeStr = String.valueOf(latitude).replace(".", "D");
                String longitudeStr = String.valueOf(longitude).replace(".", "D");

                final String splitTag = "A";
                String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.MAP_NAV.getTypeValue() + latitudeStr + splitTag + longitudeStr + splitTag + strategyConvert + splitTag + selectRouteId + SerialPortDataFlag.END_FLAG;
                LogUtils.printI(TAG, "send=" + send + ", length=" + send.length());
                SendHelperLeft.handler(send);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void initNavParams(Bundle savedInstanceState) {
        try {
            mAMapNavi = AMapNavi.getInstance(getContext());
            mAMapNavi.addAMapNaviListener(this);
            //开启多路线模式
            mAMapNavi.setMultipleRouteNaviMode(true);

        } catch (AMapException e) {
            e.printStackTrace();
        }

        //设置车头朝向
        aMapNaviView.setNaviMode(CAR_UP_MODE);
        AMapNaviViewOptions viewOptions = aMapNaviView.getViewOptions();
        //设置6秒后是否自动锁车
        viewOptions.setAutoLockCar(false);

        //设置是否开启动态比例尺 (锁车态下自动进行地图缩放变化)
        viewOptions.setAutoChangeZoom(false);
        viewOptions.setLeaderLineEnabled(-1);
        //设置菜单按钮是否在导航界面显示
        viewOptions.setSettingMenuEnabled(false);
        //设置是否自动全览模式，即在算路成功后自动进入全览模式
        viewOptions.setAutoDisplayOverview(true);
        //设置是否自动画路
        viewOptions.setAutoDrawRoute(true);
        //设置是否显示道路信息view
        viewOptions.setLaneInfoShow(false);
        //设置是否显示路口放大图(路口模型图)
        viewOptions.setModeCrossDisplayShow(false);

        //设置锁车态下地图倾角,倾角为0时地图模式是2D模式。
        viewOptions.setTilt(30);
        //设置导航界面UI是否显示。
        viewOptions.setLayoutVisible(false);
        //设置是否显示下下个路口的转向引导，默认不显示
        viewOptions.setSecondActionVisible(false);

        //设置菜单按钮是否在导航界面显示。
        viewOptions.setSettingMenuEnabled(false);

        //设置[实时交通图层开关按钮]是否显示（只适用于驾车导航，需要联网）
        viewOptions.setTrafficLayerEnabled(false);

        //设置地图上是否显示交通路况（彩虹线）
        viewOptions.setTrafficLine(true);

        UiSettings uiSettings = aMap.getUiSettings();
        //设置logo不可见
        uiSettings.setLogoBottomMargin(-DensityUtil.dip2px(getContext(), 50));

        //设置自车位置锁定在屏幕中的位置
        viewOptions.setPointToCenter(0.5, 0.5);
        aMapNaviView.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_map_nav_path_select;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        if (mAMapNavi != null) {

            mAMapNavi.removeAMapNaviListener(this);
            aMapNaviView.onDestroy();
        }


    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        // 获取路线数据对象
        try {
            HashMap<Integer, AMapNaviPath> naviPaths = mAMapNavi.getNaviPaths();
            Set<Map.Entry<Integer, AMapNaviPath>> entries = naviPaths.entrySet();
            Iterator<Map.Entry<Integer, AMapNaviPath>> iterator = entries.iterator();

            if (isRefresh) {
                isRefresh = false;
                ArrayList<StrategyItem> strategyItems = new ArrayList<>();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, AMapNaviPath> pathEntry = iterator.next();
                    Integer routeId = pathEntry.getKey();
                    AMapNaviPath mapNaviPath = pathEntry.getValue();

                    StrategyItem strategyItem = new StrategyItem();

                    float distance = BigDecimalUtils.div(mapNaviPath.getAllLength() + "", "1000", 1);
                    strategyItem.setName(mapNaviPath.getLabels());
                    strategyItem.setDistance(distance);
                    strategyItem.setPathId(mapNaviPath.getPathid());
                    strategyItem.setTime(mapNaviPath.getAllTime());
                    strategyItem.setTrafficLightCount(mapNaviPath.getTrafficLightCount());

                    LogUtils.printI(TAG, "onCalculateRouteSuccess---routeId=" + routeId + ", 总长度=" + mapNaviPath.getAllLength() + ", 所需时间=" + mapNaviPath.getAllTime());

                    aMap.moveCamera(CameraUpdateFactory.changeTilt(0));
                    RouteOverLay routeOverLay = new RouteOverLay(aMap, mapNaviPath, getContext());
                    routeOverLay.setTrafficLine(false);
                    routeOverLay.addToMap();
                    strategyItem.setRouteId(routeId);
                    strategyItem.setRouteOverLay(routeOverLay);

                    strategyItems.add(strategyItem);
                }
                if (!strategyItems.isEmpty()) {
                    strategyItems.get(selectPosition).setSelected(true);
                    navStrategyAdapter.setNewData(strategyItems);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        if (startNavTv.getVisibility() != View.VISIBLE) {
            startNavTv.setVisibility(View.VISIBLE);
        }

//使用pathid进行路线切换
//        mAMapNavi.selectMainPathID(iterator.next().getValue().getPathid());
    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onGpsSignalWeak(boolean b) {

    }

}
