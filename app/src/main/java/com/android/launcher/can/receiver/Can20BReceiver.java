package com.android.launcher.can.receiver;

import android.content.Context;

import module.common.MessageEvent;

import com.android.launcher.MyApp;

import module.common.utils.AppUtils;
import module.common.utils.LogUtils;

import com.android.launcher.ac.wind.WindUtils;
import com.bzf.module_db.entity.Can20BTable;
import com.bzf.module_db.repository.Can20BRepository;

import org.greenrobot.eventbus.EventBus;

/**
* @description:  读取前空调状态
* @createDate: 2023/7/24
*/
public class Can20BReceiver extends CanReceiverBase{

    public Can20BReceiver(Context context) {
        super(context);
    }

    @Override
    protected void disposeData(String data) {
        taskExecutor.execute(() -> {
            try {
                LogUtils.printI(TAG, "data="+data);
                String driverTemp= data.substring(0,2); //d1
                String frontSeatTemp = data.substring(2,4); //d2
                String driverWind = data.substring(4,6); //d3
                String frontSeatWind =  data.substring(6,8);  //d4
                String acKeyStatus =  data.substring(8,10) ; //d5

                LogUtils.printI(TAG, "driverWind="+driverWind+",frontSeatWind="+frontSeatWind);

                send20BData(driverTemp, frontSeatTemp, driverWind, frontSeatWind, acKeyStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void send20BData(String driverTemp, String frontSeatTemp, String driverWind, String frontSeatWind, String acKeyStatus) {
        try {

            Can20BTable can20bEntity = Can20BRepository.getInstance().getData(context, AppUtils.getDeviceId(context));
            can20bEntity.setDriverTemp(driverTemp);
            can20bEntity.setFrontSeatTemp(frontSeatTemp);
            can20bEntity.setDriverWind(driverWind);
            can20bEntity.setFrontSeatWind(frontSeatWind);
            can20bEntity.setAcKeyStatus(acKeyStatus);

            Can20BRepository.getInstance().updateData(context,can20bEntity);

            LogUtils.printI(Can20BReceiver.class.getSimpleName(),"Can20bEntity---"+can20bEntity);
            MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.UPDATE_FRONT_AC);
            messageEvent.data = can20bEntity;
            EventBus.getDefault().post(messageEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}