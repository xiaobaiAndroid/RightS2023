package com.android.launcher.map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.UiSettings;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;

import org.greenrobot.eventbus.EventBus;

import module.common.MessageEvent;
import module.common.utils.DensityUtil;
import module.common.utils.LogUtils;

import static com.amap.api.navi.AMapNaviView.CAR_UP_MODE;

/**
 * 导航
 * @date： 2023/12/1
 * @author: 78495
*/
public class MapNavFragment extends FragmentBase implements AMapNaviListener, AMapNaviViewListener {


    private AMapNavi mAMapNavi;
    private AMapNaviView aMapNaviView;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        aMapNaviView = (AMapNaviView) view.findViewById(R.id.aMapNaviView);
        initNavParams(savedInstanceState);

        if(mAMapNavi!=null){
            mAMapNavi.startNavi(NaviType.GPS);
        }

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_map_nav;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        aMapNaviView.onDestroy();
        try {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.STOP_MAP_NAV));
            mAMapNavi.stopNavi();
            mAMapNavi.removeAMapNaviListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initNavParams(Bundle savedInstanceState) {
        try {
            mAMapNavi = AMapNavi.getInstance(getContext());
            mAMapNavi.addAMapNaviListener(this);
            mAMapNavi.setEmulatorNaviSpeed(60);
            mAMapNavi.setUseInnerVoice(true,true);
//            mAMapNavi.startSpeak();
        } catch (AMapException e) {
            e.printStackTrace();
        }

        //设置车头朝向
        aMapNaviView.setNaviMode(CAR_UP_MODE);
        AMapNaviViewOptions viewOptions = aMapNaviView.getViewOptions();
//        //设置6秒后是否自动锁车
        viewOptions.setAutoLockCar(true);
//
//        //设置是否开启动态比例尺 (锁车态下自动进行地图缩放变化)
//        viewOptions.setAutoChangeZoom(true);
//        //设置菜单按钮是否在导航界面显示
        viewOptions.setSettingMenuEnabled(false);
//        //设置是否自动全览模式，即在算路成功后自动进入全览模式
//        viewOptions.setAutoDisplayOverview(false);
//        //设置是否自动画路
//        viewOptions.setAutoDrawRoute(true);
//        //设置是否显示道路信息view
        viewOptions.setLaneInfoShow(true);
//        //设置是否显示路口放大图(路口模型图)
        viewOptions.setModeCrossDisplayShow(true);
//
//        //设置锁车态下地图倾角,倾角为0时地图模式是2D模式。
        viewOptions.setTilt(60);
//        //设置导航界面UI是否显示。
//        viewOptions.setLayoutVisible(false);
//        //设置是否显示下下个路口的转向引导，默认不显示
//        viewOptions.setSecondActionVisible(true);
//
//        //设置菜单按钮是否在导航界面显示。
//        viewOptions.setSettingMenuEnabled(false);
//
//        //设置[实时交通图层开关按钮]是否显示（只适用于驾车导航，需要联网）
        viewOptions.setTrafficLayerEnabled(true);
//
//        //设置地图上是否显示交通路况（彩虹线）
        viewOptions.setTrafficLine(true);
//
//        //设置自车位置锁定在屏幕中的位置
        viewOptions.setPointToCenter(0.5, 0.5);

        UiSettings uiSettings = aMapNaviView.getMap().getUiSettings();
        //设置logo不可见
        uiSettings.setLogoBottomMargin(-DensityUtil.dip2px(getContext(),50));
        aMapNaviView.setAMapNaviViewListener(this);
        aMapNaviView.onCreate(savedInstanceState);
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
        //到达目的地后回调函数
        LogUtils.printI(TAG,"onArriveDestination---");

        new Handler(Looper.getMainLooper()).postDelayed(() -> EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.KEY_HOME)),5000);
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

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {
        LogUtils.printI(TAG,"onCalculateRouteFailure---路线规划失败="+aMapCalcRouteResult.getErrorDetail());
    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onGpsSignalWeak(boolean b) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {
        LogUtils.printI(TAG,"onNaviCancel---");
        //导航页面左下角返回按钮点击后弹出的『退出导航』对话框中选择『确定』后的回调接口。
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.BACK_EVENT));
    }

    @Override
    public boolean onNaviBackClick() {
        //导航页面左下角"退出"按钮的点击回调
        LogUtils.printI(TAG,"onNaviBackClick---");
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.BACK_EVENT));
        return true;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }
}
