package com.android.launcher.ac.winddirection.rear;

import android.content.Context;

import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.bzf.module_db.entity.CanBCTable;
import com.bzf.module_db.repository.CanBCRepository;

import module.common.utils.AppUtils;
import module.common.utils.LogUtils;

/**
 * 后空调命令
 * @date： 2023/11/2
 * @author: 78495
*/
public class RearAcCmdSender {

    private static final String TAG = RearAcCmdSender.class.getSimpleName();

    private Context context;
    private String deviceId;

    public RearAcCmdSender(Context context) {
        this.context = context;
        deviceId = AppUtils.getDeviceId(context);
    }

    //发送左侧风向命令
    public synchronized void sendLeftWindDirectionCmd(RearWindDirectionCmdValue leftAirflowCmd) {
        CanBCTable canBCTable = CanBCRepository.getInstance().getData(context, deviceId);
        LogUtils.printI(TAG, "changeLeftAirflow-----deviceId=" + deviceId + ", canBCTable=" + canBCTable);
        if (canBCTable != null) {
            String d1 = canBCTable.getLefttemp();
            String d2 = canBCTable.getRighttemp();
            String d3 = canBCTable.getWind();
            String windDirect = canBCTable.getWinddic();
            String d5 = canBCTable.getStatus();
            String d6 = canBCTable.getFrontBackStatus();

            String rightWindDirect = windDirect.substring(0, 1);

            BinaryEntity statusBinary = new BinaryEntity(d5);
            //自动空调状态下，不可调节
            if (isAutoAcStatus(statusBinary)){
                LogUtils.printI(TAG,"sendLeftWindDirectionCmd---自动空调状态下，不可调节");
                return;
            }

            String d4;

            //左侧风向自动状态
            if (leftAirflowCmd == RearWindDirectionCmdValue.AUTO) {
                d4 = windDirect;
                statusBinary.setB3(BinaryEntity.Value.NUM_1);
                d5 = statusBinary.getHexData();
            } else {
                d4 = rightWindDirect + leftAirflowCmd.getValue();
            }
            String cmd = d1 + d2 + d3 + d4 + d5 + d6;

            LogUtils.printI(TAG,"sendLeftWindDirectionCmd---cmd="+cmd);
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.REAR_AC_CONTROL.getTypeValue() + cmd + SerialPortDataFlag.END_FLAG;
            SendHelperLeft.handler(send);
        }
    }


    //右侧风向调整
    public synchronized void sendRightWindDirectionCmd(RearWindDirectionCmdValue rightAirflowCmd) {
        CanBCTable canBCTable = CanBCRepository.getInstance().getData(context, deviceId);
        LogUtils.printI(TAG, "changeLeftAirflow-----deviceId=" + deviceId + ", canBCTable=" + canBCTable);
        if (canBCTable != null) {
            String d1 = canBCTable.getLefttemp();
            String d2 = canBCTable.getRighttemp();
            String d3 = canBCTable.getWind();
            String windDirect = canBCTable.getWinddic();
            String d5 = canBCTable.getStatus();
            String d6 = canBCTable.getFrontBackStatus();

            String leftWindDirect = windDirect.substring(1);

            BinaryEntity statusBinary = new BinaryEntity(d5);
            //自动空调状态下，不可调节
            if (isAutoAcStatus(statusBinary)){
                LogUtils.printI(TAG,"sendLeftWindDirectionCmd---自动空调状态下，不可调节");
                return;
            }

            String d4;

            //左侧风向自动状态
            if (rightAirflowCmd == RearWindDirectionCmdValue.AUTO) {
                d4 = windDirect;
                statusBinary.setB4(BinaryEntity.Value.NUM_1);
                d5 = statusBinary.getHexData();
            } else {
                d4 = rightAirflowCmd.getValue() + leftWindDirect;
            }
            String cmd = d1 + d2 + d3 + d4 + d5 + d6;

            LogUtils.printI(TAG,"sendRightWindDirectionCmd---cmd="+cmd);
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.REAR_AC_CONTROL.getTypeValue() + cmd + SerialPortDataFlag.END_FLAG;
            SendHelperLeft.handler(send);
        }
    }


    //是否处在自动空调状态
    private boolean isAutoAcStatus(BinaryEntity statusBinary) {
        if(BinaryEntity.Value.NUM_1.getValue().equals(statusBinary.getB0())){
            return true;
        }
        return false;
    }

    //发送关闭空调的命令
    public void sendAcOffCmd(boolean acOff) {
        CanBCTable canBCTable = CanBCRepository.getInstance().getData(context, deviceId);
        LogUtils.printI(TAG, "sendAcOffCmd-----deviceId=" + deviceId + ", canBCTable=" + canBCTable);
        if (canBCTable != null) {
            String d1 = canBCTable.getLefttemp();
            String d2 = canBCTable.getRighttemp();
            String d3 = canBCTable.getWind();
            String d4 = canBCTable.getWinddic();
            String d5 = canBCTable.getStatus();
            String d6 = canBCTable.getFrontBackStatus();

            BinaryEntity statusBinary = new BinaryEntity(d5);
            if(acOff){
                //关闭空调
                statusBinary.setB5(BinaryEntity.Value.NUM_1);
            }else{
                //打开空调
                statusBinary.setB5(BinaryEntity.Value.NUM_0);
            }

            String cmd = d1 + d2 + d3 + d4 + statusBinary.getHexData() + d6;

            LogUtils.printI(TAG,"sendAcOffCmd---cmd="+cmd);
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.REAR_AC_CONTROL.getTypeValue() + cmd + SerialPortDataFlag.END_FLAG;
            SendHelperLeft.handler(send);
        }
    }

    //设置自动空调
    public void sendAutoModeCmd(boolean isOpen) {
        CanBCTable canBCTable = CanBCRepository.getInstance().getData(context, deviceId);
        LogUtils.printI(TAG, "sendAutoModeCmd-----deviceId=" + deviceId + ", canBCTable=" + canBCTable);
        if (canBCTable != null) {
            String d1 = canBCTable.getLefttemp();
            String d2 = canBCTable.getRighttemp();
            String d3 = canBCTable.getWind();
            String d4 = canBCTable.getWinddic();
            String d5 = canBCTable.getStatus();
            String d6 = canBCTable.getFrontBackStatus();

            BinaryEntity statusBinary = new BinaryEntity(d5);
            if(isOpen){
                //打开自动模式
                statusBinary.setB0(BinaryEntity.Value.NUM_1);
            }else{
                //关闭自动模式
                statusBinary.setB0(BinaryEntity.Value.NUM_0);
            }

            String cmd = d1 + d2 + d3 + d4 + statusBinary.getHexData() + d6;

            LogUtils.printI(TAG,"sendAutoModeCmd---cmd="+cmd);
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.REAR_AC_CONTROL.getTypeValue() + cmd + SerialPortDataFlag.END_FLAG;
            SendHelperLeft.handler(send);
        }
    }

    //设置风速
    public void sendSetWindCmd(String wind) {
        CanBCTable canBCTable = CanBCRepository.getInstance().getData(context, deviceId);
        LogUtils.printI(TAG, "sendSetLeftWindCmd-----deviceId=" + deviceId + ", canBCTable=" + canBCTable);
        if (canBCTable != null) {
            String d1 = canBCTable.getLefttemp();
            String d2 = canBCTable.getRighttemp();
            String d4 = canBCTable.getWinddic();
            String d5 = canBCTable.getStatus();
            String d6 = canBCTable.getFrontBackStatus();


            String cmd = d1 + d2 + wind + d4 + d5 + d6;

            LogUtils.printI(TAG,"sendSetLeftWindCmd---cmd="+cmd);
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.REAR_AC_CONTROL.getTypeValue() + cmd + SerialPortDataFlag.END_FLAG;
            SendHelperLeft.handler(send);
        }
    }

    //设置左侧温度
    public void sendSetLeftTempCmd(String temp) {
        CanBCTable canBCTable = CanBCRepository.getInstance().getData(context, deviceId);
        LogUtils.printI(TAG, "sendSetLeftTempCmd-----deviceId=" + deviceId + ", canBCTable=" + canBCTable);
        if (canBCTable != null) {
            String d1 = temp;
            String d2 = canBCTable.getRighttemp();
            String d3 = canBCTable.getWind();
            String d4 = canBCTable.getWinddic();
            String d5 = canBCTable.getStatus();
            String d6 = canBCTable.getFrontBackStatus();


            String cmd = d1 + d2 + d3 + d4 + d5 + d6;

            LogUtils.printI(TAG,"sendSetLeftTempCmd---cmd="+cmd);
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.REAR_AC_CONTROL.getTypeValue() + cmd + SerialPortDataFlag.END_FLAG;
            SendHelperLeft.handler(send);
        }
    }

    //设置右侧温度
    public void sendSetRightTempCmd(String temp) {
        CanBCTable canBCTable = CanBCRepository.getInstance().getData(context, deviceId);
        LogUtils.printI(TAG, "sendSetRightTempCmd-----deviceId=" + deviceId + ", canBCTable=" + canBCTable);
        if (canBCTable != null) {
            String d1 = canBCTable.getLefttemp();
            String d2 = temp;
            String d3 = canBCTable.getWind();
            String d4 = canBCTable.getWinddic();
            String d5 = canBCTable.getStatus();
            String d6 = canBCTable.getFrontBackStatus();


            String cmd = d1 + d2 + d3 + d4 + d5 + d6;

            LogUtils.printI(TAG,"sendSetRightTempCmd---cmd="+cmd);
            String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.REAR_AC_CONTROL.getTypeValue() + cmd + SerialPortDataFlag.END_FLAG;
            SendHelperLeft.handler(send);
        }
    }
}
