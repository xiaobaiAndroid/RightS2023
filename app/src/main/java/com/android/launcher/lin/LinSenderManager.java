package com.android.launcher.lin;

import module.common.MessageEvent;

import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.lin.entity.Lin30Entity;
import com.android.launcher.lin.sender.Lin30Sender;

import module.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @description:
 * @createDate: 2023/7/25
 */
public class LinSenderManager {

    private static final String TAG = LinSenderManager.class.getSimpleName();

    private Lin30Sender lin30Sender = new Lin30Sender();


    public void registerListener() {
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregisterListener() {
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        lin30Sender.release();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        try {
            if(event.type == MessageEvent.Type.CAN10C_TO_LIN30_D6){
                LogUtils.printI(TAG, "type="+event.type.name() + ", data="+event.data);
                BinaryEntity d6Entity = (BinaryEntity) event.data;
                lin30Sender.setD6B0B1(d6Entity);
            }else if(event.type == MessageEvent.Type.CAN10C_TO_LIN30_D5){
                LogUtils.printI(TAG, "type="+event.type.name() + ", data="+event.data);
                BinaryEntity d5Entity = (BinaryEntity) event.data;
                lin30Sender.setD5(d5Entity);
            }else if(event.type == MessageEvent.Type.CAN001_TO_LIN30_D6){
                LogUtils.printI(TAG, "type="+event.type.name() + ", data="+event.data);
                BinaryEntity d6Entity = (BinaryEntity) event.data;
                lin30Sender.setD6B5(d6Entity);
            }else if(event.type == MessageEvent.Type.CAN069_TO_LIN30_D1){
                LogUtils.printI(TAG, "type="+event.type.name() + ", data="+event.data);
                BinaryEntity d1Entity = (BinaryEntity) event.data;
                lin30Sender.setD1(d1Entity);
            }else if(event.type == MessageEvent.Type.CAN069_TO_LIN30_D6){
                LogUtils.printI(TAG, "type="+event.type.name() + ", data="+event.data);
                BinaryEntity d6Entity = (BinaryEntity) event.data;
                lin30Sender.setD6(d6Entity);
            }else if(event.type == MessageEvent.Type.CAN39F_TO_LIN30){
                LogUtils.printI(TAG, "type="+event.type.name() + ", data="+event.data);
                Lin30Entity lin30Entity = (Lin30Entity) event.data;
                lin30Sender.updateData(lin30Entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
