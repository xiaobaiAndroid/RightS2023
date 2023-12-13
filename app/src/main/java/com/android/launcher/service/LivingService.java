package com.android.launcher.service;

import android.app.Instrumentation;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.launcher.ActivityRouter;
import com.android.launcher.MainActivity;
import com.android.launcher.MyApp;
import com.android.launcher.bluetooth.BluetoothTelephonyHelper;
import com.android.launcher.bluetooth.PhoneBookDataPuller;
import com.android.launcher.music.CarMusicItem;
import com.android.launcher.receiver.BluetoothScanModeReceiver;
import com.android.launcher.receiver.USBBroadCastReceiver;
import com.android.launcher.serialport.sender.Can1E5ConfigSender;
import com.android.launcher.serialport.sender.Can35DConfigSender;
import com.android.launcher.serialport.sender.CdPlayerSender;
import com.android.launcher.serialport.sender.DeviceIdSender;
import com.android.launcher.serialport.sender.TimerSender;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.HandlerCanData;
import com.android.launcher.usbdriver.HandlerLeftData;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.android.launcher.usbdriver.UsbManagerBenz;
import com.android.launcher.usbdriver.UsbReadIml;
import com.android.launcher.util.OriginalMeterOperationHelper;
import com.android.launcher.ac.controller.ControlCenterFrameLayout;

import module.common.MessageEvent;

import com.android.launcher.R;
import com.android.launcher.can.CanReceiverManager;
import com.android.launcher.can.CanSenderManager;
import com.android.launcher.lin.LinSenderManager;
import com.android.launcher.music.MusicOTGReceiver;
import com.android.launcher.type.CarType;
import com.android.launcher.util.APKUtil;
import module.common.utils.AppUtils;
import module.common.utils.DensityUtil;
import module.common.utils.FMHelper;
import com.android.launcher.util.FuncUtil;
import module.common.utils.GaodeCarMapHelper;
import module.common.utils.LogUtils;
import com.android.launcher.util.LogcatHelper;

import module.common.utils.SPUtils;
import module.common.utils.StringUtils;

import com.android.launcher.util.SendUsbToCan;
import com.android.launcher.wifi.WifiReceiver;
import com.bzf.module_db.repository.ContactsRepository;
import com.daimajia.numberprogressbar.NumberProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//import module.assistant.CarSiriService;

/**
 * @date： 2023/10/16
 * @author: 78495
*/
public class LivingService extends Service {

    private static final String TAG = LivingService.class.getSimpleName();

    //唯一的通知通道的ID
    private static String notificationChannelId = "notification_channel_id_01";

    private static final int NOTIFICATION_ID = 1111;

    private boolean isRunning = false;

    private long lastUsbConnectTime = 0;

    private Handler mAgainUsbHandler = new Handler();

    private PhoneBookDataPuller phoneBookDataPuller;

    //是否通过蓝牙加载电话本
    private boolean isLoadingContacts = true;
    private View mFloatingView = null;
    private View mMeterFloatingView = null;

    private UsbDevice otgDevice = null;
    private BluetoothScanModeReceiver scanModeReceiver;
    private View updateVersionFloating;
    private NumberProgressBar apkUpdateProgressBar;

    //正在版本更新
    public static volatile boolean isDownloading = false;

    private ControlCenterFrameLayout controlCenterFrameLayout;

    private ScheduledExecutorService clockrService;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public static volatile String currentTime = "";

    private CanReceiverManager canReceiverManager;
    private CanSenderManager canSenderManager;
    private LinSenderManager linSenderManager;

    //汽车类型
    public static volatile CarType carType = CarType.S500;
    public static volatile BluetoothDevice connectBlueDevice;
    private View mStatusFloatingView;

    private WifiReceiver wifiReceiver;

    private TimerSender timerSender;
    private DeviceIdSender deviceIdSender;
    private Can35DConfigSender can35DConfigSender;

    private Can1E5ConfigSender can1E5ConfigSender;
    private CdPlayerSender cdPlayerSender;

    private ExecutorService musicSendTask;

    public LivingService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //服务被系统kill掉之后重启进来的
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.printI(TAG, "onCreate---------");
        lastUsbConnectTime = 0;

        isLoadingContacts = true;
        isDownloading = false;

        canReceiverManager = new CanReceiverManager(this.getApplicationContext());
        canReceiverManager.registerListener();

        canSenderManager = new CanSenderManager();

        linSenderManager = new LinSenderManager();

        cdPlayerSender = new CdPlayerSender();

        timerSender = new TimerSender();
        deviceIdSender = new DeviceIdSender();
        can35DConfigSender = new Can35DConfigSender();
        can1E5ConfigSender = new Can1E5ConfigSender();

        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= Build.VERSION_CODES.O) {
            // 针对 Android 8.0 或更高版本的操作
            startForegroundWithNotification();
        }

        musicSendTask = Executors.newSingleThreadExecutor();


        phoneBookDataPuller = new PhoneBookDataPuller(this.getApplicationContext());

        loadCarType();

        try {
            initUSB();
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.printI(TAG, "onCreate---------" + e.getMessage());
        }

        isRunning = true;
        new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("LivingService", "living---");
            }
        }).start();

        startLoadingContacts();

        scanModeReceiver = BluetoothScanModeReceiver.register(this);
        linSenderManager.registerListener();

        new Handler(Looper.getMainLooper()).postDelayed(() -> FuncUtil.setBluetooth(), 5000);

        clearUnnecessaryLog();

        startCheckUpdateVersionTask();

        wifiReceiver = new WifiReceiver();
        WifiReceiver.register(this,wifiReceiver);

        startClock();

        new Handler(Looper.getMainLooper()).postDelayed(() -> sendBroadcast(new Intent("com.xyauto.car360ctrl.action") .putExtra("car360cmd", 59)),8000);

    }

    private void loadCarType() {
        new Thread(() -> {
            try {
                int type = SPUtils.getInt(getApplicationContext(),SPUtils.SP_CAR_TYPE,CarType.S500.ordinal());
                if(type == CarType.S65.ordinal()){
                    carType = CarType.S65;
                }else if(type == CarType.S300.ordinal()){
                    carType = CarType.S300;
                }else if(type == CarType.S350.ordinal()){
                    carType = CarType.S350;
                }else if(type == CarType.S400.ordinal()){
                    carType = CarType.S400;
                }else if(type == CarType.S450.ordinal()){
                    carType = CarType.S450;
                }else if(type == CarType.S500.ordinal()){
                    carType = CarType.S500;
                }else if(type == CarType.S600.ordinal()){
                    carType = CarType.S600;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startClock() {
        clockrService = Executors.newSingleThreadScheduledExecutor();
        clockrService.scheduleAtFixedRate(() -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            currentTime = simpleDateFormat.format(new Date());
            MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CLOCK);
            messageEvent.data = currentTime;
            EventBus.getDefault().post(messageEvent);
        },0,60, TimeUnit.SECONDS);
    }


    private void startCheckUpdateVersionTask() {
        executorService.scheduleAtFixedRate(() -> APKUtil.checkUpdate(), 6, 10, TimeUnit.SECONDS);
    }


    private void clearUnnecessaryLog() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                LogcatHelper.getInstance(getApplicationContext()).clearUnnecessaryLog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * @description: 获取U盘设备
     * @createDate: 2023/6/9
     */
    private void getOTGDevice() {

        try {
            UsbManager mUsbManager = (UsbManager) MyApp.getGlobalContext().getSystemService(Context.USB_SERVICE);
            List<UsbDevice> allUsbDevice = UsbManagerBenz.getAllUsbDevice(mUsbManager);

            LogUtils.printI(TAG, "getOTGDevice-----allUsbDevice=" + allUsbDevice.size());

            for (int i = 0; i < allUsbDevice.size(); i++) {
                UsbDevice usbDevice = allUsbDevice.get(i);
                if (usbDevice != null) {
                    LogUtils.printI(TAG, "getOTGDevice-----usbDevice---name=" + usbDevice.getDeviceName() + ", id=" + usbDevice.getVendorId());
                    if (usbDevice.getVendorId() != 6790) { //6790: 数据传输的USB
                        otgDevice = usbDevice;
                        break;
                    }
                }
            }

            if (otgDevice != null) {
                LogUtils.printI(TAG, "getOTGDevice-----otgDevice---name=" + otgDevice.getDeviceName() + ", id=" + otgDevice.getVendorId());
                MusicOTGReceiver.registerReceiver(this.getApplicationContext());
                new Handler().postDelayed(() -> MusicOTGReceiver.initUsbAuth(otgDevice), 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startLoadingContacts() {
        new Thread(() -> {
            while (isLoadingContacts) {
                try {
                    if (phoneBookDataPuller == null) {
                        return;
                    }
                    BluetoothDevice connectDevice = phoneBookDataPuller.getConnectDevice();
                    if (connectDevice != null) {
                        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.GET_BLUETOOTH_CONTACTS);
                        messageEvent.data = connectDevice;
                        EventBus.getDefault().post(messageEvent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.type == MessageEvent.Type.USB_ACCESSORY_DETACHED) {
            LogUtils.printI(TAG, "收到USB设备断开的消息");
            try {
                SPUtils.getBoolean(this.getApplicationContext(), SPUtils.USB_OTG_STATUS, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event.type == MessageEvent.Type.USB_ACCESSORY_ATTACHED) {
            LogUtils.printI(TAG, "收到USB设备连接的消息");

            long currentTimeMillis = System.currentTimeMillis();
            if (lastUsbConnectTime == 0) {
                lastUsbConnectTime = currentTimeMillis;
                againUsbinit();
            } else {
                long interval = currentTimeMillis - lastUsbConnectTime;
                if (interval <= 500) { //间隔小于500，不处理
                    return;
                }
                againUsbinit();
                lastUsbConnectTime = currentTimeMillis;
            }
        } else if (event.type == MessageEvent.Type.PHONE_ANSWER) {
            LogUtils.printI(TAG, "接听电话");
            acceptCall();
        } else if (event.type == MessageEvent.Type.PHONE_CLOSE) {
            LogUtils.printI(TAG, "挂断电话");
            rejectCall();
        } else if (event.type == MessageEvent.Type.BLUETOOTH_CONNECTED) {
            try {
                LogUtils.printI(TAG, "BLUETOOTH_CONNECTED-----");
                BluetoothDevice bluetoothDevice;
                if (event.data == null) {
                    bluetoothDevice = phoneBookDataPuller.getConnectDevice();
                } else {
                    bluetoothDevice = (BluetoothDevice) event.data;
                }
                if (!phoneBookDataPuller.isLoading() && bluetoothDevice != null) {
                    phoneBookDataPuller.getData(bluetoothDevice);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(event.type == MessageEvent.Type.GET_BLUETOOTH_CONTACTS){
            LogUtils.printI(TAG, "GET_BLUETOOTH_CONTACTS-----");
            try {
                BluetoothDevice bluetoothDevice;
                if (event.data == null) {
                    bluetoothDevice = phoneBookDataPuller.getConnectDevice();
                } else {
                    bluetoothDevice = (BluetoothDevice) event.data;
                }
                if (!phoneBookDataPuller.isLoading() && bluetoothDevice != null) {
                    phoneBookDataPuller.getData(bluetoothDevice);
                }
                sendBluetoothConnectedToLeft();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (event.type == MessageEvent.Type.CONTACTS_PULL_FINISH) {
            isLoadingContacts = false;
        } else if (event.type == MessageEvent.Type.SHOW_FLOATING) {
            LogUtils.printI(TAG, "显示悬浮窗");

        } else if (event.type == MessageEvent.Type.CLOSE_FLOATING) {
            LogUtils.printI(TAG, "关闭悬浮窗");
//            closeFloating();
        } else if (event.type == MessageEvent.Type.LOAD_USB_OTG) {
            LogUtils.printI(TAG, "MOUSE_CONNECT-----getOTGDevice");
            getOTGDevice();
        } else if (event.type == MessageEvent.Type.SHOW_METER_TOUCH_FLOATING) {
            MyApp.propCar = true;
            showMeterTouchFloating();
        } else if (event.type == MessageEvent.Type.CLOSE_METER_TOUCH_FLOATING) {
            MyApp.propCar = false;
            closeMeterTouchFloating();
        } else if (event.type == MessageEvent.Type.BLUETOOTH_DISCONNECTED) { //蓝牙失去连接
            LogUtils.printI(TAG, "BLUETOOTH_DISCONNECTED-----clearContacts");
            clearContacts();
        } else if (event.type == MessageEvent.Type.APK_DOWNLOAD_SHOW) {
            try {
                FMHelper.finishFM(getApplicationContext());
                GaodeCarMapHelper.finish(getApplicationContext());
                showApkDownloadView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event.type == MessageEvent.Type.APK_DOWNLOAD_PROGRESS) {
            try {
                int progress = (int) event.data;
                updateDownloadProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event.type == MessageEvent.Type.APK_INSTALL) {
            installApk();
        } else if (event.type == MessageEvent.Type.UPLOAD_FAIL_SERVER_NOT_RESPONSE) {
            try {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_fail_server_not_response), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(event.type == MessageEvent.Type.SERIAL_PORT_CONNECT){
            //串口开始通讯
            if(canSenderManager!=null){
                canSenderManager.start();
            }
        }else if(event.type == MessageEvent.Type.UPDATE_MUSIC_INFO){
            if (event.data instanceof CarMusicItem) {
                try {
                    CarMusicItem musicItem = (CarMusicItem) event.data;
                    sendMusicInfoToLeft(musicItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendMusicInfoToLeft(CarMusicItem musicItem) {
        if(musicSendTask == null){
            return;
        }
        musicSendTask.execute(() -> {
            try {
                String title = StringUtils.stringToHex(musicItem.getTitle());
                if(title!=null){
                    String titleSendCmd = "AABB38" +  title + "CCDD";
                    SendHelperLeft.handler(titleSendCmd);
                }
                String lyric = StringUtils.stringToHex(musicItem.getLyric());
                if(lyric!=null){
                    String lyricSendCmd = "AABB39" +  lyric + "CCDD";
                    SendHelperLeft.handler(lyricSendCmd);
                }
                LogUtils.printI(TAG, "musicTitle="+musicItem.getTitle() +",lyric="+musicItem.getLyric()+", hexTitle="+title+", hexLyric="+lyric);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void sendBluetoothConnectedToLeft() {
        new Thread(() -> {
            //蓝牙连接
            try {
                String cmd = "AABB41CCDD";
                SendHelperLeft.handler(cmd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void installApk() {
        closeVersionUpdateFloating();
        try {
            LogUtils.printI(TAG, "------完成 安装------------");
            Intent intent = new Intent("xy.android.silent.install");
            intent.putExtra("ins_apk_pkgname", "com.android.launcher");
            intent.putExtra("ins_apk_pathname", "/storage/emulated/0/right.apk");
            intent.putExtra("force_apk_state", 1);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isDownloading = false;
        }
    }

    private void updateDownloadProgress(int progress) {
        if (updateVersionFloating != null) {
            if (apkUpdateProgressBar != null) {
                apkUpdateProgressBar.setProgress(progress);
            }
        }
    }

    private void showApkDownloadView() {
        try {
            isDownloading = true;

            closeVersionUpdateFloating();
            WindowManager.LayoutParams params;
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            updateVersionFloating = LayoutInflater.from(this).inflate(R.layout.layout_update_apk, null);

            apkUpdateProgressBar = updateVersionFloating.findViewById(R.id.progressBar);

            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                    PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.CENTER;
            params.dimAmount = 0.3f;
            windowManager.addView(updateVersionFloating, params);
        } catch (Exception e) {
            e.printStackTrace();
            isDownloading = false;
        }
    }

    private void closeVersionUpdateFloating() {
        if (updateVersionFloating != null) {
            try {
                WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                windowManager.removeView(updateVersionFloating);
                updateVersionFloating = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void clearContacts() {
        new Thread(() -> {
            try {
                ContactsRepository.getInstance().removeAll(getApplicationContext(), AppUtils.getDeviceId(getApplicationContext()));
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.UPDATE_CONTACTS_DATA));

                //蓝牙断开
                String cmd = "AABB40CCDD";
                SendHelperLeft.handler(cmd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void closeMeterTouchFloating() {
        if (mMeterFloatingView != null) {
            try {
                WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                windowManager.removeView(mMeterFloatingView);
                mMeterFloatingView = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showMeterTouchFloating() {
        try {
            closeMeterTouchFloating();
            WindowManager.LayoutParams params;
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            mMeterFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_meter_floating_window, null);

            mMeterFloatingView.findViewById(R.id.homeIV).setOnClickListener(v -> {
                ActivityRouter.toNavigation(this);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.CLOSE_METER_TOUCH_FLOATING));
            });
            mMeterFloatingView.findViewById(R.id.backIV).setOnClickListener(v -> {
                OriginalMeterOperationHelper.back();
            });
            mMeterFloatingView.findViewById(R.id.enterTV).setOnClickListener(v -> {
                OriginalMeterOperationHelper.enter();
            });
            mMeterFloatingView.findViewById(R.id.upIV).setOnClickListener(v -> {
                OriginalMeterOperationHelper.upDirection();
            });
            mMeterFloatingView.findViewById(R.id.downIV).setOnClickListener(v -> {
                OriginalMeterOperationHelper.downDirection();
            });

            mMeterFloatingView.findViewById(R.id.leftIV).setOnClickListener(v -> {
                OriginalMeterOperationHelper.leftDirection();
            });

            mMeterFloatingView.findViewById(R.id.rightIV).setOnClickListener(v -> {
                OriginalMeterOperationHelper.rightDirection();
            });
            mMeterFloatingView.findViewById(R.id.leftRotateIv).setOnClickListener(v -> {
                OriginalMeterOperationHelper.leftRotate();
            });
            mMeterFloatingView.findViewById(R.id.rightRotateIv).setOnClickListener(v -> {
                OriginalMeterOperationHelper.rightRotate();
            });


            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.TOP | Gravity.RIGHT;
            params.x = DensityUtil.dip2px(this.getApplicationContext(), 20);
            params.y = DensityUtil.dip2px(this.getApplicationContext(), 100);
            windowManager.addView(mMeterFloatingView, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rejectCall() {
        BluetoothTelephonyHelper.handUp(this.getApplicationContext());
    }

    private void acceptCall() {
        BluetoothTelephonyHelper.answer(this.getApplicationContext());
    }

    private void againUsbinit() {
        LogUtils.printI(TAG, "重新初始化USB ------");

        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.INIT_USB));

        UsbReadIml.getInstance().disconnect();
        USBBroadCastReceiver.usbRegisterReceiver(LivingService.this.getApplicationContext());
        mAgainUsbHandler.postDelayed(() -> UsbManagerBenz.init(), 100);

    }

    private void initUSB() {
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.INIT_USB));
        USBBroadCastReceiver.usbRegisterReceiver(this.getApplicationContext());

        FuncUtil.initSerialHelper();
        try {
            timerSender.start();
            deviceIdSender.start();
            can35DConfigSender.start();
            can1E5ConfigSender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UsbManagerBenz.init();
            }
        }, 2000);

        if(canSenderManager!=null){
            canSenderManager.registerListener();
        }

        new Handler().postDelayed(() -> {
            try {
                if(cdPlayerSender!=null){
                    cdPlayerSender.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        },2000);

    }

    /**
     * 开启前景服务并发送通知
     */
    private void startForegroundWithNotification() {
        //8.0及以上注册通知渠道
        createNotificationChannel();
        Notification notification = createForegroundNotification();
        //将服务置于启动状态 ,NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID, notification);
        //发送通知到状态栏
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    /**
     * 创建服务通知
     */
    private Notification createForegroundNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), notificationChannelId);
        //通知小图标
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        //设置通知显示的时间
        builder.setWhen(System.currentTimeMillis());
        //设定启动的内容
        Intent activityIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //设置为进行中的通知
        builder.setOngoing(true);
        //创建通知并返回
        return builder.build();
    }

    /**
     * 创建通知渠道
     */
    private void createNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //用户可见的通道名称
            String channelName = "Foreground Service Notification";
            //通道的重要程度
//            int importance = NotificationManager.IMPORTANCE_HIGH;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            //构建通知渠道
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId,
                    channelName, importance);
            notificationChannel.setDescription("Channel description");
            //LED灯
            notificationChannel.enableLights(false);
//            notificationChannel.lightColor = Color.RED;
            //震动
//            notificationChannel.vibrationPattern = longArrayOf(0,1000,500,1000)
            notificationChannel.enableVibration(false);
            //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    @Override
    public void onDestroy() {
       new Thread(() -> {
           try {
               String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.STOP_MAP_NAV.getTypeValue() + SerialPortDataFlag.END_FLAG;
               LogUtils.printI(MainActivity.class.getSimpleName(), "STOP_MAP_NAV---send=" + send +", length="+send.length());
               SendHelperLeft.handler(send);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }).start();


        isRunning = false;
        closeMeterTouchFloating();
        closeVersionUpdateFloating();

        FMHelper.finishFM(this);

        try {
            if(musicSendTask!=null){
                musicSendTask.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        UsbReadIml.getInstance().disconnect();

        try {
            try {
                if (FuncUtil.serialHelperttl != null) {
                    FuncUtil.serialHelperttl.close();
                    FuncUtil.serialHelperttl = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (FuncUtil.serialHelperttl3 != null) {
                FuncUtil.serialHelperttl3.close();
                FuncUtil.serialHelperttl3 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null) {
                Log.e("LivingService", e.getMessage());
            }
        }
        try {

            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isLoadingContacts = false;

        if(cdPlayerSender!=null){
            cdPlayerSender.destroy();
        }

        super.onDestroy();
        Log.i("LivingService", "onDestroy---------");
        stopForeground(true);

        BluetoothScanModeReceiver.unregister(this, scanModeReceiver);
        isDownloading = false;
        try {
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(clockrService!=null){
                clockrService.shutdown();
                clockrService = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(canReceiverManager!=null){
            canReceiverManager.unregisterListener();
            canReceiverManager.release();
            canReceiverManager.reset();
        }
        if(linSenderManager!=null){
            linSenderManager.unregisterListener();
            linSenderManager.release();
        }

        if(canSenderManager!=null){
            canSenderManager.release();
        }

        WifiReceiver.unregister(this,wifiReceiver);

        HandlerLeftData.lastCan20BData = "";
        HandlerLeftData.lastCanBBData = "";
        HandlerLeftData.lastCanBcData = "";
        HandlerLeftData.lastCan1E5StatusData = "";

        try {
            timerSender.destroy();
            deviceIdSender.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HandlerCanData.cdStatusIsSend = false;
    }

    public static void startLivingService(Context context) {
        try {
            MyApp.livingServerStop = false;
            Intent intent = new Intent(context, LivingService.class);
            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            }else{
                context.startService(intent);
            }

//            CarSiriService.startSiriService(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopLivingService(Context context) {
        try {
            MyApp.livingServerStop = true;
            Intent intent = new Intent(context, LivingService.class);
            context.stopService(intent);

//            CarSiriService.stopSiriService(context);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null) {
                Log.e("LivingService", e.getMessage());
            }

        }
    }
}
