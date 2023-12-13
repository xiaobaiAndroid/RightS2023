package com.android.launcher.serialport.sender;

import android.provider.Settings;
import android.text.TextUtils;

import com.android.launcher.MyApp;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.bzf.module_db.entity.Can1E5Table;
import com.bzf.module_db.entity.Can20BTable;
import com.bzf.module_db.entity.CanBBTable;
import com.bzf.module_db.entity.CarSetupTable;
import com.bzf.module_db.repository.Can1E5Repository;
import com.bzf.module_db.repository.Can20BRepository;
import com.bzf.module_db.repository.CanBBRepository;
import com.bzf.module_db.repository.CarSetupRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import module.common.utils.AppUtils;
import module.common.utils.LogUtils;

/**
 * @date： 2023/11/14
 * @author: 78495
 */
public class Can1E5ConfigSender {

    private ExecutorService executorService;

    public Can1E5ConfigSender() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public void start() {
        executorService.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                String deviceId = AppUtils.getDeviceId(MyApp.getGlobalContext());
                Can1E5Table table = Can1E5Repository.getInstance().getData(MyApp.getGlobalContext(), deviceId);
                Can20BTable can20BTable = Can20BRepository.getInstance().getData(MyApp.getGlobalContext(), deviceId);
                CanBBTable canBBTable = CanBBRepository.getInstance().getData(MyApp.getGlobalContext(), deviceId);
                if (table != null) {
                    if (!TextUtils.isEmpty(table.getDriverWind())) {

                        String driverTemp;
                        String frontSeatTemp;
                        String driverWind;
                        String frontSeatWind;
                        if (TextUtils.isEmpty(can20BTable.getDriverTemp())) {
                            driverTemp = table.getDriverTemp();
                            frontSeatTemp = table.getFrontSeatTemp();
                            driverWind = table.getDriverWind();
                            frontSeatWind = table.getFrontSeatWind();

                        } else {
                            driverTemp = can20BTable.getDriverTemp();
                            frontSeatTemp = can20BTable.getFrontSeatTemp();
                            driverWind = can20BTable.getDriverWind();
                            frontSeatWind = can20BTable.getFrontSeatWind();

                        }

                        String windDirection;
                        if(TextUtils.isEmpty(canBBTable.getLeftWindDirection())){
                            windDirection = table.getWindDirection();
                        }else{
                            windDirection = canBBTable.getRightWindDirection() +canBBTable.getLeftWindDirection();
                        }

                        String data = driverTemp
                                + frontSeatTemp
                                + driverWind
                                + frontSeatWind
                                +windDirection
                                +table.getStatus()
                                + table.getAirflowMode()
                                + table.getAudioMode();
                        String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.INIT_CAN1E5_CONFIG.getTypeValue() + data + SerialPortDataFlag.END_FLAG;
                        LogUtils.printI(Can1E5ConfigSender.class.getSimpleName(), "send=" + send);
                        SendHelperLeft.handler(send);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
