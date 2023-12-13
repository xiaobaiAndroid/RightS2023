package com.android.launcher;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;

import com.android.launcher.receiver.USBBroadCastReceiver;
import com.android.launcher.util.ACache;
import com.android.launcher.util.LogcatHelper;
import com.android.launcher.wifi.CommunicationStatus;

import module.common.utils.FMHelper;
import module.common.utils.LogUtils;


public class MyApp extends Application  {


    public static ACache aCache  ;

    //暂时无用，双系统使用
    public static boolean propCar;



    //是否初始化从设备读取空调压缩机状态
//    public static boolean isInitDeviceAcOff = false;
    /**
     * 全局Context
     */
    private static Context mContext;

    public static AudioManager audioManager ;

    public static boolean INITFLG ;


    public static  int size ;

    public static volatile String currentActivityStr;


    public static String rountIpLocalIp ;

    public static volatile boolean livingServerStop = true;



    public static CommunicationStatus communicationStatus = CommunicationStatus.CONNECT_INIT;


    //触控版的连接状态
    public static Boolean mMouseConnectState = false;
    //键盘的连接状态
    public static Boolean mKeyboardConnectState = false;

    public static boolean isAppStart = false;

    //是否在NavActivity页面
    public static boolean isInNavActivity = false;


    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        isInNavActivity = false;
        isAppStart = true;
        mContext =  getApplicationContext() ;
        FMHelper.finishFM(getApplicationContext());

        communicationStatus = CommunicationStatus.CONNECT_INIT;
        initApp();
        USBBroadCastReceiver.registerReceiver();
        try {
            LogcatHelper.getInstance(MyApp.getGlobalContext()).stop();
            LogcatHelper.getInstance(MyApp.getGlobalContext()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.printI(MyApp.class.getSimpleName(),"onCreate----");

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    //初始化内容
    private void initApp() {
        //音控控制管理
        audioManager =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        aCache = ACache.get(mContext) ;
        String cms = aCache.getAsString("CMS") ;
        if (cms==null){
            aCache.put("CMS","C");
        }
    }

    public static ACache getaCache(){
        return aCache ;
    }


    /**
     * 获取全局Context
     * @return
     */
    public static Context getGlobalContext() {
        return mContext;
    }


}
