package com.android.launcher.can.receiver;

import android.content.Context;
import android.text.TextUtils;

import module.common.MessageEvent;
import module.common.utils.LogUtils;

import com.bzf.module_db.entity.CanBBTable;
import com.bzf.module_db.repository.CanBBRepository;

import org.greenrobot.eventbus.EventBus;

/**
* @description: 风向调整
* @createDate: 2023/7/25
*/
public class Can0BBReceiver extends CanReceiverBase{

    public Can0BBReceiver(Context context) {
        super(context);
    }

    @Override
    protected void disposeData(String data) {
        try {
            String d1 = data.substring(0, 2);
            String d2 = data.substring(2, 4);

            String wind= d1 ;
            String auto=  d2;

            LogUtils.printI(TAG, "data="+data+", wind="+wind +", auto="+auto);
            sendData(wind, auto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendData(String wind, String auto) {

        CanBBTable canBBEntity = CanBBRepository.getInstance().getData(context, deviceId);
        canBBEntity.setAuto(auto);
        if(!TextUtils.isEmpty(wind) && wind.length() == 2){
            canBBEntity.setLeftWindDirection(wind.substring(1));
            canBBEntity.setRightWindDirection(wind.substring(0,1));
        }
        LogUtils.printI(TAG, "handlerCan----CanBBEntity="+canBBEntity);

        CanBBRepository.getInstance().updateData(context, canBBEntity);

        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.AIRFLOW_ALLOCATION_DATA);
        messageEvent.data = canBBEntity;
        EventBus.getDefault().post(messageEvent);
    }

}
