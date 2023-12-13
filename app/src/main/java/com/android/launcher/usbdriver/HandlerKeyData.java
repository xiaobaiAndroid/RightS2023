package com.android.launcher.usbdriver;

import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.android.launcher.MyApp;

import module.common.MessageEvent;
import com.android.launcher.util.ACache;
import com.android.launcher.util.CommonUtil;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;
import com.android.launcher.util.SendcanCD;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

public class HandlerKeyData {

    public static final String TAG = HandlerKeyData.class.getSimpleName();

    public static volatile boolean jingyin = false;

    public static Handler handlerkey = new Handler(Looper.getMainLooper());
    public static ACache aCache = ACache.get(MyApp.getGlobalContext());

    public static void handler(String data) {
        if (data.length() == 14) {
            String fkey = data.substring(6, 8);
            String skey = data.substring(8, 10);
            Log.i("handlerKey", data + "============" + fkey + "=============" + skey + "=====" + MyApp.propCar);
            String key = fkey + skey;

            if (!MyApp.propCar) {
                if (!key.equals("0000")) { //

                    String fkeybinary = CommonUtil.convertHexToBinary(fkey);
                    String[] fkresult = fkeybinary.split("");
                    for (int i = 1; i < fkresult.length; i++) {
                        fkHandler(i, fkresult[i]);
                    }

                    String skeybinary = CommonUtil.convertHexToBinary(skey);
                    String[] skresult = skeybinary.split("");
                    Log.i("handlerKey", skeybinary + "-------fkey-----" + Arrays.toString(skresult));
                    for (int i = 1; i < skresult.length; i++) {
                        skHandler(i, skresult[i]);
                    }
                }
            } else {
                if (!key.equals("0000")) {
                    String skeybinary = CommonUtil.convertHexToBinary(skey);
                    String[] skresult = skeybinary.split("");
                    Log.i("handlerKeyprop", skeybinary + "-------fkey-----" + Arrays.toString(skresult));
                    for (int i = 1; i < skresult.length; i++) {
                        skHandlerProp(i, skresult[i]);
                    }
                }

            }

        }

    }

    private static void skHandlerProp(int i, String s) {
        switch (i) {
            case 1://
                Log.i("skhandlerKeyprop", s + "-------1-------");
                break;
            case 2://
                Log.i("skhandlerKeyprop", s + "------2--------");
                break;
            case 3:// 音量调大
                if (s.equals("1")) {
                    Log.i("skhandlerKeyprop", s + "--------音量调小------");
                    SendcanCD.handler("AA000002000000FA7E00000000000000");
                }
                break;
            case 4:// 音量调小
                if (s.equals("1")) {
                    SendcanCD.handler("AA000002000000FA8000000000000000");
                }
                break;
            case 5:// 静音
                if (s.equals("1")) {
                    Log.i("skhandlerKeyprop", s + "-----静音---------");
                }
                break;
            case 6: //右侧屏幕电源开关 ---》 右侧
                if (s.equals("1")) {
                    Log.i("skhandlerKeyprop", s + "--------ON建------");
                    SendcanCD.handler("AA000004000000FD1000000000000000");
                }
                break;
            case 7: // 小车
                if (s.equals("1")) {
                }
                break;
            case 8:
                if (s.equals("1")) {
                }
                break;
        }
    }

    private static long currentTime;
    private static long lastTime;

    /**
     * 处理 右侧按键
     *
     * @param i
     * @param s
     */
    private static void skHandler(int i, String s) {
        switch (i) {
            case 1://
                Log.i("skhandlerKey", s + "-------1-------");
                break;
            case 2://
                Log.i("skhandlerKey", s + "------2--------");
                break;
            case 3:// 音量减小
                disposeAudioSubtract(s);
                break;
            case 4://// 音量调大
                disposeAudioAdd(s);
                break;
            case 5:// 静音
                disposeSilence(s);
                break;
            case 6: //右侧屏幕电源开关 ---》 右侧

                if (s.equals("1")) {
                    Log.i("skhandlerKey", s + "--------CD机开关------");
                    SendcanCD.handler("AA000004000000FD1000000000000000");
                }
                break;
            case 7: //// 小车
                if (s.equals("1")) {
                    Log.i("skhandlerKey", s + "------小车--------");
//                    Intent  intent = new Intent(App.getGlobalContext(), CarInfoActivity.class) ;
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
//                    App.getGlobalContext().startActivity(intent);

                    MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CAR_INFO_ACTIVITY);
                    EventBus.getDefault().post(messageEvent);
                }
                break;
            case 8:
                if (s.equals("1")) {
//                    Intent  intent = new Intent(App.getGlobalContext(), PhoneCarNumberActivity.class) ;
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
//                    App.getGlobalContext().startActivity(intent);
                    MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.PHONE_CAR_NUMBER);
                    EventBus.getDefault().post(messageEvent);
                }
                break;
        }
    }

    /**
    * @description: 处理静音
    * @createDate: 2023/6/5
    */
    private static void disposeSilence(String status) {
        try {
            LogUtils.printI(HandlerKeyData.class.getSimpleName(), "disposeSilence-----status="+status);
            if (status.equals("1")) {
                if (!jingyin) {
                    int currentVolume = MyApp.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    LogUtils.printI(HandlerKeyData.class.getSimpleName(), "disposeSilence-----jingyin="+jingyin+"---currentVolume="+currentVolume);
                    SPUtils.putInt(MyApp.getGlobalContext(),SPUtils.SP_MUSIC_VOLUME, currentVolume);
                    MyApp.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    jingyin = true;
                } else {
                    int currentVolume = SPUtils.getInt(MyApp.getGlobalContext(), SPUtils.SP_MUSIC_VOLUME, 0);
                    LogUtils.printI(HandlerKeyData.class.getSimpleName(), "disposeSilence----jingyin="+jingyin+"----currentVolume="+currentVolume);
                    MyApp.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                    jingyin = false;
                }
            }else if("2".equals(status)){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized void disposeAudioAdd(String s) {
        try {

            LogUtils.printI(HandlerKeyData.class.getSimpleName(), "disposeAudioAdd------s=" + s);
            if (s.equals("1")) {
                currentTime = System.currentTimeMillis();
                long time = currentTime-lastTime;
                Log.i("audioAdjust", "time=" + time);
    //            if(time < 10){ //间隔小于20毫秒不响应
    //                LogUtils.printI(TAG, "音量增加操作过快");
    //                return;
    //            }
                lastTime = currentTime;

                jingyin = false;

                int max1 = MyApp.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int AUDIOLEVELjia = MyApp.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (AUDIOLEVELjia < max1) {
                    AUDIOLEVELjia++;
                }
                LogUtils.printI(HandlerKeyData.class.getSimpleName(), "AUDIOLEVELjia------" + AUDIOLEVELjia);

                aCache.put("currentAudio", AUDIOLEVELjia + "");
                MyApp.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AUDIOLEVELjia, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                SendcanCD.handler("AA000002000000FA8000000000000000"); // 80 为声音增加

                SPUtils.putInt(MyApp.getGlobalContext(), SPUtils.SP_MUSIC_VOLUME, AUDIOLEVELjia);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized void disposeAudioSubtract(String s) {
        LogUtils.printI(HandlerKeyData.class.getSimpleName(), "disposeAudioSubtract------s=" + s);
        try {
            if (s.equals("1")) { //1：音量减的操作， 0：不是音量减的操作
                currentTime = System.currentTimeMillis();
                long time = currentTime - lastTime;
                Log.i("audioAdjust", "currentTime=" + currentTime);
    //            if(time < 20){ //间隔小于20毫秒不响应
    //                LogUtils.printI(TAG, "音量减少操作过快");
    //                return;
    //            }
                lastTime = currentTime;

                jingyin = false;
                int AUDIOLEVEL = MyApp.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (AUDIOLEVEL > 0) {
                    AUDIOLEVEL--;
                }
                LogUtils.printI(HandlerKeyData.class.getSimpleName(), "AUDIOLEVEL------" + AUDIOLEVEL);
                aCache.put("currentAudio", AUDIOLEVEL + "");
                MyApp.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AUDIOLEVEL, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                SendcanCD.handler("AA000002000000FA7E00000000000000"); //7E 为声音减小

                SPUtils.putInt(MyApp.getGlobalContext(), SPUtils.SP_MUSIC_VOLUME, AUDIOLEVEL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理 左侧按键
     *
     * @param i
     * @param s
     */
    private static void fkHandler(int i, String s) {

        switch (i) {
            case 1: //media
                Log.i("fkhandlerKey", s + "-------media-------" + s);
                if (s.equals("1")) {
//                    Intent  intent = new Intent(App.getGlobalContext(), MusicActivity.class) ;
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK) ;
//                    App.getGlobalContext().startActivity(intent);
                    Log.i("skhandlerKey", s + "-----启动多媒体页面---------");
                    MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.MUSIC_ACTIVITY);
                    EventBus.getDefault().post(messageEvent);
                }
                break;
            case 2:// FM
                Log.i("fkhandlerKey", s + "--------FM------" + s);
                if (s.equals("1")) {

//                    handlerkey.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            App.currentActivity = new FmActivity();
//                            FuncUtil.openFM = true ;
//                            FuncUtil.sendShellCommand(" am start --user 0 -n com.android.fmradio/.FmMainActivity");
//                        }
//                    });
                    MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.FM_ACTIVITY);
                    EventBus.getDefault().post(messageEvent);
                }
                break;
            case 3:// 导航
                Log.i("fkhandlerKey", s + "------------导航--" + s);
                if (s.equals("1")) {
                    MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.GAODE_MAP_ACTIVITY);
                    EventBus.getDefault().post(messageEvent);
//                    Intent intent1 = new Intent();
//                    intent1.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
//                    intent1.putExtra("KEY_TYPE", 10034);
//                    intent1.putExtra("SOURCE_APP","Third App");
//                    App.getGlobalContext().sendBroadcast(intent1);
//                    FuncUtil.currentActivity ="GAODE" ;
                }

                break;
            case 4://座椅调节
                Log.i("fkhandlerKey", s + "------座椅调节--------" + s);
                sendHeadsetDownData();
                break;
            case 5:// CMS
                Log.i("fkhandlerKey", s + "------cms--------" + s);
                if (s.equals("1")){
                    String cms = MyApp.getaCache().getAsString("CMS") ;
                    String sendCms = "" ;
                    if (cms.equals("C")){
                        MyApp.getaCache().put("CMS","M");
                        sendCms = "00" ;
                    }
                    if (cms.equals("M")){
                        MyApp.getaCache().put("CMS","S");
                        sendCms = "01" ;
                    }
                    if (cms.equals("S")){
                        MyApp.getaCache().put("CMS","C");
                        sendCms = "02" ;
                    }
                    String send = "AABB07"+sendCms+"CCDD" ;
                    SendHelperLeft.handler(send);
                }
                break;
            case 6: // SMC
                Log.i("fkhandlerKey", s + "-----SMC---------" + s);
                if (s.equals("1")) {
                    String cms = MyApp.getaCache().getAsString("CMS");
                    String sendCms = "";
                    if (cms.equals("C")) {
                        MyApp.getaCache().put("CMS", "M");
                        sendCms = "00";
                    }
                    if (cms.equals("M")) {
                        MyApp.getaCache().put("CMS", "S");
                        sendCms = "01";
                    }
                    if (cms.equals("S")) {
                        MyApp.getaCache().put("CMS", "C");
                        sendCms = "02";
                    }
                    String send = "AABB07" + sendCms + "CCDD";
                    SendHelperLeft.handler(send);
                }

                break;
            case 7: // 倒车影像
                Log.i("fkhandlerKey", s + "--------倒车影像------" + s);
                break;
            case 8: // *字键
                Log.i("fkhandlerKey", s + "------*字键--------" + s);
                break;
        }
    }

    private static void sendHeadsetDownData() {
        String send = "AABB33CCDD";
        SendHelperLeft.handler(send);
    }
}

