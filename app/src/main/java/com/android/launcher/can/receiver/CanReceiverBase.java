package com.android.launcher.can.receiver;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import module.common.utils.AppUtils;
import module.common.utils.LogUtils;

/**
* @description:
* @createDate: 2023/7/24
*/
public abstract class CanReceiverBase {


    protected String data;
    protected String lastData;

    private boolean isStop = false;

    protected Context context;

    protected String TAG = this.getClass().getSimpleName();

    protected ExecutorService taskExecutor;

    protected String deviceId;

    public CanReceiverBase(Context context) {
        lastData = "";
        this.context = context;
        taskExecutor = Executors.newSingleThreadExecutor();
        deviceId = AppUtils.getDeviceId(context);
    }

    public final void updateData(String data){
        this.data =data;
        if(!isStop){
            if(lastData.equalsIgnoreCase(data)){
                return;
            }
            LogUtils.printI(TAG, "isStop="+isStop +", lastData="+lastData +", data="+data);
            lastData = data;
            try {
                taskExecutor.execute(() -> disposeData(data));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    protected abstract void disposeData(String data);

    public void stop(){
        isStop = true;
    }

    public void release(){
        LogUtils.printI(TAG, "release----");

        data = "";
        lastData = "";
        isStop = true;
        context = null;

        try {
            if(taskExecutor!=null){
                taskExecutor.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
