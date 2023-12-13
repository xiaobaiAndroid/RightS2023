package com.android.launcher.can;

import module.common.MessageEvent;

import com.android.launcher.can.entity.Can015Entity;
import com.android.launcher.can.entity.Can1DBEntity;
import com.android.launcher.can.entity.Can40BEntity;
import com.android.launcher.can.sender.Can015Sender;
import com.android.launcher.can.sender.Can04BSender;
import com.android.launcher.can.sender.Can1DBSender;
import com.android.launcher.can.sender.Can1DCSender;
import com.android.launcher.can.sender.Can41BSender;
import com.android.launcher.can.sender.Can5DBSender;
import com.android.launcher.can.sender.CanSenderBase;
import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.util.DataUtils;
import com.bzf.module_db.entity.Can20BTable;

import module.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @description:
* @createDate: 2023/7/24
*/
public class CanSenderManager {

    private static final String TAG = CanSenderManager.class.getSimpleName();

    private CanSenderBase can1DBSender = new Can1DBSender();
    private Can1DCSender can1DCSender = new Can1DCSender();
    private Can04BSender can04BSender = new Can04BSender();
    private Can5DBSender can5DBSender = new Can5DBSender();
    private Can015Sender can015Sender = new Can015Sender();
    private Can41BSender can41BSender = new Can41BSender();

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void start(){
        can1DBSender.send();
        can1DCSender.send();
        can04BSender.send();
        can015Sender.send();
        can41BSender.send();
    }

    public void release(){
        unregisterListener();
        can1DBSender.release();
        can1DCSender.release();
        can04BSender.release();
        can5DBSender.release();
        can015Sender.release();
        can41BSender.release();

        try {
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void registerListener() {
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregisterListener() {
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        try {
            if(event.type == MessageEvent.Type.CAN015_UPDATE) {
                LogUtils.printI(TAG, "type="+event.type.name() +", data="+event.data);
                Can015Entity entity = (Can015Entity) event.data;
                can015Sender.updateData(entity);
            }else if(event.type == MessageEvent.Type.CAN015_D1_ADD){
                LogUtils.printI(TAG, "type="+event.type.name());
                can015Sender.setD1Add();
            }else if(event.type == MessageEvent.Type.CAN015_D1_SUBTRACT){
                LogUtils.printI(TAG, "type="+event.type.name());
                can015Sender.setD1Subtract();
            }else if(event.type == MessageEvent.Type.CAN0BC_TO_CAN1DB){
                LogUtils.printI(TAG, "type="+event.type.name());
                Can1DBEntity entity = (Can1DBEntity) event.data;
                can1DBSender.updateData(entity);
            }else if (event.type == MessageEvent.Type.LIN20_TO_CAN04B) {
                LogUtils.printI(TAG, "type=" + event.type.name() + ", data=" + event.data);
                BinaryEntity d3 = (BinaryEntity) event.data;
                Can40BEntity can40BEntity = new Can40BEntity();
                can40BEntity.setD3(d3);
                can04BSender.updateData(can40BEntity);
            }else if(event.type == MessageEvent.Type.LIN10_TO_CAN04B){
                LogUtils.printI(TAG, "type=" + event.type.name() + ", data=" + event.data);
                Can40BEntity entity = (Can40BEntity) event.data;
                can04BSender.updateData(entity);
            }else if(event.type == MessageEvent.Type.SET_DRIVER_WIND_ADD){
                if(event.data instanceof String){
                    String value = (String) event.data;
                    if(can1DCSender!=null){
                        can1DCSender.setDriverWindAdd(value);
                    }
                }
            }else if(event.type == MessageEvent.Type.SET_FRONT_SEAT_WIND_ADD){
                if(event.data instanceof String){
                    String value = (String) event.data;
                    if(can1DCSender!=null){
                        can1DCSender.setFrontSeatWindAdd(value);
                    }
                }

            }else if(event.type == MessageEvent.Type.SET_DRIVER_WIND_SUBTRACT){
                if(event.data instanceof String){
                    String value = (String) event.data;
                    if(can1DCSender!=null){
                        can1DCSender.setDriverWindSubtract(value);
                    }
                }
            }else if(event.type == MessageEvent.Type.SET_FRONT_SEAT_WIND_SUBTRACT){
                if(event.data instanceof String){
                    String value = (String) event.data;
                    if(can1DCSender!=null){
                        can1DCSender.setFrontSeatWindSubtract(value);
                    }
                }
            }else if(event.type == MessageEvent.Type.SET_DRIVER_TEMP_ADD){
                if(event.data instanceof String){
                    String value = (String) event.data;
                    if(can1DCSender!=null){
                        can1DCSender.setDriverTempAdd(value);
                    }
                }
            }else if(event.type == MessageEvent.Type.SET_DRIVER_TEMP_SUBTRACT){
                if(event.data instanceof String){
                    String value = (String) event.data;
                    if(can1DCSender!=null){
                        can1DCSender.setDriverTempSubtract(value);
                    }
                }
            }else if(event.type == MessageEvent.Type.SET_FRONT_SEAT_TEMP_ADD){
                if(event.data instanceof String){
                    String value = (String) event.data;
                    if(can1DCSender!=null){
                        can1DCSender.setFrontSeatTempAdd(value);
                    }
                }
            }else if(event.type == MessageEvent.Type.SET_FRONT_SEAT_TEMP_SUBTRACT){
                if(event.data instanceof String){
                    String value = (String) event.data;
                    if(can1DCSender!=null){
                        can1DCSender.setFrontSeatTempSubtract(value);
                    }
                }
            }else if(event.type == MessageEvent.Type.UPDATE_FRONT_AC){
                executorService.execute(() -> {
                    try {
                        if(event.data instanceof Can20BTable){
                            Can20BTable table = (Can20BTable) event.data;
                            if(can1DCSender!=null){
                                can1DCSender.updateData(table);
                            }
                            if(can04BSender!=null){
                                String acKeyStatus = table.getAcKeyStatus();
                                BinaryEntity can20bD5 = new BinaryEntity(acKeyStatus);
                                can04BSender.setAcRest(can20bD5.getB3());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }else if(event.type == MessageEvent.Type.UPDATE_REAR_DEMIST){
                if(event.data instanceof String){
                    String can007D3 = (String) event.data;
                    if(can04BSender!=null){
                        BinaryEntity binaryEntity = new BinaryEntity(can007D3);
                        can04BSender.setRearDemist(binaryEntity.getB6());
                    }
                }
            }else if(event.type == MessageEvent.Type.SET_AC_AUTO_MODE){
                if(event.data instanceof Boolean){
                    boolean auto = (boolean) event.data;
                    if(can1DCSender!=null){
                        can1DCSender.setAutoAcStatus(auto);
                    }
                }

            }else if(event.type == MessageEvent.Type.FRONT_AC_OFF){
                if(event.data instanceof Boolean){
                    boolean isAcOff = (boolean) event.data;
                    if(can1DCSender!=null){
                        can1DCSender.setAcOff(isAcOff);
                    }
                }
            }else if(event.type == MessageEvent.Type.SET_AC_COMPRESSOR_STATUS){
                if(event.data instanceof Boolean){
                    boolean compressor = (boolean) event.data;
                    if(can1DCSender!=null){
                        can1DCSender.setCompressorStatus(compressor);
                    }
                }
            }else if(event.type == MessageEvent.Type.SET_FRONT_DEMIST_STATUS){
                if(event.data instanceof Boolean){
                    boolean isFrontDemist = (boolean) event.data;
                    if(can1DCSender!=null){
                        can1DCSender.setFrontDemistOff(isFrontDemist);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
