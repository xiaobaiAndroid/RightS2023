package com.android.launcher;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.amap.api.navi.AMapNavi;
import com.amap.api.services.core.PoiItemV2;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.android.launcher.apps.AppsListFragment;
import com.android.launcher.base.Activity2Base;
import com.android.launcher.bluetooth.BluetoothDeviceHelper;
import com.android.launcher.contacts.CarContractsFragment;
import com.android.launcher.floating.KeyBackFloating;
import com.android.launcher.main.HomeFragment;
import com.android.launcher.map.MapHomeFragment;
import com.android.launcher.map.MapNavFragment;
import com.android.launcher.map.MapNavPathSelectFragment;
import com.android.launcher.map.MapSearchFragment;
import com.android.launcher.music.MusicHomeFragment;
import com.android.launcher.music.usb.MusicPlayService;
import com.android.launcher.service.LivingService;
import com.android.launcher.setting.SettingHomeFragment;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.type.ThirdAppType;
import com.android.launcher.type.UnitType;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.android.launcher.util.SystemUtils;
import com.android.launcher.util.ValueAnimatorUtil;
import com.android.launcher.wifi.WifiUtil;
import com.bzf.module_db.entity.CarSetupTable;
import com.bzf.module_db.repository.CarSetupRepository;

import module.common.MessageEvent;
import module.common.utils.AppUtils;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;

/**
 * @date： 2023/11/14
 * @author: 78495
*/
public class MainActivity extends Activity2Base {



    private SettingHomeFragment settingHomeFragment;
    private HomeFragment homeFragment;
    private CarContractsFragment carContractsFragment;
    private MusicHomeFragment musicHomeFragment;
    private AppsListFragment appsListFragment;

    public static volatile String currentFragment = HomeFragment.class.getSimpleName();

    public static ThirdAppType mCurrentThirdAppType = ThirdAppType.NONE;

    public static volatile int unitType = UnitType.getDefaultType();


    private KeyBackFloating keyBackFloating;
    public static volatile WifiInfo connectedWifiInfo;

    private boolean hideAnimationEnd = true;
    private boolean showAnimationEnd = true;
    private MapHomeFragment mapHomeFragment;
    private Fragment mapSearchFragment;
    private MapNavPathSelectFragment mapNavPathSelectFragment;
    private MapNavFragment mapNavFragment;

    //是否正在导航
    public static volatile boolean isNaving = false;

    //定位地址信息
    public static volatile RegeocodeAddress mRegeocodeAddress;

    @Override
    protected void setupData() {
        mExecutor.execute(() -> unitType = SPUtils.getInt(getApplicationContext(), SPUtils.SP_UNIT_TYPE, UnitType.getDefaultType()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApp.livingServerStop) {
            LivingService.startLivingService(this);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setBrightness();
        isNaving = false;

        LivingService.connectBlueDevice = BluetoothDeviceHelper.getConnectDevice();

        ValueAnimatorUtil.resetDurationScaleIfDisable();

        MusicPlayService.startMusicService(this);

        connectedWifiInfo = WifiUtil.getConnectedWifiInfo(this);

        if(keyBackFloating == null){
            keyBackFloating = new KeyBackFloating(this);
        }


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        LogUtils.printI(MainActivity.class.getSimpleName(), "initView---homeFragment="+homeFragment);
        if(homeFragment == null){
            homeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.fragmentContainerView,homeFragment);
        }
        fragmentTransaction.show(homeFragment).commit();

        final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = fragmentContainerView.getWidth();
                int height = fragmentContainerView.getHeight();
                if(width > 0){
                    LogUtils.printI(MainActivity.class.getSimpleName(), "removeOnGlobalLayoutListener--width="+width+", height="+height);
                    fragmentContainerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    windDirectionHomeView.setVisibility(View.GONE);
                }
            }
        };
        windDirectionHomeView.setVisibility(View.INVISIBLE);

        fragmentContainerView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);

    }


    private void setBrightness() {
        mExecutor.execute(() -> {
            CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getApplicationContext(), AppUtils.getDeviceId(getApplicationContext()));
            if (carSetupTable != null) {
                final int centralDisplayLum = carSetupTable.getCentralDisplayLum();
                runOnUiThread(() -> {
                    LogUtils.printI(MainActivity.class.getSimpleName(), "centralDisplayLum=" + centralDisplayLum);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SystemUtils.setBrightness(getApplicationContext(), centralDisplayLum);
                        }
                    }, 6000);
                });
            }
        });
    }

    public void toSettingFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if(settingHomeFragment == null){
            settingHomeFragment = new SettingHomeFragment();
            fragmentTransaction.add(R.id.fragmentContainerView,settingHomeFragment);
        }
        fragmentTransaction.show(settingHomeFragment).hide(homeFragment).commit();
        currentFragment = settingHomeFragment.getClass().getSimpleName();
    }

    @Override
    public void disposeMessageEvent(MessageEvent event) {
        super.disposeMessageEvent(event);
        if(event.type == MessageEvent.Type.BACK_EVENT){
            if(windDirectionHomeView!=null){
                if(windDirectionHomeView.getVisibility() == View.VISIBLE){
                    hideWindDirectionView();
                    return;
                }
            }

            LogUtils.printI(MainActivity.class.getSimpleName(), "返回---currentFragment="+currentFragment);
            if(SettingHomeFragment.class.getSimpleName().equals(currentFragment)){
               showHomeFragment();
            }else if(CarContractsFragment.class.getSimpleName().equals(currentFragment)){
                showHomeFragment();
            }else if(MusicHomeFragment.class.getSimpleName().equals(currentFragment)){
                showHomeFragment();
            }else if(AppsListFragment.class.getSimpleName().equals(currentFragment)){
                showHomeFragment();
            }else if(MapHomeFragment.class.getSimpleName().equals(currentFragment)){
                showHomeFragment();
            }else if(MapSearchFragment.class.getSimpleName().equals(currentFragment)){
                mapSearchToMapHome();
            }else if(MapNavPathSelectFragment.class.getSimpleName().equals(currentFragment)){
                mapNavPathSelectToMapSearch();
            }else if(MapNavFragment.class.getSimpleName().equals(currentFragment)){
                mapNavToMapNavPathSelect();
            }
        }
//        else if(event.type == MessageEvent.Type.SHOW_STATUS_BAR_VIEW){
//            if(keyBackFloating!=null && !keyBackFloating.isShow()){
//                keyBackFloating.show();
//            }
//        }
        else if(event.type == MessageEvent.Type.KEY_HOME){
            if(windDirectionHomeView!=null){
                if(windDirectionHomeView.getVisibility() == View.VISIBLE){
                    hideWindDirectionView();
                }
            }
            showHomeFragment();
        }else if (event.type == MessageEvent.Type.SHOW_FRAGMENT_AIRFLOW) {
            if(windDirectionHomeView!=null){
                if(windDirectionHomeView.getVisibility() == View.VISIBLE){
                    hideWindDirectionView();
                }else{
                    showWindDirectionView();
                }
            }
        }
        else if (event.type == MessageEvent.Type.TO_MAIN) {
//            try {
//                FragmentManager supportFragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
//                if(SettingHomeFragment.class.getSimpleName().equals(currentFragment)) {
//                    if (homeFragment != null) {
//                        fragmentTransaction.remove(homeFragment);
//                        homeFragment = null;
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            ActivityRouter.toNavigation(getApplicationContext());
        }else if(event.type == MessageEvent.Type.STOP_MAP_NAV){
            mExecutor.execute(() -> {
                try {
                    String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.STOP_MAP_NAV.getTypeValue() + SerialPortDataFlag.END_FLAG;
                    LogUtils.printI(MainActivity.class.getSimpleName(), "STOP_MAP_NAV---send=" + send +", length="+send.length());
                    SendHelperLeft.handler(send);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void mapNavToMapNavPathSelect() {
        try {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            if(mapNavFragment!=null){
                fragmentTransaction.remove(mapNavFragment);
                mapNavFragment = null;
            }
            fragmentTransaction.show(mapNavPathSelectFragment).commit();
            currentFragment = mapNavPathSelectFragment.getClass().getSimpleName();

            isNaving = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mapNavPathSelectToMapSearch() {
        try {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            if(mapNavPathSelectFragment!=null){
                fragmentTransaction.remove(mapNavPathSelectFragment);
                mapNavPathSelectFragment = null;
            }
            fragmentTransaction.show(mapSearchFragment).commit();
            currentFragment = mapSearchFragment.getClass().getSimpleName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mapSearchToMapHome() {
        try {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            if(mapSearchFragment!=null){
                fragmentTransaction.remove(mapSearchFragment);
                mapSearchFragment = null;
            }
            fragmentTransaction.show(mapHomeFragment).commit();
            currentFragment = mapHomeFragment.getClass().getSimpleName();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showHomeFragment() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);


        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        if(settingHomeFragment!=null){
            fragmentTransaction.remove(settingHomeFragment);
            settingHomeFragment = null;
        }
        if(carContractsFragment!=null){
            fragmentTransaction.remove(carContractsFragment);
            carContractsFragment = null;
        }
        if(appsListFragment!=null){
            fragmentTransaction.remove(appsListFragment);
            appsListFragment = null;
        }
        if(musicHomeFragment!=null){
            fragmentTransaction.remove(musicHomeFragment);
            musicHomeFragment = null;
        }
        if(mapHomeFragment!=null){
            fragmentTransaction.hide(mapHomeFragment);
            mapHomeFragment = null;
        }
        if(mapSearchFragment!=null){
            fragmentTransaction.hide(mapSearchFragment);
        }
        if(mapNavPathSelectFragment!=null){
            fragmentTransaction.hide(mapNavPathSelectFragment);
        }
        if(mapNavFragment!=null){
            fragmentTransaction.hide(mapNavFragment);
        }

        fragmentTransaction.show(homeFragment).commit();
        currentFragment = HomeFragment.class.getSimpleName();
    }

    private void hideWindDirectionView() {
        if(!hideAnimationEnd){
            return;
        }

        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f,  0,-fragmentContainerView.getHeight());
        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        translateAnimation.setInterpolator(new LinearOutSlowInInterpolator());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                hideAnimationEnd = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                windDirectionHomeView.setVisibility(View.GONE);
                hideAnimationEnd = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        windDirectionHomeView.startAnimation(translateAnimation);

    }

    private void showWindDirectionView() {

        if(!showAnimationEnd){
            return;
        }

        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, -fragmentContainerView.getHeight(), 0);
        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        translateAnimation.setInterpolator(new LinearOutSlowInInterpolator());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                windDirectionHomeView.setVisibility(View.VISIBLE);
                showAnimationEnd = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showAnimationEnd = true;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        windDirectionHomeView.startAnimation(translateAnimation);
    }

    public void toPhoneFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if(carContractsFragment == null){
            carContractsFragment = new CarContractsFragment();
            fragmentTransaction.add(R.id.fragmentContainerView,carContractsFragment);
        }
        fragmentTransaction.show(carContractsFragment).hide(homeFragment).commit();
        currentFragment = carContractsFragment.getClass().getSimpleName();
    }

    public void toMediaFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if(musicHomeFragment == null){
            musicHomeFragment = new MusicHomeFragment();
            fragmentTransaction.add(R.id.fragmentContainerView,musicHomeFragment);
        }
        fragmentTransaction.show(musicHomeFragment).hide(homeFragment).commit();
        currentFragment = musicHomeFragment.getClass().getSimpleName();
    }

    public void toAppsListFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if(appsListFragment == null){
            appsListFragment = new AppsListFragment();
            fragmentTransaction.add(R.id.fragmentContainerView,appsListFragment);
        }
        fragmentTransaction.show(appsListFragment).hide(homeFragment).commit();
        currentFragment = appsListFragment.getClass().getSimpleName();

    }

    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.mCurrentThirdAppType = ThirdAppType.NONE;

        if(keyBackFloating!=null && keyBackFloating.isShow()){
            keyBackFloating.close();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(keyBackFloating!=null && !keyBackFloating.isShow()){
            keyBackFloating.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(keyBackFloating!=null && keyBackFloating.isShow()){
            keyBackFloating.close();
            keyBackFloating = null;
        }
        AMapNavi.destroy();
    }

    public void toMapFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if(mapHomeFragment == null){
            mapHomeFragment = new MapHomeFragment();
            fragmentTransaction.add(R.id.fragmentContainerView,mapHomeFragment);
        }
        fragmentTransaction.show(mapHomeFragment).hide(homeFragment).commit();
        currentFragment = MapHomeFragment.class.getSimpleName();
    }

    public void toMapSearchFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if(mapSearchFragment == null){
            mapSearchFragment = new MapSearchFragment();
            fragmentTransaction.add(R.id.fragmentContainerView,mapSearchFragment);
        }
        fragmentTransaction.show(mapSearchFragment).hide(homeFragment).commit();
        currentFragment = MapSearchFragment.class.getSimpleName();
    }

    public void toMapNavPathSelectFragment(PoiItemV2 poiItemV2) {
        MainActivity.isNaving = false;
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if(mapNavPathSelectFragment == null){
            mapNavPathSelectFragment = new MapNavPathSelectFragment();
            fragmentTransaction.add(R.id.fragmentContainerView, mapNavPathSelectFragment);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("poiItemV2",poiItemV2);
        mapNavPathSelectFragment.setArguments(bundle);
        fragmentTransaction.show(mapNavPathSelectFragment).hide(mapSearchFragment).hide(homeFragment).commit();
        currentFragment = MapNavPathSelectFragment.class.getSimpleName();
    }

    public void toMapNavFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if(mapNavFragment == null){
            mapNavFragment = new MapNavFragment();
            fragmentTransaction.add(R.id.fragmentContainerView, mapNavFragment);
        }
        fragmentTransaction.show(mapNavFragment).hide(homeFragment).commit();
        MainActivity.isNaving = true;

        currentFragment = MapNavFragment.class.getSimpleName();
    }

}
