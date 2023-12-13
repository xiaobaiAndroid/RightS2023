package com.android.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.MapsInitializer;
import com.android.launcher.base.CommonActivity;
import com.android.launcher.bluetooth.BluetoothDeviceHelper;
import com.android.launcher.service.LivingService;
import com.android.launcher.util.ACache;

import module.common.utils.FMHelper;

import com.android.launcher.util.FuncUtil;
import com.android.launcher.wifi.WifiUtil;
import com.lxj.xpopup.util.KeyboardUtils;

import java.lang.reflect.Method;

/**
 * @date： 2023/10/12
 * @author: 78495
*/
public class SplashActivity extends CommonActivity {

    public ACache aCache = ACache.get(MyApp.getGlobalContext());
    public TextView logo;

    public CountDownTimer countDownTimer ;


    @Override
    protected void setupData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        try {
            MapsInitializer.updatePrivacyShow(getApplicationContext(),true,true);
            MapsInitializer.updatePrivacyAgree(getApplicationContext(),true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        statusBarView.setVisibility(View.INVISIBLE);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rootCL.getLayoutParams();
        layoutParams.bottomMargin = 0;
        rootCL.requestLayout();

        KeyboardUtils.hideSoftInput(rootCL);

        LivingService.connectBlueDevice = BluetoothDeviceHelper.getConnectDevice();
        MainActivity.connectedWifiInfo = WifiUtil.getConnectedWifiInfo(this);

        logo = findViewById(R.id.logo);

        if (FuncUtil.PRESSHOME){
            Intent intent = new Intent(this, MainActivity.class) ;
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK) ;
            startActivity(intent);

        }else{

            Log.i("LauncherAcc", "------- ----MainActivity-------oncreate==1========");
            String logoSelect = aCache.getAsString("logo");

            if (logoSelect != null) {
                Log.i("LauncherAcc", "------- ----MainActivity-------oncreate=2========="+logoSelect);
//                Drawable drawable = App.getGlobalContext().getResources().getDrawable(R.drawable.benzlogo2);
//                logo.setBackground(drawable);
            } else {
                Log.i("LauncherAcc", "------- ----MainActivity-------oncreate=3========="+logoSelect);
//                Drawable drawable1 = App.getGlobalContext().getResources().getDrawable(R.drawable.benzlogo);
//                logo.setBackground(drawable1);
            }
        }
//        PackageManager
        Log.i(this.getClass().getName(),"onCreate----");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    protected void onStart() {
        FMHelper.finishFM(this);

        Log.i("LauncherAcc", "------- ----SplashActivity-------onstart==1========");
        String logoSelect = aCache.getAsString("logo");
        if (logoSelect != null) {
            Log.i("LauncherAcc", "------- ----SplashActivity-------onstart=2========="+logoSelect);
//            Drawable drawable = App.getGlobalContext().getResources().getDrawable(R.drawable.benzlogo2);
//            logo.setBackground(drawable);
        } else {
            Log.i("LauncherAcc", "------- ----SplashActivity-------onstart=3========="+logoSelect);
//            Drawable drawable1 = App.getGlobalContext().getResources().getDrawable(R.drawable.benzlogo);
//            logo.setBackground(drawable1);
        }

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method method = c.getMethod("get", String.class, String.class);
            String os = (String) method.invoke(c, "sys.suspend.state", "");
            Log.i("LauncherAcc", "------- ----SplashActivity-------onstart=4========="+os);
            if(os.equals("0")){
                if (countDownTimer!=null){
                    countDownTimer.cancel();
                    countDownTimer = null ;
                }
                if (countDownTimer==null){
                    countDownTimer = new CountDownTimer(5*1000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.i("LauncherAcc", "------- ----SplashActivity--5*1000-----onTick=4========="+millisUntilFinished);
                        }
                        @Override
                        public void onFinish() {
                            Log.i("LauncherAcc", "------- ----SplashActivity--5*1000-----onFinish=4=========");
                            startNav();
                        }
                    }.start();
                }
            }else{
                if (countDownTimer!=null){
                    countDownTimer.cancel();
                    countDownTimer = null ;
                }
                if (countDownTimer==null){
                    countDownTimer = new CountDownTimer(10*1000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.i("LauncherAcc", "------- ----SplashActivity--15*1000-----onTick=4========="+millisUntilFinished);
                        }
                        @Override
                        public void onFinish() {
                            Log.i("LauncherAcc", "------- ----SplashActivity--15*1000-----onFinish=4=========");
                            startNav();
                        }
                    }.start();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        super.onStart();

        Log.i(this.getClass().getName(),"onStart----");
    }

    private void startNav() {
        FMHelper.finishFM(this.getApplicationContext());

        android.provider.Settings.System.putString(MyApp.getGlobalContext().getContentResolver(),"boot_xyapk","");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("LauncherAcc", "------- ----SplashActivity-------onRestart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LauncherAcc", "------- ----SplashActivity-------onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i("LauncherAcc", "------- ---SplashActivity-------onStop");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LauncherAcc","onDestroy----");
        if(countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer = null ;
            Log.i("LauncherAcc", "------- ---SplashActivity-------countDownTimer cancel");
        }
        Log.i("LauncherAcc", "------- ---SplashActivity-------onDestroy");
    }

}