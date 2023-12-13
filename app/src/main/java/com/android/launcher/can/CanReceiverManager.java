package com.android.launcher.can;

import android.content.Context;

import module.common.MessageEvent;

import com.android.launcher.can.receiver.Can001Receiver;
import com.android.launcher.can.receiver.Can007Receiver;
import com.android.launcher.can.receiver.Can025Receiver;
import com.android.launcher.can.receiver.Can029Receiver;
import com.android.launcher.can.receiver.Can069Receiver;
import com.android.launcher.can.receiver.Can0BBReceiver;
import com.android.launcher.can.receiver.Can0BCReceiver;
import com.android.launcher.can.receiver.Can10CReceiver;
import com.android.launcher.can.receiver.Can20BReceiver;
import com.android.launcher.can.receiver.Can2EEReceiver;
import com.android.launcher.can.receiver.Can39FReceiver;

import module.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @description:
 * @createDate: 2023/7/24
 */
public class CanReceiverManager {

    private static final String TAG = CanReceiverManager.class.getSimpleName();

    private Can001Receiver can001Receiver;
    private Can2EEReceiver can2EEReceiver;
    private Can007Receiver can007Receiver;
    private Can10CReceiver can10CReceiver;
    private Can20BReceiver can20BReceiver;
    private Can025Receiver can025Receiver;
    private Can029Receiver can029Receiver;
    private Can39FReceiver can39FReceiver;
    private Can069Receiver can069Receiver;
    private Can0BCReceiver can0BCReceiver;
    private Can0BBReceiver can0BBReceiver;

    public static volatile String lastData001 = "";
    public static volatile String lastData2EE = "";
    public static volatile String lastData007 = "";
    public static volatile String lastData10C = "";
    public static volatile String lastData20B = "";
    public static volatile String lastData025 = "";
    public static volatile String lastData029 = "";
    public static volatile String lastData39F = "";
    public static volatile String lastData069 = "";
    public static volatile String lastData0BC = "";
    public static volatile String lastData0BB = "";


    public CanReceiverManager(Context context) {
        can001Receiver = new Can001Receiver(context);
        can2EEReceiver = new Can2EEReceiver(context);
        can007Receiver = new Can007Receiver(context);
        can10CReceiver = new Can10CReceiver(context);
        can20BReceiver = new Can20BReceiver(context);
        can025Receiver = new Can025Receiver(context);
        can029Receiver = new Can029Receiver(context);
        can39FReceiver = new Can39FReceiver(context);
        can069Receiver = new Can069Receiver(context);
        can0BCReceiver = new Can0BCReceiver(context);
        can0BBReceiver = new Can0BBReceiver(context);
        reset();
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
            if(event.type == MessageEvent.Type.CAN0BC_UPDATE){
                LogUtils.printI(TAG, "type="+event.type + ", data="+event.data);
                String dataBC = (String) event.data;
                can0BCReceiver.updateData(dataBC);
            }else if(event.type == MessageEvent.Type.CAN0BB_UPDATE){
                LogUtils.printI(TAG, "type="+event.type + ", data="+event.data);
                String data = (String) event.data;
                can0BBReceiver.updateData(data);
            }else if(event.type == MessageEvent.Type.CAN20B_UPDATE){
                LogUtils.printI(TAG, "type="+event.type + ", data="+event.data);
                String data = (String) event.data;
                can20BReceiver.updateData(data);
            }else if(event.type == MessageEvent.Type.CAN025_UPDATE){
                LogUtils.printI(TAG, "type="+event.type + ", data="+event.data);
                String data = (String) event.data;
                can025Receiver.updateData(data);
            }else if(event.type == MessageEvent.Type.CAN39F_UPDATE){
                LogUtils.printI(TAG, "type="+event.type + ", data="+event.data);
                String data = (String) event.data;
                can39FReceiver.updateData(data);
            }else if(event.type == MessageEvent.Type.CAN007_UPDATE){
                LogUtils.printI(TAG, "type="+event.type + ", data="+event.data);
                String data = (String) event.data;
                can007Receiver.updateData(data);
            }else if(event.type == MessageEvent.Type.CAN029_UPDATE){
                LogUtils.printI(TAG, "type="+event.type + ", data="+event.data);
                String data = (String) event.data;
                can029Receiver.updateData(data);
            }else if(event.type == MessageEvent.Type.CAN001_UPDATE){
                LogUtils.printI(TAG, "type="+event.type + ", data="+event.data);
                String data = (String) event.data;
                can001Receiver.updateData(data);
            }else if(event.type == MessageEvent.Type.CAN10C_UPDATE){
                LogUtils.printI(TAG, "type="+event.type + ", data="+event.data);
                String data = (String) event.data;
                can10CReceiver.updateData(data);
            }else if(event.type == MessageEvent.Type.CAN2EE_UPDATE){
                LogUtils.printI(TAG, "type="+event.type + ", data="+event.data);
                String data = (String) event.data;
                can2EEReceiver.updateData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release(){
        can0BCReceiver.release();
        can069Receiver.release();
        can001Receiver.release();
        can2EEReceiver.release();
        can007Receiver.release();
        can10CReceiver.release();
        can20BReceiver.release();
        can025Receiver.release();
        can39FReceiver.release();
        can029Receiver.release();
        can0BBReceiver.release();
    }

    public void reset() {
        lastData001 = "";
        lastData2EE = "";
        lastData007 = "";
        lastData10C = "";
        lastData20B = "";
        lastData025 = "";
        lastData029 = "";
        lastData39F = "";
        lastData069 = "";
        lastData0BC = "";
        lastData0BB = "";
    }

}
