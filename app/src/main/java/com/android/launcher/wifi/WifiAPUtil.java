package com.android.launcher.wifi;


import android.content.Context;
import android.net.ConnectivityManager;

import module.common.utils.LogUtils;

import java.lang.reflect.Method;

public class WifiAPUtil {
    private static final String TAG = "WifiAPUtil";


    /**
     * 打开WiFi热点
     * @param context
     */
    public static void startTethering(Context context) {

//        LogUtils.printE(TAG, "开启热点----");
//
//        //1、环境属性记录
//        String property = System.getProperty("dexmaker.dexcache");
//
//        //2、设置新的属性
//        System.setProperty("dexmaker.dexcache", context.getCacheDir().getPath());
//
//        //3、反射操作打开热点
//        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
//        try {
//            Class classOnStartTetheringCallback = Class.forName("android.net.ConnectivityManager$OnStartTetheringCallback");
//            Method startTethering = connectivityManager.getClass().getDeclaredMethod("startTethering", int.class, boolean.class, classOnStartTetheringCallback);
//            Object proxy = ProxyBuilder.forClass(classOnStartTetheringCallback).handler(new InvocationHandler() {
//                @Override
//                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
//                    LogUtils.printI(WifiServerSocket.class.getSimpleName(),"method="+method.getName());
//                    return null;
//
//                }
//            }).build();
//            startTethering.invoke(connectivityManager, 0, false, proxy);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtils.printI(WifiAPUtil.class.getSimpleName(),"startTethering----"+e.getMessage());
//        }
//
//        //4、恢复环境属性
//        if (property != null) {
//            System.setProperty("dexmaker.dexcache", property);
//        }

    }


    /**
     * @description: 关闭热点
     * @createDate: 2023/5/8
     */
    public static void closeTethering(Context context){

        try {
            ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            Method startTethering = connectivityManager.getClass().getDeclaredMethod("stopTethering", int.class);
            startTethering.invoke(connectivityManager, 0);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.printI(WifiAPUtil.class.getSimpleName(),"closeTethering----"+e.getMessage());
        }

        LogUtils.printI(WifiAPUtil.class.getSimpleName(),"closeTethering----");

    }

}