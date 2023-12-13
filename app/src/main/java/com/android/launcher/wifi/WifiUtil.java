package com.android.launcher.wifi;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;

import module.common.utils.LogUtils;

import java.util.List;

/**
 * @description:
 * @createDate: 2023/5/8
 */
public class WifiUtil {

    private static final String TAG = WifiUtil.class.getSimpleName();

    public static final String SSID = "ben123";

    //gzdc0001benz
    public static final String SSID_START_WITH = "gzdc";
    public static final String SSID_END = "benz";



//    public static void openWifi() {
//        try {d
//            WifiManager wifiManager = (WifiManager) App.getGlobalContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            if (!wifiManager.isWifiEnabled()) {
//                wifiManager.setWifiEnabled(true);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtils.printI(TAG, "openWifi----"+e.getMessage());
//        }
//    }
//
//    public static void closeWifi() {
//        WifiManager wifiManager = (WifiManager) App.getGlobalContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        try {
//            if (!wifiManager.isWifiEnabled()) {
//                boolean wifiEnabled = wifiManager.setWifiEnabled(false);
//                if(wifiEnabled){
//                    LogUtils.printI(TAG, "closeWifi---关闭wifi开关成功");
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtils.printI(TAG, "closeWifi---关闭wifi开关失败"+e.getMessage());
//        }
//    }

//    // 连接到热点
//    private boolean connectToWifi(String ssid, String password) {
//        // 获取系统 WiFi 管理器
//        WifiManager wifiManager = (WifiManager) App.getGlobalContext().getSystemService(Context.WIFI_SERVICE);
//
//        // 开启系统 WiFi
//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//        }
//
//        // 扫描可用的 WiFi 列表
//        List<ScanResult> scanResults = wifiManager.getScanResults();
//        for (ScanResult result : scanResults) {
//            if (result.SSID.equals(ssid)) {
//                // 连接到指定 SSID 的 WiFi
//                WifiConfiguration config = new WifiConfiguration();
//                config.SSID = "\"" + ssid + "\"";
//                config.preSharedKey = "\"" + password + "\"";
//                int networkId = wifiManager.addNetwork(config);
//                wifiManager.enableNetwork(networkId, true);
//                return wifiManager.reconnect();
//            }
//        }
//        return false;
//    }



    /**
    * @description: 获取连接过的wifi
    * @createDate: 2023/5/9
    */
    @TargetApi(Build.VERSION_CODES.R)
    @SuppressLint("MissingPermission")
    public static String getHistoryConnectedWifi(Context context){
        String historyConnectedWifi = null;

        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                List<WifiNetworkSuggestion> suggestions = wifiManager.getNetworkSuggestions();
                for (WifiNetworkSuggestion suggestion : suggestions) {
                    LogUtils.printI(TAG,"getHistoryConnectedWifi----WifiConfiguration="+suggestion);
                    if(suggestion != null && suggestion.getSsid() != null){
                        String ssid = suggestion.getSsid().replaceAll("\"", "");
                        LogUtils.printI(TAG,"getHistoryConnectedWifi----ssid="+ssid);
                        if(isNeedWifi(ssid)){
                            historyConnectedWifi = ssid;
                            break;
                        }
                    }
                }
            }else{
                List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
                for (WifiConfiguration config : configurations) {
                    LogUtils.printI(TAG,"getHistoryConnectedWifi----WifiConfiguration="+config);

                    if(config != null && config.SSID != null){
                        String ssid = config.SSID.replaceAll("\"", "");
                        if(isNeedWifi(ssid)){
                            historyConnectedWifi = ssid;
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.printI(TAG,"getHistoryConnectedWifi----Exception="+e.getMessage());
        }
        LogUtils.printI(TAG,"getHistoryConnectedWifi----historyConnectedWifi="+historyConnectedWifi);
        return historyConnectedWifi;
    }

    public static boolean isNeedWifi(String ssid){
        return ssid.startsWith(SSID_START_WITH) && ssid.endsWith(SSID_END);
    }

    // 检查当前是否连接到 Wi-Fi 网络
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    // 获取当前连接的 Wi-Fi 的信息
    public static WifiInfo getConnectedWifiInfo(Context context) {
        if (isWifiConnected(context)) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            // 获取 Wi-Fi 的 SSID
            return wifiInfo;
        } else {
            return null;
        }
    }

}
