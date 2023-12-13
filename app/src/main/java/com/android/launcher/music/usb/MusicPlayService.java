package com.android.launcher.music.usb;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;

import com.android.launcher.music.CarMusicItem;
import com.android.launcher.music.MusicListEntity;
import com.android.launcher.music.bluetooth.PlayStatus;
import com.android.launcher.receiver.BluetoothBroadCastReceiver;
import com.android.launcher.util.AudioUtils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import module.common.MessageEvent;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;

/**
 * @description:
 * @createDate: 2023/6/9
 */
public class MusicPlayService extends Service implements CarMusicServerIView, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = MusicPlayService.class.getSimpleName();

    private List<CarMusicItem> list = new ArrayList<>();

    private int lastPlayPosition = 0;

    private MediaPlayer mMediaPlayer = new MediaPlayer();

    public static boolean isPlaying = false;

    private CarMusicServerPresenter mPresenter;


    private String mUsbPath;
    private ScheduledExecutorService durationTask;
    private ExecutorService executorService;

    private boolean usbConnected;
    private ContentObserver mContentObserver;

    public MusicPlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendDataToActivity(list);
        LogUtils.printI(TAG, "onStartCommand----------");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.printI(TAG, "onCreate----------");

        mPresenter = new CarMusicServerPresenter(this, this);

        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int mediaVolume = AudioUtils.getMediaVolume(this.getApplicationContext());
        LogUtils.printI(TAG, "mediaVolume=" + mediaVolume);

        mUsbPath = SPUtils.getString(this, SPUtils.SP_USB_PATH);
        if (mPresenter != null) {
            mPresenter.loadData(mUsbPath);
        }

        try {
            durationTask = Executors.newSingleThreadScheduledExecutor();
            executorService = Executors.newCachedThreadPool();


            executorService.execute(() -> lastPlayPosition = SPUtils.getInt(getApplicationContext(),SPUtils.SP_USB_MUSIC_PLAY_POS,0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mContentObserver = new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange, Uri uri) {
                    super.onChange(selfChange, uri);
                    LogUtils.printI(TAG, "ContentObserver---selfChange="+selfChange +",uri="+uri);
                    if(selfChange){
                        Set<String> volumeNames = MediaStore.getExternalVolumeNames(getApplicationContext());
                        Iterator<String> iterator = volumeNames.iterator();
                        while (iterator.hasNext()){
                            String volumeName = iterator.next();

                            Uri volumeAudioUri = MediaStore.Audio.Media.getContentUri(volumeName);
                            LogUtils.printI(TAG, "ContentObserver----volumeName="+volumeName+ ", volumeAudioUri="+volumeAudioUri);
                            String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE};

                            Cursor cursor = getContentResolver().query(volumeAudioUri, projection, null, null);
                            LogUtils.printI(TAG, "ContentObserver----cursor="+cursor);
                        }
                    }


                }
            };

            getContentResolver().registerContentObserver(
                    MediaStore.AUTHORITY_URI, true, mContentObserver);
        }

    }

    private void permissionRequest() {
        
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        if (event.type == MessageEvent.Type.MUSIC_PLAY) {
            LogUtils.printI(MusicPlayService.class.getSimpleName(), "onMessageEvent------MUSIC_PLAY---data=" + event.data);
            try {
                lastPlayPosition = (int) event.data;
                if (lastPlayPosition >= 0 && lastPlayPosition < list.size()) {
                    CarMusicItem carMusicItem = list.get(lastPlayPosition);
                    play(carMusicItem);

                    if(executorService!=null){
                        executorService.execute(() -> SPUtils.putInt(getApplicationContext(),SPUtils.SP_USB_MUSIC_PLAY_POS,lastPlayPosition));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event.type == MessageEvent.Type.MUSIC_PAUSE) {
            LogUtils.printI(MusicPlayService.class.getSimpleName(), "onMessageEvent------event=" + event.type.name());
            try {
                if (lastPlayPosition >= 0 && lastPlayPosition < list.size()) {
                    pause();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event.type == MessageEvent.Type.READ_OTG_MUSIC) {
            LogUtils.printI(MusicPlayService.class.getSimpleName(), "onMessageEvent------event=" + event.type.name());
            try {
                String usbPath = (String) event.data;
                SPUtils.putString(this, SPUtils.SP_USB_PATH,usbPath);
                if(mPresenter!=null){
                    mPresenter.loadData(usbPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(event.type == MessageEvent.Type.MUSIC_RESUME){
            LogUtils.printI(MusicPlayService.class.getSimpleName(), "onMessageEvent------event=" + event.type.name());
            resumeMusic();
        }else if (event.type == MessageEvent.Type.USB_ACCESSORY_DETACHED) {
            LogUtils.printI(TAG, "收到USB设备断开的消息");
            try {
                stopMusic();
                if(mMediaPlayer!=null){
                    mMediaPlayer.reset();
                }
                list.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(event.type == MessageEvent.Type.STOP_USB_MUSIC){
            try {
                if (lastPlayPosition >= 0 && lastPlayPosition < list.size()) {
                    pause();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(event.type == MessageEvent.Type.LOAD_USB_MUSIC_LIST){
            if(mPresenter!=null){
                mPresenter.loadData(mUsbPath);
            }
        }else if(event.type == MessageEvent.Type.BLUETOOTH_MUSIC_PLAY){
            if (lastPlayPosition >= 0 && lastPlayPosition < list.size()) {
                pause();
            }
        }else if(event.type == MessageEvent.Type.HOME_MUSIC_PLAY_PREVIOUS){
            if(BluetoothBroadCastReceiver.lastPlaybackState != PlayStatus.PLAY.getValue()){
                playPrevious();
            }
        }else if(event.type == MessageEvent.Type.HOME_MUSIC_PLAY_NEXT){
            LogUtils.printI(TAG,"onMessageEvent---HOME_MUSIC_PLAY_NEXT---lastPlaybackState="+BluetoothBroadCastReceiver.lastPlaybackState +", usbConnected="+usbConnected);
            if(BluetoothBroadCastReceiver.lastPlaybackState != PlayStatus.PLAY.getValue()){
                playNext();
            }
        }else if(event.type == MessageEvent.Type.USB_MUSIC_PLAY_AND_REAST){
            try {
                lastPlayPosition = (int) event.data;
                if (lastPlayPosition >= 0 && lastPlayPosition < list.size()) {
                    CarMusicItem carMusicItem = list.get(lastPlayPosition);
                    play(carMusicItem);
                    if(executorService!=null){
                        executorService.execute(() -> SPUtils.putInt(getApplicationContext(),SPUtils.SP_USB_MUSIC_PLAY_POS,lastPlayPosition));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void playNext() {
        try {
            if (list.isEmpty()) {
                return;
            }
            if (lastPlayPosition >= (list.size() - 1)) {
                lastPlayPosition = 0;
            }
            ++lastPlayPosition;
            CarMusicItem musicItem = list.get(lastPlayPosition);
            LogUtils.printE(TAG, "播放下一首，musicItem=" + musicItem.getTitle() +", lastPlayPosition="+lastPlayPosition);
            play(musicItem);

            executorService.execute(() -> SPUtils.putInt(getApplicationContext(),SPUtils.SP_USB_MUSIC_PLAY_POS,lastPlayPosition));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playPrevious() {
        try {
            if (list.isEmpty()) {
                return;
            }
            if (lastPlayPosition < 0) {
                lastPlayPosition = list.size() - 1;
            }
            --lastPlayPosition;
            CarMusicItem musicItem = list.get(lastPlayPosition);
            LogUtils.printE(TAG, "播放上一首，musicItem=" + musicItem);
            play(musicItem);

            executorService.execute(() -> SPUtils.putInt(getApplicationContext(),SPUtils.SP_USB_MUSIC_PLAY_POS,lastPlayPosition));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pause() {
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void play(CarMusicItem carMusicItem) {
        String path = carMusicItem.getPath();

        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.USB_MUSIC_START_PLAY);
        messageEvent.data = lastPlayPosition;
        EventBus.getDefault().post(messageEvent);

        LogUtils.printI(MusicPlayService.class.getSimpleName(), "play------path=" + path + ", position=" + lastPlayPosition);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.STOP_BLUETOOTH_MUSIC));
        isPlaying = true;
        try {
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.reset();
            // 指定要播放的音频文件路径
            mMediaPlayer.setDataSource(path);

            // 准备并开始播放音频文件
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);

            long duration = mMediaPlayer.getDuration();
            LogUtils.printI(TAG,"duration="+duration);

            MessageEvent messageEvent1 = new MessageEvent(MessageEvent.Type.USB_MUSIC_TOTAL_DURATION);
            messageEvent1.data = duration;
            EventBus.getDefault().post(messageEvent1);
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resumeMusic(){
        try {
            isPlaying = true;
            mMediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mMediaPlayer != null) {
                if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                }
                mMediaPlayer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mPresenter.release();
        } catch (Exception e) {
            e.printStackTrace();
        }

        list.clear();
        isPlaying = false;
        try {
            if(durationTask!=null){
                durationTask.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(executorService!=null){
                executorService.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            handler.removeCallbacks(loadDataRunnable);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    public static void startMusicService(Context context) {
        try {
            Intent intent = new Intent(context, MusicPlayService.class);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopMusicService(Context context) {
        try {
            Intent intent = new Intent(context, MusicPlayService.class);
            context.stopService(intent);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void getDataResult(List<CarMusicItem> list) {
        this.list = list;
        try {
            if (list != null && !list.isEmpty()) {
                if(lastPlayPosition > list.size()){
                    lastPlayPosition = 0;
                }

                executorService.execute(() -> executorService.execute(() -> SPUtils.putInt(getApplicationContext(),SPUtils.SP_USB_MUSIC_PLAY_POS,lastPlayPosition)));
            } else {
                stopMusic();
            }
            sendDataToActivity(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendDataToActivity(List<CarMusicItem> list) {
        MusicListEntity musicListEntity = new MusicListEntity();
        musicListEntity.setList(list);
        musicListEntity.setPlayPosition(lastPlayPosition);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.UPDATE_MUSIC_LIST);
        messageEvent.data = musicListEntity;
        EventBus.getDefault().post(messageEvent);
    }

    @Override
    public void getDataFail() {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.setOnCompletionListener(this);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.USB_CURRENT_MUSIC_PROGRESS);
        durationTask.scheduleAtFixedRate(() -> {
            if(mMediaPlayer!=null){
                int currentMusicProgress = mMediaPlayer.getCurrentPosition();

//                LogUtils.printI(TAG,"currentMusicProgress="+currentMusicProgress);
                messageEvent.data = currentMusicProgress;
                EventBus.getDefault().post(messageEvent);
            }
        },1,1, TimeUnit.SECONDS);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // 播放出错，根据错误码做相应异常处理
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                LogUtils.printE(TAG, "未知错误！");
                break;
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                LogUtils.printE(TAG, "格式不支持！");
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                LogUtils.printE(TAG, "服务异常终止！");
                break;
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        LogUtils.printE(TAG, "播放结束");
        playNext();
    }
}
