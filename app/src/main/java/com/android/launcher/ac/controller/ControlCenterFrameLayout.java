package com.android.launcher.ac.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.launcher.R;
import com.android.launcher.ac.temp.TempUtils;
import com.android.launcher.ac.wind.WindUtils;
import com.android.launcher.can.parser.Can007DataParser;
import com.android.launcher.can.parser.Can1e5DataParser;
import com.android.launcher.can.parser.Can20bDataParser;
import com.android.launcher.floating.WindDirectionFloatingWindow;
import com.bzf.module_db.AcKeyStatus;
import com.bzf.module_db.entity.Can007Table;
import com.bzf.module_db.entity.Can1E5Table;
import com.bzf.module_db.entity.Can20BTable;
import com.bzf.module_db.repository.Can007Repository;
import com.bzf.module_db.repository.Can1E5Repository;
import com.bzf.module_db.repository.Can20BRepository;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import module.common.MessageEvent;
import module.common.utils.AppUtils;
import module.common.utils.ButtonUtils;
import module.common.utils.LogUtils;

/**
 * 控制中心的FrameLayout
 *
 * @date： 2023/10/12
 * @author: 78495
 */
public class ControlCenterFrameLayout extends FrameLayout {

    private static final String TAG = ControlCenterFrameLayout.class.getSimpleName();

    private ACAdjustView leftAcAdjustView;
    private ACAdjustView rightAcAdjustView;


    private ACButtomView compressorOffV;
    private ACButtomView autoV;
    private ImageView innerLoopIV;

    private ImageView frontDemistIV;
    private ImageView rearDemistIV;

    private ExecutorService executorService;

    private String deviceId;
    private Can20bDataParser can20bDataParser;
    private Can007DataParser can007DataParser;
    private Can1e5DataParser can1E5DataParser;

    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                setAutoAcStatus();
            } else if (msg.what == 2) {
                setACDataToView();
            } else if (msg.what == 3) {
                setRearDemistDataToView();
            }
        }
    };
    private ImageView backIV;

    private ImageView previousIV;
    private ImageView nextIV;
    private TextView airflowTV;

    public ControlCenterFrameLayout(Context context) {
        this(context, null);
    }

    public ControlCenterFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlCenterFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ControlCenterFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSetup(context);
    }

    private void initSetup(Context context) {
        executorService = Executors.newCachedThreadPool();

        LayoutInflater.from(context).inflate(R.layout.layout_control_center, this, true);

        leftAcAdjustView = findViewById(R.id.driverACAdjustView);
        rightAcAdjustView = findViewById(R.id.frontSeatACAdjustView);


        autoV = findViewById(R.id.autoV);
        innerLoopIV = findViewById(R.id.innerLoopIV);
        compressorOffV = findViewById(R.id.acOffV);

        backIV = findViewById(R.id.backIV);
        previousIV = findViewById(R.id.previousIV);
        nextIV = findViewById(R.id.nextIV);
        airflowTV = findViewById(R.id.airflowTV);
        frontDemistIV = findViewById(R.id.frontDemistIV);
        rearDemistIV = findViewById(R.id.rearDemistIV);

        compressorOffV.setOnClickListener(v -> {
            if (can1E5DataParser != null && can1E5DataParser.isAcOpen()) {
                compressorOffV.setEnabled(false);
                setCompressorOff();
                new Handler(Looper.getMainLooper()).postDelayed(() -> compressorOffV.setEnabled(true), 1000);
            }
        });

        autoV.setOnClickListener(v -> {
            LogUtils.printI(TAG, "can1e5DataParser=" + can1E5DataParser);
            if (can1E5DataParser != null && can1E5DataParser.isAcOpen()) {
                autoV.setEnabled(false);
                setAuto();
                new Handler(Looper.getMainLooper()).postDelayed(() -> autoV.setEnabled(true), 1000);
            }
        });

        airflowTV.setOnClickListener(v -> {
            airflowTV.setEnabled(false);
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_FRAGMENT_AIRFLOW));
            new Handler(Looper.getMainLooper()).postDelayed(() -> airflowTV.setEnabled(true), 1000);
        });


        backIV.setOnClickListener(v -> {
            backIV.setEnabled(false);
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.BACK_EVENT));
            new Handler(Looper.getMainLooper()).postDelayed(() -> backIV.setEnabled(true), 1000);
        });
        previousIV.setOnClickListener(v -> {
            previousIV.setEnabled(false);
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.HOME_MUSIC_PLAY_PREVIOUS));
            new Handler(Looper.getMainLooper()).postDelayed(() -> previousIV.setEnabled(true), 1000);
        });

        nextIV.setOnClickListener(v -> {
            nextIV.setEnabled(false);
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.HOME_MUSIC_PLAY_NEXT));
            new Handler(Looper.getMainLooper()).postDelayed(() -> nextIV.setEnabled(true), 1000);
        });

        setAcAdjustListener();

        loadData();
    }

    private void setAutoAcStatus() {
        if (can1E5DataParser == null && compressorOffV == null) {
            return;
        }
        compressorOffV.setSelected(can1E5DataParser.isCompressorOpen());
        if(can1E5DataParser.isAcOpen()){
            leftAcAdjustView.setAccOff(false);
            rightAcAdjustView.setAccOff(false);

            autoV.setVisibility(View.VISIBLE);
            compressorOffV.setVisibility(View.VISIBLE);
            autoV.setSelected(false);

            LogUtils.printI(TAG, "setAutoAcStatus----isDriveSeatWindAuto=" + can1E5DataParser.isDriveSeatWindAuto() + " ,isFrontSeatWindAuto="+can1E5DataParser.isFrontSeatWindAuto());
            if (can1E5DataParser.isDriveSeatWindAuto() && can1E5DataParser.isFrontSeatWindAuto()) {
                leftAcAdjustView.setAuto(true);
                rightAcAdjustView.setAuto(true);
                autoV.setSelected(true);

            } else if (can1E5DataParser.isDriveSeatWindAuto()) {
                leftAcAdjustView.setAuto(true);
                rightAcAdjustView.setAuto(false);
            } else if (can1E5DataParser.isFrontSeatWindAuto()) {
                leftAcAdjustView.setAuto(false);
                rightAcAdjustView.setAuto(true);
            } else {
                leftAcAdjustView.setAuto(false);
                rightAcAdjustView.setAuto(false);
            }
        }else{
            leftAcAdjustView.setAccOff(true);
            rightAcAdjustView.setAccOff(true);
            autoV.setVisibility(View.INVISIBLE);
            compressorOffV.setVisibility(View.INVISIBLE);
        }
    }

    private void setRearDemistDataToView() {
        if (can007DataParser == null) {
            LogUtils.printE(TAG, "setRearDemistDataToView----can007Table is null");
            return;
        }

        if (can007DataParser.getRearDemistStatus() == AcKeyStatus.BREAKDOWN) {
            //后挡风除雾指示灯闪烁
            ButtonUtils.setBreakdownStatus(rearDemistIV);
        } else if (can007DataParser.getRearDemistStatus() == AcKeyStatus.OPEN) {
            //后挡风除雾指示灯长亮
            ButtonUtils.setSelected(rearDemistIV, true);
        } else {
            rearDemistIV.setVisibility(View.INVISIBLE);
            ButtonUtils.setSelected(rearDemistIV, false);
        }
    }


    //设置压缩机状态
    private void setCompressorOff() {
        if (can1E5DataParser != null) {
            executorService.execute(() -> {
                MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.SET_AC_COMPRESSOR_STATUS);
                messageEvent.data = !can1E5DataParser.isCompressorOpen();
                EventBus.getDefault().post(messageEvent);
            });
        }
    }

    private void setAuto() {
        if (can1E5DataParser != null) {
            LogUtils.printI(TAG, "setAuto--can20bDataParser--leftAutoMode" + can20bDataParser.isLeftAutoMode() + ",reftAutoMode" + can20bDataParser.isRightAutoMode());
            executorService.execute(() -> {
                MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.SET_AC_AUTO_MODE);
                messageEvent.data = !can1E5DataParser.isAutoAcMode();
                EventBus.getDefault().post(messageEvent);
            });
        }
    }

    private void setACDataToView() {
        if (can20bDataParser == null) {
            return;
        }
        if (can20bDataParser.innerLoopIsOpen()) {
            innerLoopIV.setVisibility(View.VISIBLE);
        } else {
            innerLoopIV.setVisibility(View.INVISIBLE);
        }
        if (can20bDataParser.frontDemistIsOpen()) {
            frontDemistIV.setVisibility(View.VISIBLE);
        } else {
            frontDemistIV.setVisibility(View.INVISIBLE);
        }

        int leftWind = WindUtils.cmdToNumber(can20bDataParser.getDriverWind());
        int rightWind = WindUtils.cmdToNumber(can20bDataParser.getFrontSeatWind());
        String leftTemp = TempUtils.cmdToTemp(can20bDataParser.getDriverTemp());
        String rightTemp = TempUtils.cmdToTemp(can20bDataParser.getFrontSeatTemp());
        leftAcAdjustView.setWind(leftWind);
        leftAcAdjustView.setTemp(leftTemp);
        rightAcAdjustView.setWind(rightWind);
        rightAcAdjustView.setTemp(rightTemp);

    }


    private void setAcAdjustListener() {
        leftAcAdjustView.setTouchListener(new ACAdjustView.TouchListener() {


            @Override
            public void onWindIncrease(int wind) {
                sendLeftWindIncrease(wind);
            }

            @Override
            public void onWindReduce(int wind) {
                sendLeftWindReduce(wind);
            }

            @Override
            public void onTempIncrease(String temp) {

                sendLeftTempIncrease(temp);
            }

            @Override
            public void onTempReduce(String temp) {
                sendLeftTempReduce(temp);
            }
        });

        rightAcAdjustView.setTouchListener(new ACAdjustView.TouchListener() {


            @Override
            public void onWindIncrease(int wind) {
                sendRightWindIncrease(wind);
            }

            @Override
            public void onWindReduce(int wind) {
                sendRightWindReduce(wind);
            }

            @Override
            public void onTempIncrease(String temp) {
                sendRightTempIncrease(temp);
            }

            @Override
            public void onTempReduce(String temp) {
                sendRightTempReduce(temp);
            }
        });
    }

    private void sendRightTempReduce(String temp) {
        if (can1E5DataParser == null || !can1E5DataParser.isAcOpen()) {
            return;
        }
        if (can1E5DataParser.isFrontSeatWindAuto() || can1E5DataParser.isAutoAcMode()) {
            return;
        }
        String cmd = TempUtils.tempToCmd(temp);

        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.SET_FRONT_SEAT_TEMP_SUBTRACT);
        messageEvent.data = cmd;
        EventBus.getDefault().post(messageEvent);
    }

    private void sendRightTempIncrease(String temp) {
        if (can1E5DataParser == null || !can1E5DataParser.isAcOpen()) {
            return;
        }
        if (can1E5DataParser.isFrontSeatWindAuto() || can1E5DataParser.isAutoAcMode()) {
            return;
        }

        String cmd = TempUtils.tempToCmd(temp);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.SET_FRONT_SEAT_TEMP_ADD);
        messageEvent.data = cmd;
        EventBus.getDefault().post(messageEvent);
    }

    private void sendLeftTempReduce(String temp) {
        if (can1E5DataParser == null || !can1E5DataParser.isAcOpen()) {
            return;
        }
        if (can1E5DataParser.isDriveSeatWindAuto() || can1E5DataParser.isAutoAcMode()) {
            return;
        }
        String cmd = TempUtils.tempToCmd(temp);

        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.SET_DRIVER_TEMP_SUBTRACT);
        messageEvent.data = cmd;
        EventBus.getDefault().post(messageEvent);
    }

    private void sendLeftTempIncrease(String temp) {
        if (can1E5DataParser == null || !can1E5DataParser.isAcOpen()) {
            return;
        }
        if (can1E5DataParser.isDriveSeatWindAuto() || can1E5DataParser.isAutoAcMode()) {
            return;
        }

        String cmd = TempUtils.tempToCmd(temp);

        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.SET_DRIVER_TEMP_ADD);
        messageEvent.data = cmd;
        EventBus.getDefault().post(messageEvent);
    }

    private void sendRightWindReduce(int wind) {
        if (can1E5DataParser == null || !can1E5DataParser.isAcOpen()) {
            return;
        }
        if (can1E5DataParser.isFrontSeatWindAuto() || can1E5DataParser.isAutoAcMode()) {
            return;
        }

        String cmd = WindUtils.numberToCmd(wind);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.SET_FRONT_SEAT_WIND_SUBTRACT);
        messageEvent.data = cmd;
        EventBus.getDefault().post(messageEvent);
    }

    private void sendRightWindIncrease(int wind) {
        if (can1E5DataParser == null || !can1E5DataParser.isAcOpen()) {
            return;
        }
        if (can1E5DataParser.isFrontSeatWindAuto() || can1E5DataParser.isAutoAcMode()) {
            return;
        }

        String cmd = WindUtils.numberToCmd(wind);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.SET_FRONT_SEAT_WIND_ADD);
        messageEvent.data = cmd;
        EventBus.getDefault().post(messageEvent);
    }

    private void sendLeftWindReduce(int wind) {
        if (can1E5DataParser == null || !can1E5DataParser.isAcOpen()) {
            return;
        }
        if (can1E5DataParser.isDriveSeatWindAuto()|| can1E5DataParser.isAutoAcMode()) {
            return;
        }
        String cmd = WindUtils.numberToCmd(wind);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.SET_DRIVER_WIND_SUBTRACT);
        messageEvent.data = cmd;
        EventBus.getDefault().post(messageEvent);
    }

    private void sendLeftWindIncrease(int wind) {
        if (can1E5DataParser == null || !can1E5DataParser.isAcOpen()) {
            return;
        }
        if (can1E5DataParser.isDriveSeatWindAuto() || can1E5DataParser.isAutoAcMode()) {
            return;
        }
        String cmd = WindUtils.numberToCmd(wind);

        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.SET_DRIVER_WIND_ADD);
        messageEvent.data = cmd;
        EventBus.getDefault().post(messageEvent);
    }


    private void loadData() {
        deviceId = AppUtils.getDeviceId(getContext());

        loadCan1E5Data();
        loadCan20bData();
        loadCan007Data();

    }

    private void loadCan20bData() {
        executorService.execute(() -> {
            try {
                Can20BTable can20BTable = Can20BRepository.getInstance().getData(getContext(), deviceId);
                LogUtils.printI(TAG, "loadData---can20BTable=" + can20BTable);
                can20bDataParser = new Can20bDataParser(can20BTable);
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        if (event.type == MessageEvent.Type.UPDATE_FRONT_AC) {
            loadCan20bData();
        } else if (event.type == MessageEvent.Type.AC_TEMP_SET) {
            int temp = (int) event.data;
            if (temp < 16) {
                temp = 16;
            } else if (temp > 28) {
                temp = 28;
            }
            if (leftAcAdjustView != null) {
                leftAcAdjustView.setTemp(temp + "");
            }
            if (rightAcAdjustView != null) {
                rightAcAdjustView.setTemp(temp + "");
            }
        } else if (event.type == MessageEvent.Type.AC_WIND_SET) {
            int wind = (int) event.data;
            if (wind < 1) {
                wind = 1;
            } else if (wind > 7) {
                wind = 7;
            }
            if (leftAcAdjustView != null) {
                leftAcAdjustView.setWind(wind);
            }
            if (rightAcAdjustView != null) {
                rightAcAdjustView.setWind(wind);
            }
        } else if (event.type == MessageEvent.Type.UPDATE_REAR_DEMIST) {
            loadCan007Data();
        } else if (event.type == MessageEvent.Type.UPDATE_CAN1E5_TO_VIEW) {
            loadCan1E5Data();
        }
    }

    private void loadCan007Data() {
        executorService.execute(() -> {
            Can007Table can007Table = Can007Repository.getInstance().getData(getContext(), deviceId);
            can007DataParser = new Can007DataParser(can007Table);
            Message message = new Message();
            message.what = 3;
            handler.sendMessage(message);
        });
    }

    private void loadCan1E5Data() {
        executorService.execute(() -> {
            Can1E5Table can1E5Table = Can1E5Repository.getInstance().getData(getContext(), deviceId);
            can1E5DataParser = new Can1e5DataParser(can1E5Table);

            LogUtils.printI(TAG, "can1E5DataParser=" + can1E5DataParser);
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        });
    }

}
