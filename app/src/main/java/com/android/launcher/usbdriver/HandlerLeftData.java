package com.android.launcher.usbdriver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import com.android.launcher.MyApp;
import com.android.launcher.util.FuncUtil;
import com.android.launcher.util.OriginalMeterOperationHelper;
import com.android.launcher.util.SendcanCD;
import com.bzf.module_db.entity.Can1E5Table;
import com.bzf.module_db.repository.Can1E5Repository;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import module.common.MessageEvent;
import module.common.utils.AppUtils;
import module.common.utils.FMHelper;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;

/**
 * @description: 处理左边发送过来的数据
 * @createDate: 2023/4/27
 */
public class HandlerLeftData {

    private static final String TAG = HandlerLeftData.class.getSimpleName();


    public static StringBuffer sb = new StringBuffer();

    public static volatile String lastCan20BData = "";
    public static volatile String lastCanBcData = "";
    public static volatile String lastCanBBData = "";
    public static volatile String lastCan1E5StatusData = "";

    @SuppressLint("NewApi")
    public static void lefthandlerdata(String ch) {
        sb.append(ch);
        String dat = sb.toString();
        LogUtils.printI(TAG, "lefthandlerdata-----data=" + dat);

        if (dat.contains("AABB") && dat.contains("CCDD")) {
            if (dat.endsWith("CCDD")) {
                String[] str = dat.split("CCDD");
                for (String s : str) {
                    if (!s.equals("")) {
                        String da = s.replaceAll("AABB", "");
                        leftDataHandler(da);
                    }
                }
                sb.setLength(0);
            } else {
                String[] str = dat.split("CCDD");
                for (int i = 0; i < str.length - 1; i++) {
                    String s = str[i];
                    if (!s.equals("")) {
                        String da = s.replaceAll("AABB", "");
                        leftDataHandler(da);
                    }
                }
                sb.setLength(0);
                sb.append(str[str.length - 1]);
            }
        }
    }


    public static void leftDataHandler(String data) {

        if (data.startsWith("20B")) { //空调参数：主驾副驾的调节 风向
            disposeCan20BData(data);

        } else if (data.startsWith("BB")) {
            disposeCan0BBData(data);
        } else if (data.startsWith("BC")) {
            disposeCan0BCData(data);
        } else if (data.startsWith("1E5")) {
            disposeCan1E5Data(data);
        } else {
            Log.i("propcar", MyApp.propCar + "========================" + data);

            if (!MyApp.propCar) {
                switch (data) {
                    case "1000":
                        break;
                    case "1100":
                        Intent mIntent1 = new Intent("com.unisound.customer.bluetooth.telephony");
                        mIntent1.putExtra("behavior", "answer");
                        MyApp.getGlobalContext().sendBroadcast(mIntent1);
                        break;
                    case "1200":
                        Intent intent = new Intent("xy.android.gtpkey.frd");
                        MyApp.getGlobalContext().sendBroadcast(intent);
                        break;
                    case "1300":
                        Intent intent1 = new Intent("xy.android.gtpkey.rev");
                        MyApp.getGlobalContext().sendBroadcast(intent1);
                        break;
                    case "1400":
                        Intent intent3 = new Intent("xy.android.gtpkey.play");
                        MyApp.getGlobalContext().sendBroadcast(intent3);
                        break;
                    case "1500":
                        Intent intent4 = new Intent("xy.android.gtpkey.pause");
                        MyApp.getGlobalContext().sendBroadcast(intent4);
                        break;
                    case "1600":
//                    Intent mIntent = new Intent("com.unisound.customer.bluetooth.telephony") ;
//                    mIntent.putExtra("behavior","call") ;
//                    mIntent.putExtra("phone_number","15006684053");
//                    App.getGlobalContext().sendBroadcast(mIntent);
                        break;
                    case "1700":
                        Intent mIntent2 = new Intent("com.unisound.customer.bluetooth.telephony");
                        mIntent2.putExtra("behavior", "hangup");
                        MyApp.getGlobalContext().sendBroadcast(mIntent2);
                        break;
                    case "1800": //音量减
                        disposeVolumeSubtract();
                        break;
                    case "1900"://音量加
                        disposeVolumeAdd();
                        break;
                    case "2000"://静音
                        disposeSilence();
                        break;
                    case "2100"://Fm 上一首
                        FMHelper.up(MyApp.getGlobalContext());
                        break;
                    case "2200"://Fm 下一首
                        FMHelper.down(MyApp.getGlobalContext());
                        break;

                    case "2500":
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.PHONE_ANSWER));
                        break;
                    case "2600":
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.PHONE_CLOSE));
                        break;
                }
            } else {
                Log.i("propcar", MyApp.propCar + "============111============" + data);
                switch (data) {
//                    String preLamp = "AA000000400000FD02000000";
                    case "21":// left
                        OriginalMeterOperationHelper.leftDirection();
                        break;
                    case "19":// up
                        OriginalMeterOperationHelper.upDirection();
                        break;
                    case "22":// right
                        OriginalMeterOperationHelper.rightDirection();
                        break;
                    case "20":// down
                        OriginalMeterOperationHelper.downDirection();
                        break;
                    case "66":// enter
                        OriginalMeterOperationHelper.enter();
                        break;
                    case "99":// back
                        OriginalMeterOperationHelper.back();
                        break;
                }
            }

        }
    }

    private static void disposeCan20BData(String data) {
        String data20B = data.substring("20B7".length());
        if (lastCan20BData.equalsIgnoreCase(data20B)) {
            return;
        }
        lastCan20BData = data20B;

        List<String> can20b = FuncUtil.getCan20b(data);
        LogUtils.printI(HandlerLeftData.class.getSimpleName(), "Can20B---data=" + data + ", data20B=" + data20B + ", lastCan20BData=" + lastCan20BData + ", can20b=" + can20b);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CAN20B_UPDATE);
        messageEvent.data = lastCan20BData;
        EventBus.getDefault().post(messageEvent);
    }

    private static void disposeCan0BBData(String data) {
        LogUtils.printI(HandlerLeftData.class.getSimpleName(), "CanBB---data=" + data);
        String dataBB = data.substring("BB05".length());
        if (lastCanBBData.equalsIgnoreCase(dataBB)) {
            return;
        }
        lastCanBBData = dataBB;
        LogUtils.printI(HandlerLeftData.class.getSimpleName(), "CanBB---data=" + data + ", dataBB=" + dataBB + ", lastCanBBData=" + lastCanBBData);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CAN0BB_UPDATE);
        messageEvent.data = lastCanBBData;
        EventBus.getDefault().post(messageEvent);
    }

    private static void disposeCan0BCData(String data) {
        String dataBc = data.substring("BC6".length());
        if (lastCanBcData.equalsIgnoreCase(dataBc)) {
            return;
        }
        lastCanBcData = dataBc;
        LogUtils.printI(HandlerLeftData.class.getSimpleName(), "CanBC---data=" + data + ", dataBC=" + dataBc + ", lastCanBcData=" + lastCanBcData);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CAN0BC_UPDATE);
        messageEvent.data = lastCanBcData;
        EventBus.getDefault().post(messageEvent);
    }

    private static void disposeCan1E5Data(String data) {


        try {
            String data1e5 = data.substring("1E58".length());
            String currentStatus = data1e5.substring(10, 12);
            String airflowPattern = data1e5.substring(12, 14);
            String audioModel = data1e5.substring(14, 16);

            String currentData = currentStatus + airflowPattern + audioModel;
            if (lastCan1E5StatusData.equalsIgnoreCase(currentData)) {
                return;
            }
            lastCan1E5StatusData = currentData;
            LogUtils.printI(TAG, "disposeCan1E5Data---1E5=" + data);
            new Thread(() -> {
                try {

                    String data1 = data1e5.substring(0, 2);
                    String data2 = data1e5.substring(2, 4);
                    String data3 = data1e5.substring(4, 6);
                    String data4 = data1e5.substring(6, 8);
                    String data5 = data1e5.substring(8, 10);
                    String data6 = data1e5.substring(10, 12);
                    String data7 = data1e5.substring(12, 14);
                    String data8 = data1e5.substring(14, 16);

                    Can1E5Table can1E5Table = Can1E5Repository.getInstance().getData(MyApp.getGlobalContext(), AppUtils.getDeviceId(MyApp.getGlobalContext()));
                    can1E5Table.setDriverTemp(data1);
                    can1E5Table.setFrontSeatTemp(data2);
                    can1E5Table.setDriverWind(data3);
                    can1E5Table.setFrontSeatWind(data4);
                    can1E5Table.setWindDirection(data5);
                    can1E5Table.setAirflowMode(data7);
                    can1E5Table.setAudioMode(data8);
                    can1E5Table.setStatus(data6);

                    Can1E5Repository.getInstance().updateData(MyApp.getGlobalContext(),can1E5Table);

                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.UPDATE_CAN1E5_TO_VIEW));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @description: 音量加
     * @createDate: 2023/6/7
     */
    private static void disposeVolumeAdd() {
        try {
            int max1 = MyApp.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int AUDIOLEVELjia = MyApp.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (AUDIOLEVELjia < max1) {
                AUDIOLEVELjia++;
            }

            LogUtils.printI(HandlerLeftData.class.getSimpleName(), "disposeVolumeAdd-------AUDIOLEVEL=" + AUDIOLEVELjia);

            MyApp.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AUDIOLEVELjia, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            SendcanCD.handler("AA000002000000FA8000000000000000");

            SPUtils.putInt(MyApp.getGlobalContext(), SPUtils.SP_MUSIC_VOLUME, AUDIOLEVELjia);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 音量减
     * @createDate: 2023/6/7
     */
    private static void disposeVolumeSubtract() {
        try {
            int AUDIOLEVEL = MyApp.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (AUDIOLEVEL > 0) {
                AUDIOLEVEL--;
            }
            LogUtils.printI(HandlerLeftData.class.getSimpleName(), "disposeVolumeSubtract-------AUDIOLEVEL=" + AUDIOLEVEL);

            MyApp.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AUDIOLEVEL, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            SendcanCD.handler("AA000002000000FA7E00000000000000");

            SPUtils.putInt(MyApp.getGlobalContext(), SPUtils.SP_MUSIC_VOLUME, AUDIOLEVEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @description: 设置静音
     * @createDate: 2023/5/6
     */
    private static void disposeSilence() {
        try {
            if (!HandlerKeyData.jingyin) {
                int currentVolume = MyApp.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                LogUtils.printI(HandlerLeftData.class.getSimpleName(), "disposeSilence-----jingyin=" + HandlerKeyData.jingyin + "---currentVolume=" + currentVolume);
                SPUtils.putInt(MyApp.getGlobalContext(), SPUtils.SP_MUSIC_VOLUME, currentVolume);
                MyApp.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                HandlerKeyData.jingyin = true;
            } else {
                int currentVolume = SPUtils.getInt(MyApp.getGlobalContext(), SPUtils.SP_MUSIC_VOLUME, 0);
                LogUtils.printI(HandlerLeftData.class.getSimpleName(), "disposeSilence----jingyin=" + HandlerKeyData.jingyin + "----currentVolume=" + currentVolume);
                MyApp.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                HandlerKeyData.jingyin = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
