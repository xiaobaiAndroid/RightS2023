package com.android.launcher.can.sender;

import android.text.TextUtils;

import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.usbdriver.MUsbReceiver;
import com.android.launcher.util.DataUtils;
import com.android.launcher.can.entity.Can40BEntity;

import module.common.utils.LogUtils;

/**
 * @description:
 * @createDate: 2023/7/24
 */
public class Can04BSender extends CanSenderBase<Can40BEntity> {

    //REST按键状态，余温加热的功能
    private String restStatus = "0";

    //后挡风除雾
    private String demistStatus = "0";

    private String d1 = "00";
    private String d2 = "00";
    private String d3 = "00";
    private BinaryEntity d4 = new BinaryEntity("00");
    private BinaryEntity d5 = new BinaryEntity("00");

    public Can04BSender() {
        super("05", "04B");
    }

    @Override
    public void send() {
        startTask(0L,100L);
    }

    @Override
    protected void execute() {
        String sendData = DATA_HEAD + dataLength + ID + d1 + d2 + d3 + d4.getHexData() + d5.getHexData();
        LogUtils.printI(TAG, "sendData=" + sendData + ", length=" + sendData.length());
        MUsbReceiver.write(DataUtils.hexStringToByteArray(sendData.toUpperCase()));
    }

    @Override
    public void updateData(Can40BEntity entity) {
        if (entity != null) {
            String newD1 = analysisData(entity.getD1());
            if(newD1!=null){
                d1 = newD1;
            }
            String newD2 = analysisData(entity.getD2());
            if(newD2!=null){
                d2 = newD2;
            }
            String newD3 = analysisData(entity.getD3());
            if(newD3!=null){
                d3 = newD3;
            }
        }
        if(!isInit){
            isInit = true;
        }
    }

    private String analysisData(BinaryEntity binaryEntity) {
        if (binaryEntity != null) {
            String newData = binaryEntity.getHexData();
            if (!TextUtils.isEmpty(newData) && newData.length() == 2) {
                return newData;
            }
        }
        return null;
    }

    //设置后挡风除雾开关
    public void setRearDemistOff(boolean isOff){
        if(isOff){
            setRearDemist("0");
        }else{
            setRearDemist("1");
        }
        LogUtils.printI(TAG, "setRearDemistOff---d4="+d4);
    }

    public void setRearDemist(String status){
        if("1".equals(status)){
            d4.setB5(BinaryEntity.Value.NUM_1);
        }else{
            d4.setB5(BinaryEntity.Value.NUM_0);
        }
    }


    //设置空调REST开关
    public void setAcRestOff(boolean isOff){
        if(isOff){
            setAcRest("0");
        }else{
            setAcRest("1");
        }
        LogUtils.printI(TAG, "setAcRestOff---d5="+d5);
    }

    public void setAcRest(String status){
        if("1".equals(status)){
            d5.setB5(BinaryEntity.Value.NUM_1);
        }else{
            d5.setB5(BinaryEntity.Value.NUM_0);
        }
    }
}
