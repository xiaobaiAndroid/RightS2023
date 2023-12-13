package com.android.launcher.can.receiver;

import android.content.Context;

import com.android.launcher.MyApp;
import com.android.launcher.entity.BinaryEntity;

import module.common.MessageEvent;
import com.android.launcher.can.entity.Can1DBEntity;

import module.common.utils.AppUtils;
import module.common.utils.LogUtils;

import com.bzf.module_db.entity.CanBCTable;
import com.bzf.module_db.repository.CanBCRepository;

import org.greenrobot.eventbus.EventBus;

/**
* @description: 读取后空调状态
* @createDate: 2023/7/25
*/
public class Can0BCReceiver extends CanReceiverBase{

    public Can0BCReceiver(Context context) {
        super(context);
    }

    @Override
    protected void disposeData(String data) {

        LogUtils.printI(TAG, "data"+data);
        try {

            //[bc, 6, 32, 5a, 60, 89, 38, 00, 00, 00] //后空调关闭
            //[bc, 6, 32, 5a, 60, 89, 18, 00, 00, 00] 后空调开启

            String d1 = data.substring(0, 2);
            String d2 = data.substring(2, 4);
            String d3 = data.substring(4, 6);
            String d4 = data.substring(6, 8);
            String d5 = data.substring(8, 10); //状态
            String d6 = data.substring(10, 12);
//            String d7 = data.substring(12, 14);
//            String d8 = data.substring(14, 16);

            Can1DBEntity can1DBEntity = new Can1DBEntity();
            can1DBEntity.setD1(new BinaryEntity(d1));
            can1DBEntity.setD2(new BinaryEntity(d2));
            can1DBEntity.setD3(new BinaryEntity(d3));
            can1DBEntity.setD5(new BinaryEntity(d6));

            MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CAN0BC_TO_CAN1DB);
            messageEvent.data = can1DBEntity;
            EventBus.getDefault().post(messageEvent);



            CanBCTable canBCEntity = CanBCRepository.getInstance().getData(context, deviceId);
            canBCEntity.setStatus(d5);
            canBCEntity.setLefttemp(d1);
            canBCEntity.setRighttemp(d2);
            canBCEntity.setWind(d3);
            canBCEntity.setWinddic(d4);
            canBCEntity.setFrontBackStatus(d6);

            LogUtils.printI(TAG,"canBCEntity="+canBCEntity);
            CanBCRepository.getInstance().updateData(context,canBCEntity);

            updateUI(canBCEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI(CanBCTable canBCEntity) {

        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.UPDATE_REAR_AC_DATA);
        messageEvent.data = canBCEntity;
        EventBus.getDefault().post(messageEvent);
    }
}
