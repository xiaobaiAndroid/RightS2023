package com.android.launcher.can.receiver;

import android.content.Context;
import android.text.TextUtils;

import module.common.MessageEvent;

import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.util.DataUtils;

import module.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
* @description: 雷达状态
* @createDate: 2023/7/24
*/
public class Can2EEReceiver extends CanReceiverBase{

    public Can2EEReceiver(Context context) {
        super(context);
    }

    @Override
    protected void disposeData(String data) {
        String d1 = data.substring(0, 2);
        String d1Binary = DataUtils.hexStringToBinaryString(d1);

        LogUtils.printI(TAG, "data="+data + ", d1="+d1 +", d1Binary="+d1Binary);
        if(!TextUtils.isEmpty(d1Binary)){
            //b7 b6 b5 b4 b3 b2 b1 b0  //标识
            //0  0  0  0  0  0  0  0
            //0  1  2  3  4  5  6  7  角标

            String b7 = d1Binary.substring(0,1);

            BinaryEntity lin30D5Entity = new BinaryEntity();
            if(BinaryEntity.Value.NUM_0.getValue().equals(b7)){
                lin30D5Entity.setB5(BinaryEntity.Value.NUM_0);
            }else{
                lin30D5Entity.setB5(BinaryEntity.Value.NUM_1);
            }

            LogUtils.printI(TAG, "lin30D5Entity="+lin30D5Entity.toString());

            MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CAN10C_TO_LIN30_D5);
            messageEvent.data = lin30D5Entity;
            EventBus.getDefault().post(messageEvent);
        }

    }
}
