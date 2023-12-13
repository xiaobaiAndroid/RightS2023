package com.android.launcher.music.bluetooth;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import module.common.MessageEvent;

import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;

import module.common.utils.BluetoothMusicHelper;

import com.android.launcher.music.CarMusicItem;
import com.android.launcher.music.MusicButtonType;
import com.android.launcher.receiver.BluetoothBroadCastReceiver;
import com.android.launcher.service.LivingService;
import com.android.launcher.util.FuncUtil;
import com.warkiz.widget.IndicatorSeekBar;

import org.greenrobot.eventbus.EventBus;

import module.common.utils.IconUtils;
import module.common.utils.LogUtils;
import module.common.utils.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @date： 2023/10/16
 * @author: 78495
 */
public class BluetoothMusicFragment extends FragmentBase {

    private ImageView bluetoothIV;
    private CardView bluetoothCV;
    private TextView musicTitleTV;
    private TextView musicLyricTV;

    private boolean isPlaying = false;
    private TextView durationTV;
    private long currentMusicDuration;
    private IndicatorSeekBar seekBar;
    private TextView currentProgressTV;

    private TextView bluetoothDeviceTV;

    private long musicDuration;
    private ImageView nextIV;
    private ImageView previousIV;
    private ImageView playIV;

    private boolean connected = false;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        bluetoothDeviceTV = view.findViewById(R.id.bluetoothDeviceTV);
        bluetoothCV = view.findViewById(R.id.bluetoothCV);
        bluetoothIV = view.findViewById(R.id.bluetoothIV);
        musicTitleTV = view.findViewById(R.id.musicTitleTV);
        musicLyricTV = view.findViewById(R.id.musicLyricTV);
        seekBar = view.findViewById(R.id.seekBar);
        durationTV = view.findViewById(R.id.durationTV);
        currentProgressTV = view.findViewById(R.id.currentProgressTV);

        nextIV = view.findViewById(R.id.nextIV);
        previousIV = view.findViewById(R.id.previousIV);
        playIV = view.findViewById(R.id.playIV);

        playIV.setOnClickListener(v -> {
            if(connected){
                isPlaying = true;
                startPlay();

                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.STOP_USB_MUSIC));
            }
        });

        nextIV.setOnClickListener(v -> {
            if(connected) {
                BluetoothMusicHelper.next(getContext());
            }
        });
        previousIV.setOnClickListener(v -> {
            if(connected) {
                BluetoothMusicHelper.previous(getContext());
            }
        });

        if(LivingService.connectBlueDevice!=null){
            connected = true;
        }else{
            connected = false;
        }

        if (connected) {
            setConnectedStatusView();

            if(BluetoothBroadCastReceiver.lastPlaybackState == PlayStatus.PLAY.getValue()){
                isPlaying = true;
                setPlayStatusView(true,R.drawable.ic_music_pause);
            }else{
                isPlaying = false;
                setPlayStatusView(false,R.drawable.ic_music_play);
            }
        } else {
            setUnconnectedStatusView();
            isPlaying = false;
            setPlayStatusView(false,R.drawable.ic_music_play);
        }

    }

    private void setUnconnectedStatusView() {
        if(bluetoothIV == null){
            return;
        }
        IconUtils.setColor(bluetoothIV, getResources().getColor(R.color.cl_ffffff));
        bluetoothCV.setCardBackgroundColor(getResources().getColor(R.color.cl_carview));
        if(bluetoothDeviceTV!=null){
            bluetoothDeviceTV.setText(getResources().getString(R.string.unconnected));
        }
    }

    private void setConnectedStatusView() {
        if(bluetoothIV == null){
            return;
        }
        IconUtils.setColor(bluetoothIV, getResources().getColor(R.color.cl_item_selected));
        bluetoothCV.setCardBackgroundColor(getResources().getColor(R.color.ffffff));
        if(bluetoothDeviceTV!=null){
            if(LivingService.connectBlueDevice!=null){
                String blueDeviceName = LivingService.connectBlueDevice.getName();
                bluetoothDeviceTV.setText(StringUtils.removeNull(blueDeviceName));
            }
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_bluetooth_music;
    }


    @Override
    public void disposeMessageEvent(MessageEvent event) {

        if (event.type == MessageEvent.Type.UPDATE_MUSIC_INFO) {
            LogUtils.printI(TAG, "disposeMessageEvent----event=" + event.type.name());
            if (event.data instanceof CarMusicItem) {
                CarMusicItem musicItem = (CarMusicItem) event.data;
                updateMusicInfo(musicItem);
            }
        } else if (event.type == MessageEvent.Type.BLUETOOTH_CONNECTED) {
            LogUtils.printI(TAG, "disposeMessageEvent----event=" + event.type.name());
            resetSeekBar();
            setConnectedStatusView();
        } else if (event.type == MessageEvent.Type.BLUETOOTH_DISCONNECTED) {
            LogUtils.printI(TAG, "disposeMessageEvent----event=" + event.type.name());
            resetSeekBar();
            setUnconnectedStatusView();
        } else if (event.type == MessageEvent.Type.BLUETOOTH_MUSIC_PLAY) {
            LogUtils.printI(TAG, "disposeMessageEvent----event=" + event.type.name());
            setPlayStatusView(true, R.drawable.ic_music_pause);
        } else if (event.type == MessageEvent.Type.BLUETOOTH_MUSIC_PAUSE) {
            LogUtils.printI(TAG, "disposeMessageEvent----event=" + event.type.name());
            setPlayStatusView(false, R.drawable.ic_music_play);
        } else if (event.type == MessageEvent.Type.MUSIC_TOTAL_DURATION) {
            try {
                musicDuration = (long) event.data;
                updateMusicDuration(musicDuration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event.type == MessageEvent.Type.CURRENT_MUSIC_PROGRESS) {
            if (currentProgressTV != null) {
                int currentMusicProgress = (int) event.data;
                currentMusicDuration = currentMusicProgress;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(currentMusicDuration);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(currentMusicDuration) - TimeUnit.MINUTES.toSeconds(minutes);
                currentProgressTV.setText(String.format("%02d:%02d", minutes, seconds));
                seekBar.setProgress(currentMusicDuration);
            }
        }else if(event.type == MessageEvent.Type.HOME_MUSIC_PLAY_PREVIOUS){
            if(BluetoothBroadCastReceiver.lastPlaybackState == PlayStatus.PLAY.getValue()){
                BluetoothMusicHelper.previous(getContext());
            }
        }else if(event.type == MessageEvent.Type.HOME_MUSIC_PLAY_NEXT){
            if(BluetoothBroadCastReceiver.lastPlaybackState == PlayStatus.PLAY.getValue()){
                BluetoothMusicHelper.next(getContext());
            }
        }else if(event.type == MessageEvent.Type.STOP_BLUETOOTH_MUSIC){
            BluetoothMusicHelper.pause(getContext());
            setPlayStatusView(false, R.drawable.ic_music_play);
        }
    }

    private void resetSeekBar() {
        if (currentProgressTV != null && durationTV != null) {
            currentProgressTV.setText("00:00");
            seekBar.setProgress(0);
            durationTV.setText("00:00");
        }
    }

    private void updateMusicDuration(long usbMusicDuration) {
        if (durationTV != null) {

            currentMusicDuration = usbMusicDuration;
            seekBar.setMax(usbMusicDuration);

            long minutes = TimeUnit.MILLISECONDS.toMinutes(usbMusicDuration);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(usbMusicDuration) - TimeUnit.MINUTES.toSeconds(minutes);
            durationTV.setText(String.format("%02d:%02d", minutes, seconds));
        }
    }


    private void setPlayStatusView(boolean playing, int resId) {
        isPlaying = playing;
        if (playIV != null) {
            playIV.setImageResource(resId);
        }
    }


    private void updateMusicInfo(CarMusicItem musicItem) {
        if (musicItem == null) {
            return;
        }

        musicTitleTV.setText(StringUtils.removeNull(musicItem.getTitle()));

        if (musicItem.getLyric() != null) {
            musicLyricTV.setText(musicItem.getLyric());
        } else {
            musicLyricTV.setText(getResources().getString(R.string.not_music_lyric));
        }
    }


    private void startPlay() {
        if (isPlaying) {
            BluetoothMusicHelper.pause(getContext());
            playIV.setImageResource(R.drawable.ic_music_play);
        } else {
            BluetoothMusicHelper.play(getContext());
            playIV.setImageResource(R.drawable.ic_music_pause);
        }

    }

}
