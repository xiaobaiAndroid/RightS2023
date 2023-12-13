package com.android.launcher.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.launcher.MainActivity;
import com.android.launcher.MyApp;
import com.android.launcher.bluetooth.BluetoothDeviceHelper;

import module.common.MessageEvent;
import module.common.utils.BluetoothMusicHelper;
import com.android.launcher.music.CarMusicItem;
import module.common.utils.FMHelper;

import com.android.launcher.music.MusicHomeFragment;
import com.android.launcher.music.bluetooth.PlayStatus;
import com.android.launcher.service.LivingService;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.android.launcher.util.FuncUtil;
import module.common.utils.LogUtils;
import module.common.utils.StringUtils;

import com.android.launcher.vo.MusicVo;

import org.greenrobot.eventbus.EventBus;

import java.util.Set;

public class BluetoothBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = BluetoothBroadCastReceiver.class.getSimpleName();

    public MusicVo musicVo = new MusicVo() ;

    public static volatile int lastPlaybackState = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();


        if (action.equals("android.bluetooth.avrcp-controller.profile.action.TRACK_EVENT")){
            Bundle bundle  = intent.getExtras() ;
            PlaybackState playbackState = (PlaybackState)bundle.get("android.bluetooth.avrcp-controller.profile.extra.PLAYBACK") ;

            if (playbackState!=null){


                int state = playbackState.getState() ;
                if(lastPlaybackState == state){
                    return;
                }
                LogUtils.printI(TAG, "lastPlaybackState="+lastPlaybackState +", state="+state);

                lastPlaybackState = state;
                if (state == PlayStatus.PLAY.getValue()){
                    //正在播放
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.BLUETOOTH_MUSIC_PLAY));
                    if (FuncUtil.openFM){
                        FMHelper.finishFM( MyApp.getGlobalContext());
                        FuncUtil.openFM = false ;
                        try {
                            Thread.sleep(500);
                            BluetoothMusicHelper.play(MyApp.getGlobalContext());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }else if(state == PlayStatus.STOP.getValue()){
                    //播放停止或者中断
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.BLUETOOTH_MUSIC_PAUSE));

                    new Thread(() -> {
                        try {
                            String lyricSendCmd = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.MUSIC_PAUSE.getTypeValue() + SerialPortDataFlag.END_FLAG;
                            SendHelperLeft.handler(lyricSendCmd);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        } else if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)){
            LogUtils.printI(TAG, "action="+action);
            disposeBluetoothConnected(intent);
            lastPlaybackState =  PlayStatus.NONE.getValue();
        } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)){
            LogUtils.printI(TAG, "action="+action);
            LivingService.connectBlueDevice = null;
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.BLUETOOTH_DISCONNECTED));
            lastPlaybackState = PlayStatus.NONE.getValue();
        } else if (action.equals("com.xyauto.bt.songinfo")){
            LogUtils.printI(TAG, "action="+action);
            Bundle bundle = intent.getExtras();

            String artist = bundle.getString("title") ;


            String title = bundle.getString("artist") ;
            if(!TextUtils.isEmpty(title)){
                try {
                    title = title.replace("- <unknown>","");
                    title = title.trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            String album = bundle.getString("album") ;

            LogUtils.printI(TAG, action+"----------bundle="+bundle);

            if (title!= null ){
                musicVo.setTitle(title);
            }

            if (artist != null){
                musicVo.setArtist(artist) ;
            }

            if (album!=null){
                musicVo.setAlbum(album);
            }
//            CallBackUtils.doCallBackMethod(musicVo);
            FuncUtil.musicVo = musicVo ;


            sendMusicInfo(title, artist, album);

        }else if(action.equals("com.xyauto.bt.playstate")){
            Bundle bundle = intent.getExtras();
            int song_len = bundle.getInt("song_len");
            int song_pos = bundle.getInt("song_pos");

            LogUtils.printI(TAG, "song_len="+song_len +", song_pos="+song_pos);
            if(song_pos != -1){
                if(MusicHomeFragment.class.getSimpleName().equals(MainActivity.currentFragment)){
                    updateMusicDuration(song_len);
                    updateMusicPlayProgress(song_pos);
                }
            }
        }
    }



    private void updateMusicPlayProgress(int song_pos) {
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CURRENT_MUSIC_PROGRESS);
        messageEvent.data = song_pos;
        EventBus.getDefault().post(messageEvent);
    }

    private void updateMusicDuration(long song_len) {
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.MUSIC_TOTAL_DURATION);
        messageEvent.data = song_len;
        EventBus.getDefault().post(messageEvent);
    }

    private void sendMusicInfo(String title, String artist, String album) {
        try {
            CarMusicItem carMusicItem = new CarMusicItem();
            carMusicItem.setTitle(title);
            carMusicItem.setLyric(artist);
            carMusicItem.setAlbum(album);
            MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.UPDATE_MUSIC_INFO);
            messageEvent.data = carMusicItem;
            EventBus.getDefault().post(messageEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
    * @description: 处理蓝牙连接
    * @createDate: 2023/5/29
    */
    private void disposeBluetoothConnected(Intent intent){
//            String send = "AABB01CCDD" ;
//            SendHelperLeft.handler(send);
        try {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            LogUtils.printI(TAG, "ACTION_ACL_CONNECTED----连接的蓝牙："+device.getName() +", "+device.getAddress());

            Set<BluetoothDevice> pairedDevices =   BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            LogUtils.printI(TAG, "ACTION_ACL_CONNECTED----配对的蓝牙数量："+pairedDevices.size());
            if (pairedDevices.size()>1){
                for (BluetoothDevice getDevice : pairedDevices) {
                    String listDeviceName = getDevice.getName() ;
                    String devName = device.getName() ;
                    if(listDeviceName.equals(devName)){
                        LogUtils.printI(TAG, "ACTION_ACL_CONNECTED----不删除的设备："+getDevice.getName());
                    }else{
                        BluetoothDeviceHelper.deleteDevice(getDevice);
                    }
                }
            }
            LivingService.connectBlueDevice = device;
            MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.BLUETOOTH_CONNECTED);
            messageEvent.data = device;
            EventBus.getDefault().post(messageEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
