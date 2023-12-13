package com.android.launcher.lin.recevier;

import module.common.MessageEvent;

import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.util.DataUtils;
import com.android.launcher.can.entity.Can40BEntity;

import module.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
* @description: 灯光旋钮状态
* @createDate: 2023/7/24
*/
public class Lin10Receiver extends LinReceiverBase{

    private String dataLength = "02";

    private static final String ID = "010";


    @Override
    protected void disposeData(String data) {
        int index = data.indexOf(ID) + ID.length();
        String d1 = data.substring(index, index + 2);
        String d2 = data.substring(index + 2, index + 4);

        LogUtils.printI(TAG, "data=" + data + ", d1=" + d1 + ", d2=" + d2);

        String binaryD1 = DataUtils.hexStringToBinaryString(d1);

        String binaryD2 = DataUtils.hexStringToBinaryString(d2);

        LogUtils.printI(TAG, "binaryD1=" + binaryD1 + ", binaryD2=" + binaryD2);

        //b7 b6 b5 b4 b3 b2 b1 b0  //标识
        //0  0  0  0  0  0  0  0
        //0  1  2  3  4  5  6  7  角标
        String d1B3 = binaryD1.substring(4,5);
        String d1B4 = binaryD1.substring(3,4);
        String d1B5 = binaryD1.substring(2,3);
        String d1B6 = binaryD1.substring(1,2);
        String d1B7 = binaryD1.substring(0,1);


        String d2B0 = binaryD2.substring(7);
        String d2B1 = binaryD2.substring(6,7);
        String d2B2 = binaryD2.substring(5,6);
        String d2B3 = binaryD2.substring(4,5);
        String d2B4 = binaryD2.substring(3,4);

        BinaryEntity can04BD1Entity = new BinaryEntity();

        if(BinaryEntity.Value.NUM_0.getValue().equals(d1B7)){
            can04BD1Entity.setB0(BinaryEntity.Value.NUM_0);
        }else{
            can04BD1Entity.setB0(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d1B4)){
            can04BD1Entity.setB1(BinaryEntity.Value.NUM_0);
        }else{
            can04BD1Entity.setB1(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d2B1)){
            can04BD1Entity.setB2(BinaryEntity.Value.NUM_0);
        }else{
            can04BD1Entity.setB2(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d1B6)){
            can04BD1Entity.setB3(BinaryEntity.Value.NUM_0);
        }else{
            can04BD1Entity.setB3(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d1B5)){
            can04BD1Entity.setB4(BinaryEntity.Value.NUM_0);
        }else{
            can04BD1Entity.setB4(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d2B0)){
            can04BD1Entity.setB6(BinaryEntity.Value.NUM_0);
        }else{
            can04BD1Entity.setB6(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d2B2)){
            can04BD1Entity.setB7(BinaryEntity.Value.NUM_0);
        }else{
            can04BD1Entity.setB7(BinaryEntity.Value.NUM_1);
        }


        BinaryEntity can04BD2Entity = new BinaryEntity();

        if(BinaryEntity.Value.NUM_0.getValue().equals(d2B3)){
            can04BD2Entity.setB3(BinaryEntity.Value.NUM_0);
        }else{
            can04BD2Entity.setB3(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d2B4)){
            can04BD2Entity.setB7(BinaryEntity.Value.NUM_0);
        }else{
            can04BD2Entity.setB7(BinaryEntity.Value.NUM_1);
        }


        Can40BEntity can40BEntity = new Can40BEntity();
        can40BEntity.setD1(can04BD1Entity);
        can40BEntity.setD2(can04BD2Entity);

        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.LIN10_TO_CAN04B);
        messageEvent.data = can40BEntity;
        EventBus.getDefault().post(messageEvent);
    }

}
