package com.android.launcher.serialport.sender;

import android.provider.Settings;

import com.android.launcher.MyApp;
import com.android.launcher.serialport.ttl.SerialHelperttlLd;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import module.common.utils.LogUtils;

/**
 * 设备Id
 * @date： 2023/11/13
 * @author: 78495
*/
public class DeviceIdSender {

    private ScheduledExecutorService executorService;

    public DeviceIdSender() {
        executorService =  Executors.newSingleThreadScheduledExecutor();
    }

    public void start(){
        executorService.scheduleAtFixedRate(() -> {
            //30秒发送一次设备Id
            String deviceId = "";
            try {
                deviceId = Settings.System.getString(MyApp.getGlobalContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                SerialHelperttlLd.sendHex("AABB21" + deviceId + "CCDD");
            } catch (Exception e) {
                e.printStackTrace();
            }

        },10000,30000, TimeUnit.MILLISECONDS);
    }

    public void destroy(){
        if(executorService!=null){
            executorService.shutdown();
        }

    }
}
