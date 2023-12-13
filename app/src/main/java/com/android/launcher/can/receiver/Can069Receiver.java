package com.android.launcher.can.receiver;

import android.content.Context;
import android.text.TextUtils;

import module.common.MessageEvent;

import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.util.DataUtils;
import com.android.launcher.can.entity.Can015Entity;

import module.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
* @description: 背景灯的状态
* @createDate: 2023/7/24
*/
public class Can069Receiver extends CanReceiverBase{


    public Can069Receiver(Context context) {
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

            String b2 = d1Binary.substring(5,6);
            LogUtils.printI(TAG, "b2="+b2);

            if("0".equals(b2)){ //015发送的报文为 00 64 00 64，LIN发送中的ID:30中的D1=0x00.
                sendLin30D1Data("00");
                sendCan015Data("00", "64", "00", "64");
            }else{ //当接收到CAN的ID：69的报文中的B0:b2=1时，CAN发送中的ID：015发送的报文为 上次所存储的 ，如果没有存储，默认值是 50 64 00 64，此时，LIN发送中的ID:30中的B0=0x50，其数值等于CAN发送中的ID：015的B0。

                //上次所存储的 ? ?
                String can015D1 = "50";
                sendCan015Data(can015D1, "64", "00", "64");
                sendLin30D1Data(can015D1);
            }

            String b5 = d1Binary.substring(2,3);

            String d2 = data.substring(2,4);
            String d2Binary = DataUtils.hexStringToBinaryString(d2);
            String b1 = d2Binary.substring(6, 7);

            BinaryEntity lin30D6Entity = new BinaryEntity();
            if(BinaryEntity.Value.NUM_0.getValue().equals(b1)){
                lin30D6Entity.setB4(BinaryEntity.Value.NUM_0);
            }else{
                lin30D6Entity.setB4(BinaryEntity.Value.NUM_1);
            }

            if(BinaryEntity.Value.NUM_0.getValue().equals(b5)){
                lin30D6Entity.setB6(BinaryEntity.Value.NUM_0);
            }else{
                lin30D6Entity.setB6(BinaryEntity.Value.NUM_1);
            }

            if(BinaryEntity.Value.NUM_0.getValue().equals(b2)){
                lin30D6Entity.setB7(BinaryEntity.Value.NUM_0);
            }else{
                lin30D6Entity.setB7(BinaryEntity.Value.NUM_1);
            }

            MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CAN069_TO_LIN30_D6);
            messageEvent.data = lin30D6Entity;
            EventBus.getDefault().post(messageEvent);

            LogUtils.printI(TAG, "lin30D6Entity="+lin30D6Entity + ", d2Binary="+d2Binary);
        }
    }

    private void sendCan015Data(String d1, String d2, String d3, String d4) {
        Can015Entity can015Entity = new Can015Entity();
        can015Entity.setD1(new BinaryEntity(d1));
        can015Entity.setD2(new BinaryEntity(d2));
        can015Entity.setD3(new BinaryEntity(d3));
        can015Entity.setD4(new BinaryEntity(d4));

        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CAN015_UPDATE);
        messageEvent.data = can015Entity;
        EventBus.getDefault().post(messageEvent);
    }

    private void sendLin30D1Data(String data) {
        BinaryEntity lin30D1Entity = new BinaryEntity(data);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CAN069_TO_LIN30_D1);
        messageEvent.data = lin30D1Entity;
        EventBus.getDefault().post(messageEvent);
    }
}
