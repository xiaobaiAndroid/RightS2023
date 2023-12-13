package com.android.launcher.music.usb;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import module.common.MessageEvent;
import module.common.utils.IconUtils;
import module.common.utils.LogUtils;

import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.dialog.HintMessageDialog;
import com.android.launcher.music.CarMusicItem;
import com.android.launcher.music.MusicListEntity;
import com.android.launcher.music.bluetooth.PlayStatus;
import com.android.launcher.receiver.BluetoothBroadCastReceiver;
import com.android.launcher.view.LoadingView;
import com.lxj.xpopup.XPopup;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @date： 2023/10/16
 * @author: 78495
 */
public class USBMusicFragment extends FragmentBase {

    private UsbMusicAdapter usbMusicAdapter = new UsbMusicAdapter(new ArrayList<>());
    private ImageView usbIV;
    private ImageView playIV;
    private TextView musicTitleTV;
    private TextView musicLyricTV;
    private TextView durationTV;
    private TextView currentProgressTV;
    private IndicatorSeekBar seekBar;
    private LoadingView loadingV;

    private int lastSelectPosition = 0;
    private RecyclerView musicListRV;
    private boolean isPlaying;
    private long usbMusicDuration;
    private long currentMusicDuration;

    private boolean usbConnected;
    private CardView bluetoothCV;

    private ButtonValidClick buttonValidClick;
    private ConstraintLayout rootCL;

    @Override
    public void disposeMessageEvent(MessageEvent event) {
        if (event.type == MessageEvent.Type.UPDATE_MUSIC_LIST) {
            if (loadingV != null) {
                loadingV.destroy();
            }
            setConnectedStatusView();
            LogUtils.printI(TAG, "disposeMessageEvent---UPDATE_MUSIC_LIST----data=" + event.data);
            updateData(event.data);
        }else if(event.type == MessageEvent.Type.USB_MUSIC_TOTAL_DURATION){
            try {
                usbMusicDuration = (long) event.data;
                updateMusicDuration(usbMusicDuration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(event.type == MessageEvent.Type.USB_CURRENT_MUSIC_PROGRESS){
            if(currentProgressTV!=null){
                int currentMusicProgress = (int) event.data;
                currentMusicDuration = currentMusicProgress;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(currentMusicDuration);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(currentMusicDuration) - TimeUnit.MINUTES.toSeconds(minutes);
                currentProgressTV.setText(String.format("%02d:%02d", minutes, seconds));
                seekBar.setProgress(currentMusicDuration);
            }
        }else if(event.type == MessageEvent.Type.START_READ_OTG_MUSIC){
            loadingV.startLoadingView();
        }else if (event.type == MessageEvent.Type.USB_ACCESSORY_DETACHED) {
            LogUtils.printI(TAG, "收到USB设备断开的消息");
            if(playIV!=null){
                usbConnected = false;
                playIV.setImageResource(R.drawable.ic_music_play);
                musicTitleTV.setText(getResources().getString(R.string.not_play_music));
                currentProgressTV.setText("00:00");
                durationTV.setText("00:00");
                seekBar.setProgress(0);
                usbMusicAdapter.setNewData(null);

                setUnconnectedStatusView();
            }

        }else if (event.type == MessageEvent.Type.BLUETOOTH_CONNECTED) {
            LogUtils.printI(MusicPlayService.class.getSimpleName(), "onMessageEvent------event=" + event.type.name());
            if(playIV!=null){
                playIV.setImageResource(R.drawable.ic_music_play);
            }
        }else if(event.type == MessageEvent.Type.STOP_USB_MUSIC){
            isPlaying = false;
            if(playIV!=null){
                playIV.setImageResource(R.drawable.ic_music_play);
            }
        }else if(event.type == MessageEvent.Type.BLUETOOTH_MUSIC_PLAY){
            isPlaying = false;
            if(playIV!=null){
                playIV.setImageResource(R.drawable.ic_music_play);
            }
        }else if(event.type == MessageEvent.Type.USB_MUSIC_START_PLAY){
            updatePlayMusicViewStatus(event);
        }
    }

    private void updatePlayMusicViewStatus(MessageEvent event) {
        try {
            if(musicTitleTV == null){
                return;
            }
            int position = (int) event.data;

            CarMusicItem lastItem = usbMusicAdapter.getItem(lastSelectPosition);
            lastItem.setSelected(false);
            usbMusicAdapter.notifyItemChanged(lastSelectPosition);


            CarMusicItem musicItem = usbMusicAdapter.getItem(position);
            musicItem.setSelected(true);
            usbMusicAdapter.notifyItemChanged(position);

            lastSelectPosition = position;

            musicTitleTV.setText(musicItem.getTitle());
            playIV.setImageResource(R.drawable.ic_music_pause);

//            LinearLayoutManager layoutManager = (LinearLayoutManager) musicListRV.getLayoutManager();
//            layoutManager.scrollToPositionWithOffset(lastSelectPosition,rootCL.getHeight()/2);

            //测试屏RecyclerView滚动不准确，位置加6才显示出来
//            musicListRV.scrollToPosition(lastSelectPosition+6);
            musicListRV.scrollToPosition(lastSelectPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        rootCL = view.findViewById(R.id.rootL);
        bluetoothCV = view.findViewById(R.id.bluetoothCV);
        usbIV = view.findViewById(R.id.usbIV);
        playIV = view.findViewById(R.id.playIV);
        loadingV = view.findViewById(R.id.loadingV);

        CardView playCV = view.findViewById(R.id.playCV);
        CardView previousCV = view.findViewById(R.id.previousCV);
        CardView nextCV = view.findViewById(R.id.nextCV);
        musicTitleTV = view.findViewById(R.id.musicTitleTV);
        musicLyricTV = view.findViewById(R.id.musicLyricTV);
        durationTV = view.findViewById(R.id.durationTV);
        currentProgressTV = view.findViewById(R.id.currentProgressTV);
        seekBar = view.findViewById(R.id.seekBar);

        musicListRV = view.findViewById(R.id.musicListRV);
        musicListRV.setLayoutManager(new LinearLayoutManager(getContext()));
        musicListRV.setAdapter(usbMusicAdapter);
        musicListRV.setItemAnimator(null);

        setUnconnectedStatusView();

        usbMusicAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if(buttonValidClick.itemSelectedIsValid()){
                LogUtils.printI(TAG,"setOnItemClickListener---position="+position + ", lastSelectPosition="+lastSelectPosition);
                if(BluetoothBroadCastReceiver.lastPlaybackState != PlayStatus.PLAY.getValue()) {
                    if (position != lastSelectPosition) {
                        isPlaying = true;
                        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.USB_MUSIC_PLAY_AND_REAST);
                        messageEvent.data = position;
                        EventBus.getDefault().post(messageEvent);
                    }
                }else {
                    showStopBluetoothMusicDialog();
                }
            }
        });
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
        playCV.setOnClickListener(v -> {
            playCV.setEnabled(false);
            if(BluetoothBroadCastReceiver.lastPlaybackState != PlayStatus.PLAY.getValue()){
                if(usbConnected) {
                    isPlaying = true;
                    startPlay(lastSelectPosition);
                }
            }else{
                showStopBluetoothMusicDialog();
            }
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if(playCV!=null){
                    playCV.setEnabled(true);
                }
            },1000);
        });

        previousCV.setOnClickListener(v -> {
            previousCV.setEnabled(false);
            if(BluetoothBroadCastReceiver.lastPlaybackState != PlayStatus.PLAY.getValue()) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.HOME_MUSIC_PLAY_PREVIOUS));
            }else{
                showStopBluetoothMusicDialog();
            }
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if(previousCV!=null){
                    previousCV.setEnabled(true);
                }
            },1000);
        });
        nextCV.setOnClickListener(v -> {
            LogUtils.printI(TAG, "next-----");
            nextCV.setEnabled(false);
            if(BluetoothBroadCastReceiver.lastPlaybackState != PlayStatus.PLAY.getValue()) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.HOME_MUSIC_PLAY_NEXT));
            }else{
                showStopBluetoothMusicDialog();
            }
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if(nextCV!=null){
                    nextCV.setEnabled(true);
                }
            },1000);
        });

    }

    private void showStopBluetoothMusicDialog() {
        new XPopup.Builder(getActivity()).hasStatusBar(false).hasShadowBg(false)
                .asCustom(new HintMessageDialog(getActivity(),getResources().getString(R.string.please_stop_bluetooth_music)))
                .show();
    }

    @Override
    protected void setupData() {
        super.setupData();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.LOAD_USB_MUSIC_LIST));

        buttonValidClick = new ButtonValidClick();
    }

    private void setUnconnectedStatusView() {
        if(usbIV == null){
            return;
        }
        IconUtils.setColor(usbIV, getResources().getColor(R.color.cl_ffffff));
        bluetoothCV.setCardBackgroundColor(getResources().getColor(R.color.cl_carview));
    }

    private void setConnectedStatusView() {
        if(usbIV == null){
            return;
        }
        IconUtils.setColor(usbIV, getResources().getColor(R.color.cl_item_selected));
        bluetoothCV.setCardBackgroundColor(getResources().getColor(R.color.ffffff));
    }

    private void updateMusicDuration(long usbMusicDuration) {
        if(durationTV!=null){

            currentMusicDuration = usbMusicDuration;
            seekBar.setMax(usbMusicDuration);

            long minutes = TimeUnit.MILLISECONDS.toMinutes(usbMusicDuration);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(usbMusicDuration) - TimeUnit.MINUTES.toSeconds(minutes);
            durationTV.setText(String.format("%02d:%02d", minutes, seconds));
        }
    }

    private void startPlay(int position) {
        if (isPlaying) {
            if (playIV != null) {
                playIV.setImageResource(R.drawable.ic_music_pause);
            }
            MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.MUSIC_PLAY);
            messageEvent.data = position;
            EventBus.getDefault().post(messageEvent);
        } else {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.MUSIC_PAUSE));
            if (playIV != null) {
                playIV.setImageResource(R.drawable.ic_music_play);
            }
        }
    }

    private void resetAllItemStatus() {
        for (int i = 0; i < usbMusicAdapter.getItemCount(); i++) {
            CarMusicItem item = usbMusicAdapter.getItem(i);
            if (item.isSelected()) {
                item.setSelected(false);
                usbMusicAdapter.notifyItemChanged(i);
            }
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_usb_music;
    }


    private void updateData(Object data) {
        LogUtils.printI(TAG, "updateData----data=" + data);
        if (data instanceof MusicListEntity) {
            try {
                MusicListEntity entity = (MusicListEntity) data;
                lastSelectPosition = entity.getPlayPosition();
                List<CarMusicItem> list = entity.getList();
                if (list != null && !list.isEmpty()) {
                    usbConnected = true;

                    CarMusicItem musicItem = list.get(lastSelectPosition);
                    musicItem.setSelected(true);
                    usbMusicAdapter.setNewData(list);

                    musicListRV.scrollToPosition(lastSelectPosition);
                    playIV.setImageResource(R.drawable.ic_music_play);
                    musicTitleTV.setText(musicItem.getTitle());
                } else {
                    usbMusicAdapter.setNewData(null);
                    CarMusicItem musicItem = new CarMusicItem();
                    musicItem.setTitle(getResources().getString(R.string.not_play_music));
                    musicItem.setLyric("");
                    playIV.setImageResource(R.drawable.ic_music_play);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
