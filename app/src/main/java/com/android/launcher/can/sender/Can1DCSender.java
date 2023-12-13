package com.android.launcher.can.sender;

import android.text.TextUtils;

import com.android.launcher.airsystem.AirSystemProtocolFlags;
import com.android.launcher.can.parser.Can20bDataParser;
import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.bzf.module_db.entity.Can20BTable;

import module.common.utils.LogUtils;

/**
 * @description: 空调按键
 * @createDate: 2023/7/24
 */
public class Can1DCSender extends CanSenderBase<Can20BTable> {

    private String d1 = "00";
    private String d2 = "00";
    private String d3 = "00";
    private BinaryEntity d4 = new BinaryEntity("00");
    private BinaryEntity d5 = new BinaryEntity("00");
    private volatile BinaryEntity d6 = new BinaryEntity("00");


    public Can1DCSender() {
        super("06", "1DC");
    }

    @Override
    public void send() {
        startTask(0L, 1000L);
    }

    @Override
    protected void execute() {
//        String sendData = DATA_HEAD + dataLength + ID + d1 +d2 +d3 + d4.getHexData() +d5.getHexData() + d6.getHexData();
//        LogUtils.printI(TAG, "sendData=" + sendData + ", length=" + sendData.length());
//        MUsbReceiver.write(DataUtils.hexStringToByteArray(sendData.toUpperCase()));

        String sendData = SerialPortDataFlag.START_FLAG +SerialPortDataFlag.CAN_1DC.getTypeValue() + d1 + d2 + d3 + d4.getHexData() + d5.getHexData() + d6.getHexData();
        LogUtils.printI(TAG, "sendData=" + sendData + ", length=" + sendData.length());
        SendHelperLeft.handler(sendData.toUpperCase());
    }

    @Override
    public void updateData(Can20BTable table) {

        d1 = table.getDriverTemp();
        d2 = table.getFrontSeatTemp();
        d3 = table.getDriverWind();
        d4 = new BinaryEntity(table.getFrontSeatWind());

        BinaryEntity can20bD5 = new BinaryEntity(table.getAcKeyStatus());

        Can20bDataParser can20bDataParser = new Can20bDataParser(table);
        //主驾自动
        setDriverAutoStatus(can20bD5.getB0());
        //副驾自动开关状态
        setFrontSeatAutoStatus(can20bD5.getB1());
        setFrontDemistOff(can20bDataParser.frontDemistIsOpen());
        setInnerLoopOffStatus(can20bD5.getB4());
        setAcOffStatus(can20bD5.getB5());

        if (!isInit) {
            isInit = true;
        }
    }

    /**
     * @description: 没有按键按下时
     * @createDate: 2023/7/24
     */
    private void setNotKeyDownData() {
        d1 = "";
    }

    /*设置主驾风速增大*/
    public void setDriverWindAdd(String value) {
        LogUtils.printI(TAG, "setDriverWindAdd---value=" + value);
        String data = analysisData(value);
        if (data != null) {
            d3 = data;
            setDriverWindAddStatus("1");
        }
        new Thread(() -> {
            String send = "AABB" + AirSystemProtocolFlags.WIND_MAIN_DRIVER + value + "CCDD";
            SendHelperLeft.handler(send);
        }).start();
    }

    public void setDriverWindAddStatus(String status) {
        if ("1".equals(status)) {
            d6.setB2(BinaryEntity.Value.NUM_1);
        } else {
            d6.setB2(BinaryEntity.Value.NUM_0);
        }
    }

    public void setDriverWindSubtract(String value) {
        LogUtils.printI(TAG, "setDriverWindSubtract---value=" + value);
        String data = analysisData(value);
        if (data != null) {
            d3 = data;
            setDriverWindSubtractStatus("1");
        }

        new Thread(() -> {
            String send = "AABB" + AirSystemProtocolFlags.WIND_MAIN_DRIVER + value + "CCDD";
            SendHelperLeft.handler(send);
        }).start();
    }

    public void setDriverWindSubtractStatus(String status) {
        if ("1".equals(status)) {
            d6.setB3(BinaryEntity.Value.NUM_1);
        } else {
            d6.setB3(BinaryEntity.Value.NUM_0);
        }
    }

    /*设置副驾风速增大*/
    public void setFrontSeatWindAdd(String value) {
        LogUtils.printI(TAG, "setFrontSeatWindAdd---value=" + value);
        String data = analysisData(value);
        if (data != null) {
            d4 = new BinaryEntity(data);
            setFrontSeatWindAddStatus("1");
        }

        new Thread(() -> {
            String send = "AABB" + AirSystemProtocolFlags.WIND_COPILOT + value + "CCDD";
            SendHelperLeft.handler(send);
        }).start();
    }

    public void setFrontSeatWindAddStatus(String status) {
        if ("1".equals(status)) {
            d6.setB6(BinaryEntity.Value.NUM_1);
        } else {
            d6.setB6(BinaryEntity.Value.NUM_0);
        }
    }

    public void setFrontSeatWindSubtract(String value) {
        LogUtils.printI(TAG, "setFrontSeatWindSubtract---value=" + value);
        String data = analysisData(value);
        if (data != null) {
            d4 = new BinaryEntity(data);
            setFrontSeatWindSubtractStatus("1");
        }
        new Thread(() -> {
            String send = "AABB" + AirSystemProtocolFlags.WIND_COPILOT + value + "CCDD";
            SendHelperLeft.handler(send);
        }).start();

    }


    public void setFrontSeatWindSubtractStatus(String status) {
        if ("1".equals(status)) {
            d6.setB7(BinaryEntity.Value.NUM_1);
        } else {
            d6.setB7(BinaryEntity.Value.NUM_0);
        }
    }

    public String analysisData(String data) {
        if (!TextUtils.isEmpty(data) && data.length() == 2) {
            return data;
        }
        return null;
    }


    public void setDriverTempAdd(String value) {
        LogUtils.printI(TAG, "setDriverTempAdd---value=" + value);
        String data = analysisData(value);
        if (data != null) {
            d1 = data;
            setDriverTempAddStatus("1");
        }
        new Thread(() -> {
            String send = "AABB" + AirSystemProtocolFlags.TEMP_MAIN_DRIVER + value + "CCDD";
            SendHelperLeft.handler(send);
        }).start();
    }

    public void setDriverTempAddStatus(String status) {
        if ("1".equals(status)) {
            d6.setB0(BinaryEntity.Value.NUM_1);
        } else {
            d6.setB0(BinaryEntity.Value.NUM_0);
        }
    }


    public void setDriverTempSubtract(String value) {
        LogUtils.printI(TAG, "setDriverTempSubtract---value=" + value);
        String data = analysisData(value);


        if (data != null) {
            d1 = data;
            setDriverTempSubtractStatus("1");
        }

        new Thread(() -> {
            String send = "AABB" + AirSystemProtocolFlags.TEMP_MAIN_DRIVER + value + "CCDD";
            SendHelperLeft.handler(send);
        }).start();
    }

    public void setDriverTempSubtractStatus(String status) {
        if ("1".equals(status)) {
            d6.setB1(BinaryEntity.Value.NUM_1);
        } else {
            d6.setB1(BinaryEntity.Value.NUM_0);
        }
    }

    //副驾温度增加
    public void setFrontSeatTempAdd(String value) {
        LogUtils.printI(TAG, "setFrontSeatTempAdd---value=" + value);
        String data = analysisData(value);
        if (data != null) {
            d2 = data;
            setFrontSeatTempAddStatus("1");
        }
        new Thread(() -> {
            String send = "AABB" + AirSystemProtocolFlags.TEMP_COPILOT + value + "CCDD";
            SendHelperLeft.handler(send);
        }).start();
    }

    //设置副驾温度增加开关状态
    public void setFrontSeatTempAddStatus(String status) {
        if ("1".equals(status)) {
            d6.setB4(BinaryEntity.Value.NUM_1);
        } else {
            d6.setB4(BinaryEntity.Value.NUM_0);
        }
    }

    public void setFrontSeatTempSubtract(String value) {
        LogUtils.printI(TAG, "setFrontSeatTempSubtract---value=" + value);
        String data = analysisData(value);
        if (data != null) {
            d2 = data;
            setFrontSeatTempSubtractStatus("1");
        }
        new Thread(() -> {
            String send = "AABB" + AirSystemProtocolFlags.TEMP_COPILOT + value + "CCDD";
            SendHelperLeft.handler(send);
        }).start();
    }

    //设置副驾温度减小开关状态
    public void setFrontSeatTempSubtractStatus(String status) {
        if ("1".equals(status)) {
            d6.setB5(BinaryEntity.Value.NUM_1);
        } else {
            d6.setB5(BinaryEntity.Value.NUM_0);
        }
    }


    //设置前挡风除雾开关状态
    public void setFrontDemistOff(boolean frontDemistStatus) {
        String value;
        if (frontDemistStatus) {
            value = BinaryEntity.Value.NUM_1.getValue();
            d5.setB2(BinaryEntity.Value.NUM_1);
        } else {
            value = BinaryEntity.Value.NUM_0.getValue();
            d5.setB2(BinaryEntity.Value.NUM_0);
        }


        LogUtils.printI(TAG, "setFrontDemistStatus---d5=" + d5 + ", value="+value);

    }


    //设置空调开关
    public void setAcOff(boolean isOff) {
        String value;
        if (isOff) {
            value = BinaryEntity.Value.NUM_0.getValue();
            setAcOffStatus(value);
        } else {
            value = BinaryEntity.Value.NUM_1.getValue();
            setAcOffStatus(value);
        }

        LogUtils.printI(TAG, "setAcOff---d5=" + d5 + ", value="+value +",isOff="+isOff);

        new Thread(() -> {
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.AC_OFF.getTypeValue() + value + SerialPortDataFlag.END_FLAG;
            LogUtils.printI(TAG, "setAcOff---send=" + send);
            SendHelperLeft.handler(send);
        }).start();
    }

    //设置空调开关状态
    public void setAcOffStatus(String status) {
        if ("1".equals(status)) {
            d5.setB5(BinaryEntity.Value.NUM_1);
        } else {
            d5.setB5(BinaryEntity.Value.NUM_0);
        }
    }


    //设置内循环开关
    public void setInnerLoopOff(boolean isOff) {
        if (isOff) {
            setInnerLoopOffStatus("0");
        } else {
            setInnerLoopOffStatus("1");
        }
        LogUtils.printI(TAG, "setInnerLoopOff---d5=" + d5);
    }

    //设置内循环开关状态
    public void setInnerLoopOffStatus(String status) {
        if ("1".equals(status)) {
            d5.setB4(BinaryEntity.Value.NUM_1);
        } else {
            d5.setB4(BinaryEntity.Value.NUM_0);
        }
    }


    //设置主驾自动模式开关
    public void setDriverAutoOff(boolean isOff) {
        String value;
        if (isOff) {
            value = BinaryEntity.Value.NUM_0.getValue();
            setDriverAutoStatus(value);
        } else {
            value = BinaryEntity.Value.NUM_1.getValue();
            setDriverAutoStatus(value);
        }
        LogUtils.printI(TAG, "setDriverAutoOff---d5=" + d5);

        new Thread(() -> {
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.DRIVER_AUTO.getTypeValue() + value + SerialPortDataFlag.END_FLAG;
            LogUtils.printI(TAG, "setDriverAutoOff---send=" + send);
            SendHelperLeft.handler(send);
        }).start();

    }

    private void setDriverAutoStatus(String status) {
        if ("1".equals(status)) {
            d5.setB0(BinaryEntity.Value.NUM_1);
        } else {
            d5.setB0(BinaryEntity.Value.NUM_0);
        }
    }

    //设置副驾自动模式开关
    public void setFrontSeatAutoOff(boolean isOff) {
        String value;
        if (isOff) {
            value = BinaryEntity.Value.NUM_0.getValue();
            setFrontSeatAutoStatus(value);
        } else {
            value = BinaryEntity.Value.NUM_1.getValue();
            setFrontSeatAutoStatus(value);
        }

        new Thread(() -> {
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.FRONT_SEAT_AUTO.getTypeValue() + value + SerialPortDataFlag.END_FLAG;
            LogUtils.printI(TAG, "setDriverAutoOff---send=" + send);
            SendHelperLeft.handler(send);
        }).start();
        LogUtils.printI(TAG, "setFrontSeatAutoOff---d5=" + d5);
    }

    private void setFrontSeatAutoStatus(String status) {
        if ("1".equals(status)) {
            d5.setB1(BinaryEntity.Value.NUM_1);
        } else {
            d5.setB1(BinaryEntity.Value.NUM_0);
        }
    }

    //设置自动空调
    public void setAutoAcStatus(boolean auto) {
        String value;
        if (auto) {
            value = BinaryEntity.Value.NUM_1.getValue();
            setFrontSeatAutoStatus(value);
        } else {
            value = BinaryEntity.Value.NUM_0.getValue();
            setFrontSeatAutoStatus(value);
        }

        new Thread(() -> {
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.SET_AUTO_AC.getTypeValue() + value + SerialPortDataFlag.END_FLAG;
            LogUtils.printI(TAG, "setAutoAcStatus---send=" + send);
            SendHelperLeft.handler(send);
        }).start();
        LogUtils.printI(TAG, "setAutoAcStatus---d5=" + d5);
    }

    //设置空调压缩机开关
    public void setCompressorStatus(boolean compressor) {
        String value;
        if (compressor) { //开启
            value = BinaryEntity.Value.NUM_0.getValue();
        } else {
            value = BinaryEntity.Value.NUM_1.getValue();
        }

        new Thread(() -> {
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.SET_AC_COMPRESSOR_STATUS.getTypeValue() + value + SerialPortDataFlag.END_FLAG;
            LogUtils.printI(TAG, "setCompressorStatus---send=" + send);
            SendHelperLeft.handler(send);
        }).start();
    }


}
