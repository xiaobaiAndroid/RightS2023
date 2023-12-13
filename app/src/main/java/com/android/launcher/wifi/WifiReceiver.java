package com.android.launcher.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.android.launcher.MainActivity;
import com.android.launcher.MyApp;

import module.common.MessageEvent;
import module.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @description: wifi状态监听器
 * @createDate: 2023/5/7
 */
public class WifiReceiver extends BroadcastReceiver {

    private static final String TAG = WifiReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.printI(TAG, "action=" + intent.getAction());
        try {
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                LogUtils.printI(WifiReceiver.class.getSimpleName(), "networkInfo=" + networkInfo);
                if (networkInfo != null) {
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        disposeWifiConnected(context);

                    } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && !networkInfo.isConnected()) {
                        // Wi-Fi 断开连接
                        MainActivity.connectedWifiInfo = null;
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.WIFI_DISCONNECTED));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disposeWifiConnected(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        try {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo == null) {
                LogUtils.printI(TAG, "disposeWifiConnected-----wifiInfo=" + wifiInfo);
                return;
            }
            MainActivity.connectedWifiInfo = wifiInfo;

//            //获取当前wifi名称
//            String ssid = wifiInfo.getSSID().replaceAll("\"", "");
//            if(!MyApp.mWifiConnectState){
//                    LogUtils.printI(TAG, "WIFI已连接 ----wifiInfo=" + wifiInfo);
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.WIFI_CONNECTED));
//            }else{
//                LogUtils.printI(TAG, "WIFI已连接,无需重复连接 ----wifiInfo=" + wifiInfo);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.printI(TAG, "disposeWifiConnected----Exception=" + e.getMessage());
        }
    }

    public static void register(Context context, WifiReceiver wifiReceiver) {
        if (context != null) {
            try {
                IntentFilter filter = new IntentFilter();
                filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                context.registerReceiver(wifiReceiver, filter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogUtils.printI(TAG, "register----");
    }

    public static void unregister(Context context, WifiReceiver wifiReceiver) {
        try {
            if (context != null) {
                context.unregisterReceiver(wifiReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.printI(TAG, "unregister----");
    }
}