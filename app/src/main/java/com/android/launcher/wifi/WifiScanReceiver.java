package com.android.launcher.wifi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import module.common.MessageEvent;
import module.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
* @description:
* @createDate: 2023/5/8
*/
@Deprecated
public class WifiScanReceiver extends BroadcastReceiver {

    private static final String TAG = WifiScanReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.printI(WifiScanReceiver.class.getSimpleName(),"onReceive----action="+intent.getAction());
        boolean success = intent.getBooleanExtra(
                WifiManager.EXTRA_RESULTS_UPDATED, false);
        if (success) {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            disposeWifiList(context, mWifiManager);
        } else {
            LogUtils.printI(WifiScanReceiver.class.getSimpleName(),"WifiScanReceiver----wifi扫描失败");
        }
    }

    private void disposeWifiList(Context context, WifiManager mWifiManager) {
        List<ScanResult> scanResults = mWifiManager.getScanResults();
        String ssid = WifiUtil.getHistoryConnectedWifi(context);
        if(TextUtils.isEmpty(ssid)){
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.NOT_FIND_HIS_CON_WIFI));
            return;
        }
        try {
            if(scanResults!= null && scanResults.size() > 0){
                for (int i = 0; i<scanResults.size(); i++){
                    //连接wifi
                    ScanResult scanResult = scanResults.get(i);
                    LogUtils.printI(TAG,"scanWifiInfo---scanResult="+scanResult);
                    if(ssid.equalsIgnoreCase(scanResult.SSID) || ssid.equals("ben123")){
                        connectWifi(context,scanResult.SSID,"abc123456987","");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.printI(TAG,"disposeWifiList---e="+e.getMessage());
        }
    }


    /**
     * 连接wifi
     * @param targetSsid wifi的SSID
     * @param targetPsd 密码
     * @param enc 加密类型
     */
    @SuppressLint("WifiManagerLeak")
    public void connectWifi(Context context,String targetSsid, String targetPsd, String enc) throws Exception{
        // 1、注意热点和密码均包含引号，此处需要需要转义引号
        String ssid = "\"" + targetSsid + "\"";
        String psd = "\"" + targetPsd + "\"";

        //2、配置wifi信息
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = ssid;
        switch (enc) {
            case "WEP":
                // 加密类型为WEP
                conf.wepKeys[0] = psd;
                conf.wepTxKeyIndex = 0;
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                break;
            case "WPA":
                // 加密类型为WPA
                conf.preSharedKey = psd;
                break;
            case "OPEN":
                //开放网络
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        LogUtils.printI(TAG, "connectWifi----WifiConfiguration="+conf);

        //3、链接wifi
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);
        @SuppressLint("MissingPermission") List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration wifiConfiguration : list) {
            if (wifiConfiguration.SSID != null && wifiConfiguration.SSID.equals(ssid)) {
                LogUtils.printI(TAG, "connectWifi----WifiConfiguration="+wifiConfiguration);
                wifiManager.disconnect();
                wifiManager.enableNetwork(wifiConfiguration.networkId, true);
                boolean reconnect = wifiManager.reconnect();
                if(reconnect){
                    LogUtils.printI(TAG, "connectWifi----连接热点成功");
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SCAN_WIFI_CONNECTED));
                }else{
                    LogUtils.printI(TAG, "connectWifi----连接热点失败");
                }
                break;
            }
        }
    }

    public static void register(Context context, WifiScanReceiver wifiScanReceiver){
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            context.registerReceiver(wifiScanReceiver, intentFilter);

            LogUtils.printI(TAG, "register------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregister(Context context, WifiScanReceiver wifiScanReceiver){
        try {
            context.unregisterReceiver(wifiScanReceiver);
            LogUtils.printI(TAG, "unregister------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描附近wifi
     */
    public static void scanWifiInfo(Context context) {
        try {
            LogUtils.printI(TAG,"scanWifiInfo---wifi开始扫描");
            String wserviceName = Context.WIFI_SERVICE;
            WifiManager mWifiManager = (WifiManager) context.getSystemService(wserviceName);

//            mWifiManager.setWifiEnabled(true);
            boolean startScan = mWifiManager.startScan();
            if(!startScan){
                LogUtils.printI(TAG,"scanWifiInfo---wifi扫描失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.printI(TAG,"scanWifiInfo---e="+e.getMessage());
        }

    }
}


