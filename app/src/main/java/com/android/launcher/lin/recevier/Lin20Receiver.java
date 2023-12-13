package com.android.launcher.lin.recevier;

import module.common.MessageEvent;

import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.util.DataUtils;

import module.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @description: 长条板按键状态
 * @createDate: 2023/7/24
 */
public class Lin20Receiver extends LinReceiverBase {

    private String dataLength = "04";
    private static final String ID = "020";


    @Override
    protected void disposeData(String data) {
        int index = data.indexOf(ID) + ID.length();
//            String d1 = data.substring(index, index + 2);
        String d2 = data.substring(index + 2, index + 4);
        String d3 = data.substring(index + 4, index + 6);
        String d4 = data.substring(index + 6);

        LogUtils.printI(TAG, "data=" + data + ", d3=" + d3 + ", d4=" + d4);

        if("01".equals(d2)){
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.CAN015_D1_ADD));
        }else if("FF".equalsIgnoreCase(d2)){
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.CAN015_D1_SUBTRACT));
        }


        String binaryD3 = DataUtils.hexStringToBinaryString(d3);

        String binaryD4 = DataUtils.hexStringToBinaryString(d4);

        LogUtils.printI(TAG, "binaryD3=" + binaryD3 + ", binaryD4=" + binaryD4);

        String d4B0 = binaryD4.substring(binaryD4.length() - 1);
        String d4B2 = binaryD4.substring(binaryD4.length()-2, binaryD4.length() - 1);
        String d4B3 = binaryD4.substring(binaryD4.length()-3, binaryD4.length() - 2);

        String d3B4 = binaryD3.substring(3,4);
        String d3B6 = binaryD3.substring(1,2);

        BinaryEntity can04BD3Entity = new BinaryEntity();

        if(BinaryEntity.Value.NUM_0.getValue().equals(d4B0)){
            can04BD3Entity.setB1(BinaryEntity.Value.NUM_0);
        }else{
            can04BD3Entity.setB1(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d4B2)){
            can04BD3Entity.setB2(BinaryEntity.Value.NUM_0);
        }else{
            can04BD3Entity.setB2(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d4B3)){
            can04BD3Entity.setB3(BinaryEntity.Value.NUM_0);
        }else{
            can04BD3Entity.setB3(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d3B4)){
            can04BD3Entity.setB5(BinaryEntity.Value.NUM_0);
        }else{
            can04BD3Entity.setB5(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d3B6)){
            can04BD3Entity.setB7(BinaryEntity.Value.NUM_0);
        }else{
            can04BD3Entity.setB7(BinaryEntity.Value.NUM_1);
        }


        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.LIN20_TO_CAN04B);
        messageEvent.data = can04BD3Entity;
        EventBus.getDefault().post(messageEvent);
    }
}
