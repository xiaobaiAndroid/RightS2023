package com.android.launcher.can.receiver;

import android.content.Context;
import android.text.TextUtils;

import module.common.MessageEvent;

import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.util.DataUtils;

import module.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
* @description: 悬挂状态
* @createDate: 2023/7/24
*/
public class Can10CReceiver extends CanReceiverBase {

    public Can10CReceiver(Context context) {
        super(context);
    }

    @Override
    protected void disposeData(String data) {
        String d2 = data.substring(4, 6);
        String d2Binary = DataUtils.hexStringToBinaryString(d2);

        LogUtils.printI(TAG, "data="+data + ", d2="+d2 +", d2Binary="+d2Binary);
        if(!TextUtils.isEmpty(d2Binary)){
            //b7 b6 b5 b4 b3 b2 b1 b0  //标识
            //0  0  0  0  0  0  0  0
            //0  1  2  3  4  5  6  7  角标

            String b0 = d2Binary.substring(7);
            String b1 = d2Binary.substring(6,7);

            BinaryEntity lin30Entity = new BinaryEntity();
            if("1".equals(b0)){
                lin30Entity.setB0(BinaryEntity.Value.NUM_1);
            }
            if("1".equals(b1)){
                lin30Entity.setB1(BinaryEntity.Value.NUM_1);
            }

            LogUtils.printI(TAG, "lin30Entity="+lin30Entity.toString());

            MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CAN10C_TO_LIN30_D6);
            messageEvent.data = lin30Entity;
            EventBus.getDefault().post(messageEvent);
        }

    }
}
