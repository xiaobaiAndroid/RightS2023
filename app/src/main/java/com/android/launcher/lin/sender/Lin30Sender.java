package com.android.launcher.lin.sender;

import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.usbdriver.MUsbReceiver;
import com.android.launcher.util.DataUtils;
import com.android.launcher.lin.entity.Lin30Entity;

import module.common.utils.LogUtils;

/**
 * @description:
 * @createDate: 2023/7/24
 */
public class Lin30Sender extends LinSenderBase {

    private String dataLength = "08";

    private static final String ID = "030";

    private volatile String d1 = "64";
    private volatile String d2 = "FF";
    private volatile String d3 = "FF";
    private volatile String d4 = "FF";
    private volatile String d5 = "00";
    private volatile String d6 = "00";
    private volatile String d7 = "00";
    private volatile String d8 = "00";

    @Override
    public void send() {
//        isRunnable = true;

        new Thread(() -> {
            try {
                String sendData = DATA_HEAD + dataLength + ID + d1 +d2 +d3 + d4 +d5 + d6 + d7 +d8;
                LogUtils.printI(TAG, "sendData=" + sendData + ", length=" + sendData.length());
                MUsbReceiver.write(DataUtils.hexStringToByteArray(sendData.toUpperCase()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setD6B0B1(BinaryEntity d6Entity) {
        String b0 = d6Entity.getB0();
        String b1 = d6Entity.getB1();

        String d6Binary = DataUtils.hexStringToBinaryString(d6);
        String newD6Binary = d6Binary.substring(d6Binary.length() - 3) + b1 + b0;

        LogUtils.printI(TAG, "d6Binary=" + d6Binary + ", newD6Binary=" + newD6Binary);

        d6 = DataUtils.binaryStringToHexString(newD6Binary);


    }

    public void setD5(BinaryEntity d5Entity) {
        String b5 = d5Entity.getB5();

        BinaryEntity binaryEntity = new BinaryEntity(d5);
        if(BinaryEntity.Value.NUM_0.getValue().equals(b5)){
            binaryEntity.setB5(BinaryEntity.Value.NUM_0);
        }else{
            binaryEntity.setB5(BinaryEntity.Value.NUM_1);
        }

        d5 = binaryEntity.getHexData();

    }

    public void setD6B5(BinaryEntity d6Entity) {
        String b5 = d6Entity.getB5();
        BinaryEntity binaryEntity = new BinaryEntity(d6);
        if(BinaryEntity.Value.NUM_0.getValue().equals(b5)){
            binaryEntity.setB5(BinaryEntity.Value.NUM_0);
        }else{
            binaryEntity.setB5(BinaryEntity.Value.NUM_1);
        }
    }

    public void setD1(BinaryEntity d1Entity) {
        d1 = d1Entity.getHexData();
    }

    public void setD6(BinaryEntity d6Entity) {
        BinaryEntity binaryEntity = new BinaryEntity(d6);

        if(BinaryEntity.Value.NUM_0.getValue().equals(d6Entity.getB4())){
            binaryEntity.setB4(BinaryEntity.Value.NUM_0);
        }else{
            binaryEntity.setB4(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d6Entity.getB6())){
            binaryEntity.setB6(BinaryEntity.Value.NUM_0);
        }else{
            binaryEntity.setB6(BinaryEntity.Value.NUM_1);
        }

        if(BinaryEntity.Value.NUM_0.getValue().equals(d6Entity.getB7())){
            binaryEntity.setB7(BinaryEntity.Value.NUM_0);
        }else{
            binaryEntity.setB7(BinaryEntity.Value.NUM_1);
        }
        d6 = binaryEntity.getHexData();

    }

    public void updateData(Lin30Entity lin30Entity) {
        d2 = lin30Entity.getD2().getHexData();
        d3 = lin30Entity.getD3().getHexData();
        d4 = lin30Entity.getD4().getHexData();

        BinaryEntity binaryEntity = new BinaryEntity(d5);
        binaryEntity.setB0(BinaryEntity.Value.NUM_1);
        d5 = binaryEntity.getHexData();

    }
}
