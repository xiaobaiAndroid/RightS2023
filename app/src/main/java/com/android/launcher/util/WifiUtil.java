package com.android.launcher.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.android.launcher.MyApp;

import java.lang.reflect.Method;
import java.util.List;

import module.common.utils.LogUtils;

@Deprecated
public class WifiUtil {


    /**
     * 设置wifi配置
     * @param wifiName
     * @param password
     * @return
     */
    public static WifiConfiguration getConfig(String wifiName, String password){
        String ssid = "\"" + wifiName + "\"";
        String psd = "\"" + password + "\"";

        //2、配置wifi信息
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = ssid;
        conf.preSharedKey = psd;

        return conf ;
    }


    /**
     * 保存wifi
     * @param config
     * @return
     */
    public static  boolean saveWifi(WifiManager mWifiManager, WifiConfiguration config) {
        if (mWifiManager == null) {
            return false;
        }
        try {
            Method save = mWifiManager.getClass().getDeclaredMethod("save", WifiConfiguration.class, Class.forName("android.net.wifi.WifiManager$ActionListener"));
            if (save != null) {
                save.setAccessible(true);
                save.invoke(mWifiManager, config, null);
            }
            return true ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    @SuppressLint("NewApi")
    public static void test()
    {

        WifiManager wifiManager = (WifiManager) MyApp.getGlobalContext().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);


        WifiConfiguration wifiConfiguration = getConfig("BENCAR","12345678");
        saveWifi(wifiManager,wifiConfiguration);

        wifiManager.setWifiEnabled(true) ;


        int state = wifiManager.getWifiState() ;

        Log.i("mywifi","-----------aa------------1-------------2-----------"+state) ;
//            NetworkSpecifier specifier =
//                    new WifiNetworkSpecifier.Builder()
//                            .setSsidPattern(new PatternMatcher("lxpiPhone", PatternMatcher.PATTERN_PREFIX))
//                            .setWpa2Passphrase("9876543210")
//                            .build();

//            NetworkRequest request =
//                    new NetworkRequest.Builder()
//                            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//                            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//                            .setNetworkSpecifier(specifier)
//                            .build();
//
//            ConnectivityManager connectivityManager = (ConnectivityManager) App.getGlobalContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//
//            ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
//                @Override
//                public void onAvailable(Network network) {
//                    // do success processing here..
//                    Log.i("wifi","-aaa----11---------") ;
//                }
//
//                @Override
//                public void onUnavailable() {
//                    // do failure processing here..
//                }
//            };
//            connectivityManager.requestNetwork(request, networkCallback);
            // Release the request when done.
            // connectivityManager.unregisterNetworkCallback(networkCallback);

    }


    @SuppressLint("MissingPermission")
    public static void removWifi(){
        WifiManager wifiManager = (WifiManager) MyApp.getGlobalContext().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);


        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

        for( WifiConfiguration i : list ) {
            Log.i("mywifi",i.SSID+"-----------------------");
//            wifiManager.removeNetwork(i.networkId);
            if (!i.SSID.equals("BENCAR")){
                wifiManager.removeNetwork(i.networkId);
            }
        }

    }


    /**
    * @description: 开启wifi热点
    * @createDate: 2023/6/9
    */
    public static boolean setWifiApEnabled(Context context) {
        try {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            mWifiManager.setWifiEnabled(true);
            LogUtils.printI(WifiUtil.class.getSimpleName(), "setWifiApEnabled----"+mWifiManager.getWifiState());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
