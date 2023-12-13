package com.android.launcher.ac.winddirection.front;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.launcher.R;
import com.android.launcher.ac.wind.WindCmdValue;
import com.android.launcher.can.parser.Can007DataParser;
import com.android.launcher.can.parser.Can1e5DataParser;
import com.android.launcher.can.parser.Can20bDataParser;
import com.android.launcher.type.CarType;
import com.bzf.module_db.AcKeyStatus;
import com.bzf.module_db.entity.Can007Table;
import com.bzf.module_db.entity.Can1E5Table;
import com.bzf.module_db.entity.Can20BTable;
import com.bzf.module_db.entity.CanBBTable;
import com.bzf.module_db.repository.Can007Repository;
import com.bzf.module_db.repository.Can1E5Repository;
import com.bzf.module_db.repository.Can20BRepository;
import com.bzf.module_db.repository.CanBBRepository;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import module.common.MessageEvent;
import module.common.utils.AppUtils;
import module.common.utils.ButtonUtils;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;

/**
 * @date： 2023/11/7
 * @author: 78495
 */
public class FrontWindDirectionView extends FrameLayout {

    private static final String TAG = FrontWindDirectionView.class.getSimpleName();

    private FrontWindDirectionAdapter leftWindDirectionAdapter = new FrontWindDirectionAdapter(new ArrayList<>());
    private FrontWindDirectionAdapter rightWindDirectionAdapter = new FrontWindDirectionAdapter(new ArrayList<>());
    private RecyclerView leftContentRV;
    private RecyclerView rightContentRV;

    private ImageView acOffIV;
    private ImageView rearDemistIV;
    private ImageView frontDemistIV;
    private ImageView innerLoopIV;
    //前后空调切换
    private ImageView frontRearToggleIV;
    private TextView frontRearToggleTV;

    private int leftCurrentPosition = 0;
    private int rightCurrentPosition = 0;

    private volatile CanBBTable canBBTable;


    private String deviceId;

    // 创建一个根据需要创建新线程的线程池，可扩容
    private ExecutorService mExecutor = Executors.newCachedThreadPool();

    private FrontAcCmdSender mCmdSender;
    private Can1e5DataParser can1E5DataParser;
    private Can20bDataParser can20bDataParser;
    private Can007DataParser can007DataParser;


    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
                    setCan1e5ToView();
                } else if (msg.what == 2) {
                    setCan20BDataToView();
                } else if (msg.what == 3) {
                    setCan007DataToView();
                } else if (msg.what == 4) {
                    setCanBbDataToView();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    private TextView driverAutoTV;
    private TextView frontSeatAutoTV;


    private void setCan007DataToView() {
        if (can007DataParser == null) {
            LogUtils.printE(TAG, "setRearDemistDataToView----can007Table is null");
            return;
        }
        AcKeyStatus rearDemistStatus = can007DataParser.getRearDemistStatus();
        if (rearDemistStatus == AcKeyStatus.BREAKDOWN) {
            //后挡风除雾指示灯闪烁
            ButtonUtils.setBreakdownStatus(rearDemistIV);
        } else if (rearDemistStatus == AcKeyStatus.OPEN) {
            //后挡风除雾指示灯长亮
            ButtonUtils.setSelected(rearDemistIV, true);
        } else {
            ButtonUtils.setSelected(rearDemistIV, false);
            //后挡风除雾关闭
        }
    }

    private void setCan1e5ToView() {
        if (can1E5DataParser == null || acOffIV == null) {
            return;
        }
        if (!can1E5DataParser.isAcOpen()) {
            //空调关闭状态
            ButtonUtils.setSelected(acOffIV, true);
            ButtonUtils.setSelected(frontDemistIV, false);
            ButtonUtils.setSelected(rearDemistIV, false);
            ButtonUtils.setSelected(innerLoopIV, false);
            ButtonUtils.setTextViewSelected(driverAutoTV, false);
            ButtonUtils.setTextViewSelected(frontSeatAutoTV, false);

            resetAdapterData(leftWindDirectionAdapter);
            resetAdapterData(rightWindDirectionAdapter);
        } else {
            ButtonUtils.setSelected(acOffIV, false);
        }

        LogUtils.printI(TAG,"setCan1e5ToView---isDriveSeatWindDirAuto="+can1E5DataParser.isDriveSeatWindDirAuto() +", isFrontSeatWindDirAuto="+ can1E5DataParser.isFrontSeatWindDirAuto());
        if (can1E5DataParser.isDriveSeatWindDirAuto() && can1E5DataParser.isFrontSeatWindDirAuto()) {
            //空调自动模式
            resetAdapterData(leftWindDirectionAdapter);
            selectWindDirectionAutoMode(leftWindDirectionAdapter);
            resetAdapterData(rightWindDirectionAdapter);
            selectWindDirectionAutoMode(rightWindDirectionAdapter);

        } else if (can1E5DataParser.isDriveSeatWindDirAuto()) {
            //主驾自动模式
            resetAdapterData(leftWindDirectionAdapter);
            selectWindDirectionAutoMode(leftWindDirectionAdapter);
        } else if (can1E5DataParser.isFrontSeatWindDirAuto()) {
            //副驾自动模式
            resetAdapterData(rightWindDirectionAdapter);
            selectWindDirectionAutoMode(rightWindDirectionAdapter);
        }

        ButtonUtils.setTextViewSelected(driverAutoTV, can1E5DataParser.isDriveSeatWindAuto());
        ButtonUtils.setTextViewSelected(frontSeatAutoTV, can1E5DataParser.isFrontSeatWindAuto());

        loadCanBbData();
    }

    private void loadAdapterData() {
        try {
            List<WindDirectionItem> itemslLeft = FrontWindDirectionDataUtils.getDataByCarType(carType);
            leftWindDirectionAdapter = new FrontWindDirectionAdapter(itemslLeft);
            Objects.requireNonNull(leftWindDirectionAdapter.getItem(leftCurrentPosition)).setSelected(true);
            leftContentRV.setAdapter(leftWindDirectionAdapter);

            List<WindDirectionItem> itemslRight= FrontWindDirectionDataUtils.getDataByCarType(carType);
            rightWindDirectionAdapter = new FrontWindDirectionAdapter(itemslRight);
            Objects.requireNonNull(rightWindDirectionAdapter.getItem(rightCurrentPosition)).setSelected(true);
            rightContentRV.setAdapter(rightWindDirectionAdapter);

            setListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int carType = CarType.S500.ordinal();


    public FrontWindDirectionView(Context context) {
        this(context, null);
    }

    public FrontWindDirectionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrontWindDirectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FrontWindDirectionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupInit(context);
    }

    private void setupInit(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_front_seat_winddir, this, true);

        leftContentRV = view.findViewById(R.id.leftContentRV);
        rightContentRV = view.findViewById(R.id.rightContentRV);
        leftContentRV.setLayoutManager(new LinearLayoutManager(getContext()));
        rightContentRV.setLayoutManager(new LinearLayoutManager(getContext()));

        leftContentRV.setItemAnimator(null);
        rightContentRV.setItemAnimator(null);

        frontDemistIV = view.findViewById(R.id.frontDemistIV);
        rearDemistIV = view.findViewById(R.id.rearDemistIV);
        innerLoopIV = view.findViewById(R.id.innerLoopIV);
        acOffIV = view.findViewById(R.id.acOffIV);
//        rightAirflowIV = view.findViewById(R.id.rightAirflowIV);
//        leftAirflowIV = view.findViewById(R.id.leftAirflowIV);
        frontRearToggleIV = view.findViewById(R.id.frontRearToggleIV);
        FrameLayout frontRearToggleFL = view.findViewById(R.id.frontRearToggleFL);
        frontRearToggleTV = view.findViewById(R.id.frontRearToggleTV);

        driverAutoTV = view.findViewById(R.id.driverAutoTV);
        frontSeatAutoTV = view.findViewById(R.id.frontSeatAutoTV);

        rightContentRV.setAdapter(rightWindDirectionAdapter);

        deviceId = AppUtils.getDeviceId(getContext());
        mCmdSender = new FrontAcCmdSender(getContext());

        loadAdapterData();

        loadData();
    }

    private void setListener() {
//        leftWindDirectionAdapter.setOnItemClickListener((adapter, view1, position) -> {
//            updateLeftWinddireItem(position);
//        });
        leftWindDirectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                view.setEnabled(false);
                updateLeftWinddireItem(position);
                new Handler(Looper.getMainLooper()).postDelayed(() -> view.setEnabled(true),1000);
            }
        });

//        rightWindDirectionAdapter.setOnItemClickListener((adapter, view12, position) -> {
//            updateRightWinddireItem(position);
//        });

        rightWindDirectionAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            view.setEnabled(false);
            updateRightWinddireItem(position);
            new Handler(Looper.getMainLooper()).postDelayed(() -> view.setEnabled(true),1000);
        });

        //空调开关
        acOffIV.setOnClickListener(v -> {
            if (can1E5DataParser != null) {
                acOffIV.setEnabled(false);
                setAcOff(can1E5DataParser.isAcOpen());

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (acOffIV != null) {
                        acOffIV.setEnabled(true);
                    }
                }, 1500);
            }
        });

        //内循环开关
        innerLoopIV.setOnClickListener(v -> {
            if (can1E5DataParser != null) {
                if (can1E5DataParser.isAcOpen()) {
                    innerLoopIV.setEnabled(false);
                    toggleInnerLoopView();
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        if (innerLoopIV != null) {
                            innerLoopIV.setEnabled(true);
                        }
                    }, 1500);
                }
            }
        });

        //前挡风除雾开关
        frontDemistIV.setOnClickListener(v -> {
            if (can1E5DataParser != null) {
                if (can1E5DataParser.isAcOpen()) {
                    frontDemistIV.setEnabled(false);
                    toggleFrontDemistView();
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        if (frontDemistIV != null) {
                            frontDemistIV.setEnabled(true);
                        }
                    }, 1000);
                }
            }
        });

        //后挡风玻璃开关
        rearDemistIV.setOnClickListener(v -> {
            if (can1E5DataParser != null) {
                if (can1E5DataParser.isAcOpen()) {
                    rearDemistIV.setEnabled(false);
                    toggleRearDemistView();
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        if (rearDemistIV != null) {
                            rearDemistIV.setEnabled(true);
                        }
                    }, 1500);
                }
            }
        });

        driverAutoTV.setOnClickListener(v -> {
            if(can1E5DataParser==null || !can1E5DataParser.isAcOpen()){
                return;
            }
            if(mCmdSender == null){
                return;
            }
            driverAutoTV.setEnabled(false);
            mCmdSender.setDriverAuto(!can1E5DataParser.isDriveSeatWindAuto());
            new Handler(Looper.getMainLooper()).postDelayed(() -> driverAutoTV.setEnabled(true),1000);
        });

        frontSeatAutoTV.setOnClickListener(v -> {
            if(can1E5DataParser==null || !can1E5DataParser.isAcOpen()){
                return;
            }
            if(mCmdSender == null){
                return;
            }
            frontSeatAutoTV.setEnabled(false);
            mCmdSender.setFrontSeatAuto(!can1E5DataParser.isFrontSeatWindAuto());
            new Handler(Looper.getMainLooper()).postDelayed(() -> frontSeatAutoTV.setEnabled(true),1000);
        });
    }


    private void updateRightWinddireItem(int position) {
        if (can1E5DataParser == null || !can1E5DataParser.isAcOpen()) {
            return;
        }
        if (rightWindDirectionAdapter == null) {
            return;
        }
        WindDirectionItem windDirectionItem = rightWindDirectionAdapter.getItem(position);
        mExecutor.execute(() -> {
            FrontWindDirectionCmdValue cmdValue = windDirectionItem.getCmdValue();
            if(windDirectionItem.getItemType() == WindDirectionItem.Type.AUTO.ordinal()){
                mCmdSender.setRightWindDirectionAuto(true);
            }else {
                if (mCmdSender != null) {
                    mCmdSender.setRightWindDirection(cmdValue);
                } else {
                    LogUtils.printE(TAG, "updateRightWinddireItem mCmdSender isNull");
                }
            }
        });
    }

    private void updateLeftWinddireItem(int position) {
        if (can1E5DataParser == null || !can1E5DataParser.isAcOpen()) {
            return;
        }
        if (leftWindDirectionAdapter == null) {
            return;
        }
        WindDirectionItem windDirectionItem = leftWindDirectionAdapter.getItem(position + leftWindDirectionAdapter.getHeaderLayoutCount());
        mExecutor.execute(() -> {
            FrontWindDirectionCmdValue cmdValue = windDirectionItem.getCmdValue();
            if(windDirectionItem.getItemType() == WindDirectionItem.Type.AUTO.ordinal()){
                mCmdSender.setLeftWindDirectionAuto(true);
            }else{
                if (mCmdSender != null) {
                    mCmdSender.setLeftWindDirection(cmdValue);
                } else {
                    LogUtils.printI(TAG, "updateLeftWinddireItem mCmdSender isNull");
                }
            }

        });

    }

    //设置空调开关
    private void setAcOff(boolean acStatus) {
        LogUtils.printI(TAG, "setAcOff---acStatus=" + acStatus);
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.FRONT_AC_OFF);
        messageEvent.data = !acStatus;
        EventBus.getDefault().post(messageEvent);

    }


    //内循环开关切换
    private void toggleInnerLoopView() {
        if (can20bDataParser != null) {
            if (can20bDataParser.innerLoopIsOpen()) {
                ButtonUtils.setSelected(innerLoopIV, false);
            } else {
                ButtonUtils.setSelected(innerLoopIV, true);
            }
        }
    }

    //前挡风除雾开关切换
    private void toggleFrontDemistView() {
        if (can20bDataParser != null) {
            mExecutor.execute(() -> {
                MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.SET_FRONT_DEMIST_STATUS);
                messageEvent.data = !can20bDataParser.frontDemistIsOpen();
                EventBus.getDefault().post(messageEvent);
            });
        }
    }

    //后挡风除雾开关切换
    private void toggleRearDemistView() {
        if (can007DataParser == null) {
            return;
        }
        if (can007DataParser.getRearDemistStatus() == AcKeyStatus.CLOSE) {
            ButtonUtils.setSelected(rearDemistIV, true);
        } else if (can007DataParser.getRearDemistStatus() == AcKeyStatus.OPEN) {
            ButtonUtils.setSelected(rearDemistIV, false);
        }
    }


    private void loadData() {
        mExecutor.execute(() -> {
            try {
                carType = SPUtils.getInt(getContext(), SPUtils.SP_CAR_TYPE, CarType.S500.ordinal());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        loadCan1E5Data();
        loadCan20bData();
        loadCan007Data();

    }

    private void loadCan20bData() {
        mExecutor.execute(() -> {
            try {
                carType = SPUtils.getInt(getContext(), SPUtils.SP_CAR_TYPE, CarType.S500.ordinal());
                Can20BTable can20BTable = Can20BRepository.getInstance().getData(getContext(), deviceId);
                can20bDataParser = new Can20bDataParser(can20BTable);

                LogUtils.printI(TAG,"can20bDataParser="+can20bDataParser);
                Message message2 = new Message();
                message2.what = 2;
                mHandler.sendMessage(message2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadCanBbData() {
        mExecutor.execute(() -> {
            canBBTable = CanBBRepository.getInstance().getData(getContext(), deviceId);
            LogUtils.printI(TAG, "canBBTable="+canBBTable);
            Message message = new Message();
            message.what = 4;
            mHandler.sendMessage(message);
        });
    }

    //设置风向数据
    private void setCanBbDataToView() {
        if (canBBTable == null) {
            LogUtils.printI(TAG, "setWindDirectionDataToView----canBBTable is null");
            return;
        }

        String leftWindDirection = canBBTable.getLeftWindDirection();
        String rightWindDirection = canBBTable.getRightWindDirection();

        LogUtils.printI(TAG, "setCanBbDataToView----leftWindDirection="+leftWindDirection+" ,rightWindDirection="+rightWindDirection);
        if (can1E5DataParser != null) {
            if(can1E5DataParser.isAcOpen()){
                if (can1E5DataParser.isDriveSeatWindDirAuto() && can1E5DataParser.isFrontSeatWindDirAuto()) {
                    //自动模式
                    resetAdapterData(leftWindDirectionAdapter);
                    resetAdapterData(rightWindDirectionAdapter);
                    leftCurrentPosition = selectWindDirectionAutoMode(leftWindDirectionAdapter);
                    rightCurrentPosition = selectWindDirectionAutoMode(rightWindDirectionAdapter);
                } else if (can1E5DataParser.isDriveSeatWindDirAuto()) {
                    //主驾自动模式
                    resetAdapterData(leftWindDirectionAdapter);
                    resetAdapterData(rightWindDirectionAdapter);
                    leftCurrentPosition = selectWindDirectionAutoMode(leftWindDirectionAdapter);
                    rightCurrentPosition = setWindDirectionItemSelectedByCmd(rightWindDirectionAdapter, rightWindDirection);

                } else if (can1E5DataParser.isFrontSeatWindDirAuto()) {
                    //副驾自动模式
                    resetAdapterData(rightWindDirectionAdapter);
                    resetAdapterData(leftWindDirectionAdapter);
                    rightCurrentPosition = selectWindDirectionAutoMode(rightWindDirectionAdapter);
                    leftCurrentPosition = setWindDirectionItemSelectedByCmd(leftWindDirectionAdapter, leftWindDirection);
                } else {
                    resetAdapterData(leftWindDirectionAdapter);
                    resetAdapterData(rightWindDirectionAdapter);
                    //手动模式
                    leftCurrentPosition = setWindDirectionItemSelectedByCmd(leftWindDirectionAdapter, leftWindDirection);
                    rightCurrentPosition = setWindDirectionItemSelectedByCmd(rightWindDirectionAdapter, rightWindDirection);
                }

            }else {
                resetAdapterData(leftWindDirectionAdapter);
                resetAdapterData(rightWindDirectionAdapter);
            }
        }
    }

    private int setWindDirectionItemSelectedByCmd(FrontWindDirectionAdapter adapter, String leftWindDirection) {
        if(TextUtils.isEmpty(leftWindDirection)){
            leftWindDirection = WindCmdValue.WIND7.getValue();
        }
        for (int i = 0; i < adapter.getItemCount(); i++) {
            WindDirectionItem windDirectionItem = adapter.getItem(i);
            if (windDirectionItem != null) {
                if (windDirectionItem.getCmdValue().getValue().equalsIgnoreCase(leftWindDirection)) {
                    windDirectionItem.setSelected(true);
                    adapter.notifyItemChanged(i);
                    return i;
                }
            }
        }
        return 0;
    }


    private void setCan20BDataToView() throws NullPointerException {

        if (can20bDataParser.innerLoopIsOpen()) {
            ButtonUtils.setSelected(innerLoopIV, true);
        } else {
            ButtonUtils.setSelected(innerLoopIV, false);
        }
        if (can20bDataParser.frontDemistIsOpen()) {
            ButtonUtils.setSelected(frontDemistIV, true);
        } else {
            ButtonUtils.setSelected(frontDemistIV, false);
        }
    }

    //设置风向item为自动模式
    private int selectWindDirectionAutoMode(FrontWindDirectionAdapter adapter) {
        try {
            if (adapter != null) {
                WindDirectionItem windDirectionItem = Objects.requireNonNull(adapter.getItem(0));
                windDirectionItem.setSelected(true);
                adapter.notifyItemChanged(0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private void resetAdapterData(FrontWindDirectionAdapter frontAirflowAdapter) {
        for (int i = 0; i < frontAirflowAdapter.getItemCount(); i++) {
            WindDirectionItem item = frontAirflowAdapter.getItem(i);
            if (item != null) {
                item.setSelected(false);
                frontAirflowAdapter.notifyItemChanged(i);

            }
        }
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
        if (event.type == MessageEvent.Type.UPDATE_REAR_DEMIST) {
            loadCan007Data();
        } else if (event.type == MessageEvent.Type.UPDATE_CAN1E5_TO_VIEW) {
            loadCan1E5Data();
        }else if(event.type == MessageEvent.Type.UPDATE_FRONT_AC){
            loadCan20bData();
        }else if(event.type == MessageEvent.Type.AIRFLOW_ALLOCATION_DATA){
            loadCanBbData();
        }
    }

    private void loadCan007Data() {
        mExecutor.execute(() -> {
            Can007Table can007Table = Can007Repository.getInstance().getData(getContext(), deviceId);
            LogUtils.printI(TAG, "can007Table=" + can007Table);
            can007DataParser = new Can007DataParser(can007Table);
            Message message = new Message();
            message.what = 3;
            mHandler.sendMessage(message);
        });
    }

    private void loadCan1E5Data() {
        mExecutor.execute(() -> {
            try {
                Can1E5Table can1E5Table = Can1E5Repository.getInstance().getData(getContext(), deviceId);
                can1E5DataParser = new Can1e5DataParser(can1E5Table);
                LogUtils.printI(TAG, "can1E5DataParser=" + can1E5DataParser);
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
