package com.android.launcher.can.sender;

import com.android.launcher.usbdriver.MUsbReceiver;
import com.android.launcher.util.DataUtils;

import module.common.utils.LogUtils;

/**
 * @description:
 * @createDate: 2023/7/24
 */
public class Can5DBSender extends CanSenderBase<Object> {

    private String d1 = "10";
    private String d2 = "12";
    private String d3 = "00";
    private String d4 = "00";
    private String d5 = "00";
    private String d6 = "00";
    private String d7 = "00";
    private String d8 = "00";

    public Can5DBSender() {
        super("08", "5DB");
    }

    @Override
    public void send() {

        new Thread(() -> {
            try {
                String sendData = DATA_HEAD + dataLength + ID + d1 + d2 + d3 + d4 + d5 + d6 + d7 + d8;
                LogUtils.printI(TAG, "sendData=" + sendData + ", length=" + sendData.length());
                MUsbReceiver.write(DataUtils.hexStringToByteArray(sendData.toUpperCase()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    protected void execute() {

    }

    @Override
    public void updateData(Object o) {

    }


}
