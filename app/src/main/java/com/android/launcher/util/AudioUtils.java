package com.android.launcher.util;

import android.content.Context;
import android.media.AudioManager;

import module.common.utils.LogUtils;

/**
* @description: 音量工具
* @createDate: 2023/6/10
*/
public class AudioUtils {

    private static final String TAG = AudioUtils.class.getSimpleName();

    public static void setMediaVolume(Context context, int value, boolean isShowView){
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

            LogUtils.printI(TAG, "setMediaVolume------value="+value +", maxVolume="+maxVolume);
            if(value >maxVolume){
                value = maxVolume;
            }
            if(value <0){
                value = 0;
            }
            if(isShowView){
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            }else{
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, 0);
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getMediaVolume(Context context){
        int streamVolume = 0;
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.printI(TAG, "getMediaVolume------streamVolume="+streamVolume);
        return streamVolume;
    }

    public static int getMediaMaxVolume(Context context){
        int streamVolume = 0;
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            streamVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.printI(TAG, "getMaxVolume------streamVolume="+streamVolume);
        return streamVolume;
    }
}
