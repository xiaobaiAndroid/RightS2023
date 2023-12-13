package com.android.launcher.can.sender;

import com.android.launcher.can.entity.Can1DBEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
* @description:
* @createDate: 2023/7/24
*/
public abstract class CanSenderBase<T> {

    protected static final String DATA_HEAD = "AA000000000";

    protected  String TAG = this.getClass().getSimpleName();

    protected String dataLength;
    protected  String ID;
    protected ScheduledExecutorService periodService;

    protected volatile boolean isInit = false;


    public CanSenderBase(String dataLength, String ID) {
        this.dataLength = dataLength;
        this.ID = ID;
        this.periodService = Executors.newSingleThreadScheduledExecutor();
    }

    public abstract void send();

    /**
     * @date： 2023/10/31
     * @author: 78495
     * @param period 周期，单位毫秒
     * @param delay 延迟执行
    */
    protected void startTask(long delay, long period){
        if(periodService !=null){
            periodService.scheduleAtFixedRate(() -> {
                try {
                    if(isInit){
                        execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, delay, period, TimeUnit.MILLISECONDS);
        }
    }

    protected abstract void execute();


    public void release(){
        try {
            if(periodService !=null){
                periodService.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isInit = false;
    };

    public abstract void updateData(T t);


}
