package com.android.launcher.can.sender;

import com.android.launcher.usbdriver.MUsbReceiver;
import com.android.launcher.util.DataUtils;

import module.common.utils.LogUtils;

/**
 * @description:
 * @createDate: 2023/7/24
 */
public class Can41BSender extends CanSenderBase {

    private String d1 = "FC";
    private String d2 = "1B";
    private String d3 = "3F";
    private String d4 = "FF";
    private String d5 = "FF";
    private String d6 = "FF";
    private String d7 = "FF";
    private String d8 = "FF";

    public Can41BSender() {
        super("08", "41B");
    }

    @Override
    public void send() {
        startTask(0L, 1000L);
    }

    @Override
    protected void execute() {
        String sendData = DATA_HEAD + dataLength + ID + d1 + d2 + d3 + d4 + d5 + d6 + d7 + d8;
        LogUtils.printI(TAG, "sendData=" + sendData + ", length=" + sendData.length());
        MUsbReceiver.write(DataUtils.hexStringToByteArray(sendData.toUpperCase()));
    }

    @Override
    public void updateData(Object o) {

    }

}
