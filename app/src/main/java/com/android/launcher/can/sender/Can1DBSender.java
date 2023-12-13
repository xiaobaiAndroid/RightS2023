package com.android.launcher.can.sender;

import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.usbdriver.MUsbReceiver;
import com.android.launcher.util.DataUtils;
import com.android.launcher.can.entity.Can1DBEntity;

import java.util.concurrent.TimeUnit;

import module.common.utils.LogUtils;

/**
* @description: 从can中读取OBC填充1DB，参见前后空调逻辑那一标签页
* @createDate: 2023/7/24
*/
public class Can1DBSender extends CanSenderBase<Can1DBEntity>{

    private String d1 = "FF";
    private String d2 = "FF";
    private String d3 = "FF";
    private String d4 = "FF";
    private String d5 = "00";

    public Can1DBSender() {
        super("05", "1DB");
    }

    @Override
    public void send() {
        startTask(0L,1000L);
    }

    @Override
    protected void execute() {
        String sendData = DATA_HEAD + dataLength + ID + d1 +d2 +d3 + d4 +d5;
        LogUtils.printI(TAG, "sendData=" + sendData + ", length=" + sendData.length());
        MUsbReceiver.write(DataUtils.hexStringToByteArray(sendData.toUpperCase()));
    }


    @Override
    public void updateData(Can1DBEntity entity) {
        d1 = entity.getD1().getHexData();
        d2 = entity.getD2().getHexData();
        d3 = entity.getD3().getHexData();

        BinaryEntity binaryD5 = entity.getD5();
        if("0".equals(binaryD5.getB1())){
            d5 = "00";
        }else{ //b1=1时，并且按下按键

        }
    }
}
