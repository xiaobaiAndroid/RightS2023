package com.android.launcher.serialport.sender;

import android.text.format.Time;

import com.android.launcher.serialport.ttl.SerialHelperttlLd;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @date： 2023/11/13
 * @author: 78495
*/
public class TimerSender {

    private ScheduledExecutorService executorService;

    public TimerSender() {
        executorService =  Executors.newSingleThreadScheduledExecutor();
    }

    public void start(){
        executorService.scheduleAtFixedRate(() -> {
            //校准时间
            SerialHelperttlLd.sendHex("AABB22" + System.currentTimeMillis() + "CCDD");
        },2000,1000, TimeUnit.MILLISECONDS);
    }

    public void destroy(){
        if(executorService!=null){
            executorService.shutdown();
        }

    }
}
