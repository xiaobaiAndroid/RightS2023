package com.android.launcher.serialport.sender;

import android.provider.Settings;

import com.android.launcher.MyApp;
import com.android.launcher.can.status.Can35dStatus;
import com.android.launcher.can.status.RearHatchCoverStatus;
import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.serialport.ttl.SerialHelperttlLd;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.bzf.module_db.entity.CarSetupTable;
import com.bzf.module_db.repository.CarSetupRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import module.common.utils.LogUtils;

/**
 * @date： 2023/11/13
 * @author: 78495
*/
public class Can35DConfigSender {

    private ExecutorService executorService;

    public Can35DConfigSender() {
        executorService =  Executors.newSingleThreadExecutor();
    }

    public void start(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    String deviceId = "";
                    try {
                        deviceId = Settings.System.getString(MyApp.getGlobalContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(MyApp.getGlobalContext(), deviceId);
                    if (carSetupTable != null) {
                        //自动折耳
                        BinaryEntity binaryEntity = new BinaryEntity();
                        if(carSetupTable.isAutomaticRarFolding()){
                            binaryEntity.setB2(BinaryEntity.Value.NUM_1);
                        }else {
                            binaryEntity.setB2(BinaryEntity.Value.NUM_0);
                        }

                        if( carSetupTable.isRearviewMirrorDown()){
                            binaryEntity.setB0(BinaryEntity.Value.NUM_1);
                        }else{
                            binaryEntity.setB0(BinaryEntity.Value.NUM_0);
                        }

                        String can35D5Left;
                        //后舱盖高度限制 开启
                        if(carSetupTable.isTrunkOpenLimit()){
                            can35D5Left = RearHatchCoverStatus.STATE_OPEN.getValue();
                        }else{
                            can35D5Left = RearHatchCoverStatus.STATE_CLOSE.getValue();
                        }

                        String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.AUTOMATIC_CLOSING.getTypeValue() + can35D5Left + binaryEntity.getHexData() + SerialPortDataFlag.END_FLAG;

                        SendHelperLeft.handler(send);


                        if (carSetupTable.isCenterDoorLock()) { //自动落锁
                            send = "AABB2401CCDD";
                        } else {
                            send = "AABB2400CCDD";
                        }
                        SendHelperLeft.handler(send);

                        send =  "AABB27"+carSetupTable.getInnerLighting() + "CCDD";
                        SendHelperLeft.handler(send);

                        send =  "AABB28"+carSetupTable.getExternalLighting() + "CCDD";
                        SendHelperLeft.handler(send);

                        send =  "AABB29"+carSetupTable.getAmbientLighting() + "CCDD";
                        SendHelperLeft.handler(send);

                        String status;
                        if (carSetupTable.isSteeringColumnAssist() && carSetupTable.isSeatAssist()) { //上下车辅助
                            status = "11";
                        } else if (carSetupTable.isSteeringColumnAssist()){
                            status = "01";
                        } else if (carSetupTable.isSeatAssist()){
                            status = "10";
                        }
                        else{
                            status = "00";
                        }

                        send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.IN_AND_OUT_ASSIST.getTypeValue() + status + SerialPortDataFlag.END_FLAG;

                        LogUtils.printI(Can35DConfigSender.class.getSimpleName(),"send="+send);
                        SendHelperLeft.handler(send);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
