package com.android.launcher.entity;

import android.text.TextUtils;

import com.android.launcher.util.DataUtils;
import module.common.utils.LogUtils;

/**
* @description:
* @createDate: 2023/7/24
*/
public class BinaryEntity {
    private String b0 = "0";
    private String b1 = "0";
    private String b2 = "0";
    private String b3 = "0";
    private String b4 = "0";
    private String b5 = "0";
    private String b6 = "0";
    private String b7 = "0";


    public BinaryEntity(String hexData) {
        try {
            String binaryString = DataUtils.hexStringToBinaryString(hexData);
            if(!TextUtils.isEmpty(binaryString)){
                //b7 b6 b5 b4 b3 b2 b1 b0  //标识
                //0  0  0  0  0  0  0  0
                //0  1  2  3  4  5  6  7  角标
                b7 = binaryString.substring(0,1);
                b6 = binaryString.substring(1,2);
                b5 = binaryString.substring(2,3);
                b4 = binaryString.substring(3,4);
                b3 = binaryString.substring(4,5);
                b2 = binaryString.substring(5,6);
                b1 = binaryString.substring(6,7);
                b0 = binaryString.substring(7);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BinaryEntity() {
    }

    public synchronized String getBinaryData() {
        String data = b7 +
                b6 +
                b5 +
                b4 +
                b3 +
                b2 +
                b1 +
                b0;
        LogUtils.printI(BinaryEntity.class.getSimpleName(), "getData---=" + data);
        return data;
    }

    public String getHexData() {
        try {
            String binaryData = getBinaryData();
            int parseInt = Integer.parseInt(binaryData, 2);
            String hexData = Integer.toHexString(parseInt);
            LogUtils.printI(BinaryEntity.class.getSimpleName(), "hexData---=" + hexData);
            return hexData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "00"; //全开
    }

    public String getB0() {
        return b0;
    }

    public void setB0(Value b0) {
        this.b0 = b0.getValue();
    }

    public String getB1() {
        return b1;
    }

    public void setB1(Value b1) {
        this.b1 = b1.getValue();
    }

    public String getB2() {
        return b2;
    }

    public void setB2(Value b2) {
        this.b2 = b2.getValue();
    }

    public String getB3() {
        return b3;
    }

    public void setB3(Value b3) {
        this.b3 = b3.getValue();
    }

    public String getB4() {
        return b4;
    }

    public void setB4(Value b4) {
        this.b4 = b4.getValue();
    }

    public String getB5() {
        return b5;
    }

    public void setB5(Value b5) {
        this.b5 = b5.getValue();
    }

    public String getB6() {
        return b6;
    }

    public void setB6(Value b6) {
        this.b6 = b6.getValue();
    }

    public String getB7() {
        return b7;
    }

    public void setB7(Value b7) {
        this.b7 = b7.getValue();
    }

    @Override
    public String toString() {
        return getBinaryData() +", "+getHexData();
    }


    public enum Value{
        NUM_0("0"),
        NUM_1("1");

        private String value;

        Value(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
