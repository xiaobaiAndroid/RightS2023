package com.android.launcher.can.sender;

import com.android.launcher.usbdriver.MUsbReceiver;
import com.android.launcher.util.DataUtils;
import com.android.launcher.can.entity.Can015Entity;

import module.common.utils.LogUtils;

/**
 * @description: 设置的亮度
 * @createDate: 2023/7/24
 */
public class Can015Sender extends CanSenderBase<Can015Entity> {

    private volatile String d1 = "00";
    private volatile String d2 = "64";
    private volatile String d3 = "00";
    private volatile String d4 = "64";

    private long updateTime = 0;

    public Can015Sender() {
        super("04", "015");
    }

    @Override
    public void send() {
        startTask(0L,200L);
    }

    @Override
    protected void execute() {
        String sendData = DATA_HEAD + dataLength + ID + d1 + d2 + d3 + d4;
        LogUtils.printI(TAG, "sendData=" + sendData + ", length=" + sendData.length());
        MUsbReceiver.write(DataUtils.hexStringToByteArray(sendData.toUpperCase()));
    }


    @Override
    public void updateData(Can015Entity entity) {
        try {
            updateTime = System.currentTimeMillis();

            d1 = entity.getD1().getHexData();
            d2 = entity.getD2().getHexData();
            d3 = entity.getD3().getHexData();
            d4 = entity.getD4().getHexData();

            LogUtils.printI(TAG, "updateData----d1=" + d1 + ", d2=" + d2 + ", d3=" + d3 + ", d4=" + d4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        super.release();
        updateTime = 0;
    }

    /**
     * @description: 设置 D1增加一档
     * @createDate: 2023/7/25
     */
    public void setD1Add() {
        long currentTime = System.currentTimeMillis();

        long interval = currentTime - updateTime;

        LogUtils.printI(TAG, "setD1Add--------updateTime=" + updateTime + ", interval=" + interval + ", d1=" + d1);

        if (updateTime != 0 && interval < 200) { //小于200毫秒，认为是同时发生
            if (GradeType.GRADE_NONE.getValue().equalsIgnoreCase(d1)) {
                d1 = GradeType.GRADE_1.getValue();
            } else if (GradeType.GRADE_1.getValue().equalsIgnoreCase(d1)) {
                d1 = GradeType.GRADE_2.getValue();
            } else if (GradeType.GRADE_2.getValue().equalsIgnoreCase(d1)) {
                d1 = GradeType.GRADE_3.getValue();
            } else if (GradeType.GRADE_3.getValue().equalsIgnoreCase(d1)) {
                d1 = GradeType.GRADE_4.getValue();
            } else if (GradeType.GRADE_4.getValue().equalsIgnoreCase(d1)) {
                d1 = GradeType.GRADE_5.getValue();
            } else if (GradeType.GRADE_5.getValue().equalsIgnoreCase(d1)) {
                d1 = GradeType.GRADE_6.getValue();
            } else {
                d1 = GradeType.GRADE_6.getValue();
            }
            LogUtils.printI(TAG, "setD1Add------update--d1=" + d1);
        }
    }

    /**
     * @description: 设置 D1减一档
     * @createDate: 2023/7/25
     */
    public void setD1Subtract() {
        long currentTime = System.currentTimeMillis();

        long interval = currentTime - updateTime;

        LogUtils.printI(TAG, "setD1Subtract--------updateTime=" + updateTime + ", interval=" + interval + ", d1=" + d1);

        if (updateTime != 0 && interval < 200) { //小于200毫秒，认为是同时发生
            if (GradeType.GRADE_1.getValue().equalsIgnoreCase(d1)) {
                d1 = GradeType.GRADE_NONE.getValue();
            } else if (GradeType.GRADE_2.getValue().equalsIgnoreCase(d1)) {
                d1 = GradeType.GRADE_1.getValue();
            } else if (GradeType.GRADE_3.getValue().equalsIgnoreCase(d1)) {
                d1 = GradeType.GRADE_2.getValue();
            } else if (GradeType.GRADE_4.getValue().equalsIgnoreCase(d1)) {
                d1 = GradeType.GRADE_3.getValue();
            } else if (GradeType.GRADE_5.getValue().equalsIgnoreCase(d1)) {
                d1 = GradeType.GRADE_4.getValue();
            }  else if (GradeType.GRADE_6.getValue().equalsIgnoreCase(d1)) {
                d1 = GradeType.GRADE_5.getValue();
            }else {
                d1 = GradeType.GRADE_NONE.getValue();
            }
            LogUtils.printI(TAG, "setD1Subtract------update--d1=" + d1);
        }
    }


    public enum GradeType {
        GRADE_NONE("00"),
        GRADE_1("07"),
        GRADE_2("14"),
        GRADE_3("28"),
        GRADE_4("3C"),
        GRADE_5("50"),
        GRADE_6("64");

        private String value;

        public String getValue() {
            return value;
        }

        GradeType(String value) {
            this.value = value;
        }


    }
}
