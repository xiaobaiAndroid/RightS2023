package com.android.launcher.ac.winddirection.front;

import android.content.Context;

import com.android.launcher.ac.airflow.AirflowPatternCmdValue;
import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;

import module.common.utils.AppUtils;
import module.common.utils.LogUtils;

/**
 * 后空调命令
 *
 * @date： 2023/11/2
 * @author: 78495
 */
public class FrontAcCmdSender {

    private static final String TAG = FrontAcCmdSender.class.getSimpleName();

    private Context context;
    private String deviceId;

    public FrontAcCmdSender(Context context) {
        this.context = context;
        deviceId = AppUtils.getDeviceId(context);
    }


    //设置主驾风向
    public synchronized void setLeftWindDirection(FrontWindDirectionCmdValue cmdValue) {
        String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.FRONT_LEFT_WINDDIRE.getTypeValue() + cmdValue.getValue() + SerialPortDataFlag.END_FLAG;
        LogUtils.printI(TAG, "setLeftWindDirection---send" + send);
        SendHelperLeft.handler(send);
    }

    public  void setRightWindDirection(FrontWindDirectionCmdValue cmdValue) {
        String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.FRONT_RIGHT_WINDDIRE.getTypeValue() + cmdValue.getValue() + SerialPortDataFlag.END_FLAG;
        LogUtils.printI(TAG, "setRightWindDirection---send" + send);
        SendHelperLeft.handler(send);
    }

    //设置压缩机
    public  void setCompressorOff(boolean compressorOff) {
        if (compressorOff) {
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.COMPRESSOR_OFF.getTypeValue()+"01"+SerialPortDataFlag.END_FLAG;
            SendHelperLeft.handler(send.toUpperCase());
        } else {
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.COMPRESSOR_OFF.getTypeValue()+"00"+SerialPortDataFlag.END_FLAG;
            SendHelperLeft.handler(send.toUpperCase());
        }
    }

    //设置驾驶员风向自动
    public void setLeftWindDirectionAuto(boolean isAuto) {

        String send;
        if (isAuto) {
            send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.SET_DRIVER_WINDDIR_AUTO.getTypeValue()+ BinaryEntity.Value.NUM_1.getValue() +SerialPortDataFlag.END_FLAG;
        } else {
            send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.SET_DRIVER_WINDDIR_AUTO.getTypeValue()+BinaryEntity.Value.NUM_0.getValue()+SerialPortDataFlag.END_FLAG;
        }
        SendHelperLeft.handler(send.toUpperCase());
        LogUtils.printI(TAG, "setLeftWindDirectionAuto---send=" + send);
    }

    //设置副驾风向自动
    public void setRightWindDirectionAuto(boolean isAuto) {
        String send;
        if (isAuto) {
            send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.SET_FRONT_SEAT_WINDDIR_AUTO.getTypeValue()+ BinaryEntity.Value.NUM_1.getValue() +SerialPortDataFlag.END_FLAG;
        } else {
            send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.SET_FRONT_SEAT_WINDDIR_AUTO.getTypeValue()+BinaryEntity.Value.NUM_0.getValue()+SerialPortDataFlag.END_FLAG;
        }
        SendHelperLeft.handler(send.toUpperCase());
        LogUtils.printI(TAG, "setRightWindDirectionAuto---send=" + send);
    }

    //设置主驾风速自动
    public void setDriverAuto(boolean isAuto) {
        String send;
        if (isAuto) {
            send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.SET_DRIVER_WIND_AUTO.getTypeValue()+ BinaryEntity.Value.NUM_1.getValue() +SerialPortDataFlag.END_FLAG;
        } else {
            send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.SET_DRIVER_WIND_AUTO.getTypeValue()+BinaryEntity.Value.NUM_0.getValue()+SerialPortDataFlag.END_FLAG;
        }
        SendHelperLeft.handler(send.toUpperCase());
        LogUtils.printI(TAG, "setDriverAuto---send=" + send);
    }

    //设置副驾风速自动
    public void setFrontSeatAuto(boolean isAuto) {
        String send;
        if (isAuto) {
            send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.SET_FRONT_SEAT_WIND_AUTO.getTypeValue()+ BinaryEntity.Value.NUM_1.getValue() +SerialPortDataFlag.END_FLAG;
        } else {
            send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.SET_FRONT_SEAT_WIND_AUTO.getTypeValue()+BinaryEntity.Value.NUM_0.getValue()+SerialPortDataFlag.END_FLAG;
        }
        SendHelperLeft.handler(send.toUpperCase());
        LogUtils.printI(TAG, "setDriverAuto---send=" + send);
    }

    //设置气流模式
    public void setAirflowPattern(AirflowPatternCmdValue cmdValue) {
        String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.SET_AIRFLOW_PATTERN.getTypeValue()+ cmdValue.getValue() +SerialPortDataFlag.END_FLAG;
        SendHelperLeft.handler(send.toUpperCase());
        LogUtils.printI(TAG, "setAirflowPattern---send=" + send);
    }
}
