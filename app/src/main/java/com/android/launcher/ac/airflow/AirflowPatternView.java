package com.android.launcher.ac.airflow;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.launcher.R;
import com.android.launcher.ac.wind.WindCmdValue;
import com.android.launcher.ac.winddirection.front.FrontAcCmdSender;
import com.android.launcher.ac.winddirection.front.FrontWindDirectionAdapter;
import com.android.launcher.ac.winddirection.front.FrontWindDirectionCmdValue;
import com.android.launcher.ac.winddirection.front.FrontWindDirectionDataUtils;
import com.android.launcher.ac.winddirection.front.WindDirectionItem;
import com.android.launcher.can.parser.Can007DataParser;
import com.android.launcher.can.parser.Can1e5DataParser;
import com.android.launcher.can.parser.Can20bDataParser;
import com.android.launcher.type.CarType;
import com.bzf.module_db.AcKeyStatus;
import com.bzf.module_db.entity.Can007Table;
import com.bzf.module_db.entity.Can1E5Table;
import com.bzf.module_db.entity.Can20BTable;
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
 * 气流模式
 * @date： 2023/11/7
 * @author: 78495
 */
public class AirflowPatternView extends FrameLayout {

    private static final String TAG = AirflowPatternView.class.getSimpleName();

    private AirflowPatternAdapter mAdapter = new AirflowPatternAdapter(new ArrayList<>());
    private RecyclerView contentRV;


    private String deviceId;

    // 创建一个根据需要创建新线程的线程池，可扩容
    private ExecutorService mExecutor = Executors.newCachedThreadPool();

    private FrontAcCmdSender mCmdSender;
    private Can1e5DataParser can1E5DataParser;


    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
                    setCan1e5ToView();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void setCan1e5ToView() {
        if (can1E5DataParser == null) {
            return;
        }
        resetAdapterData(mAdapter);
        if (can1E5DataParser.isAcOpen()) {
            String pattern = can1E5DataParser.getAirflowPattern();
            if(pattern!=null){
                setWindDirectionItemSelectedByCmd(mAdapter,pattern);
            }
        }


    }

    private void loadAdapterData() {
        try {
            List<AirflowPatternItem> items = new ArrayList<>();
            items.add(new AirflowPatternItem(AirflowPatternCmdValue.CONCENTRATE,getResources().getString(R.string.concentrate)));
            items.add(new AirflowPatternItem(AirflowPatternCmdValue.MEDIUM,getResources().getString(R.string.medium)));
            items.add(new AirflowPatternItem(AirflowPatternCmdValue.DIFFUSE,getResources().getString(R.string.diffuse)));
            mAdapter = new AirflowPatternAdapter(items);
            contentRV.setAdapter(mAdapter);

            setListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public AirflowPatternView(Context context) {
        this(context, null);
    }

    public AirflowPatternView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AirflowPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AirflowPatternView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupInit(context);
    }

    private void setupInit(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_airflow_pattern, this, true);

        contentRV = view.findViewById(R.id.contentRV);
        contentRV.setLayoutManager(new LinearLayoutManager(getContext()));

        contentRV.setItemAnimator(null);


        deviceId = AppUtils.getDeviceId(getContext());
        mCmdSender = new FrontAcCmdSender(getContext());

        loadAdapterData();

        loadData();
    }

    private void setListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (can1E5DataParser == null || !can1E5DataParser.isAcOpen()) {
                return;
            }

            view.setEnabled(false);
            AirflowPatternItem item = mAdapter.getItem(position + mAdapter.getHeaderLayoutCount());
            mExecutor.execute(() -> {
                AirflowPatternCmdValue cmdValue = item.getCmdValue();
                if (mCmdSender != null) {
                    mCmdSender.setAirflowPattern(cmdValue);
                } else {
                    LogUtils.printI(TAG, "AirflowPatternItem mCmdSender isNull");
                }

            });
            new Handler(Looper.getMainLooper()).postDelayed(() -> view.setEnabled(true),1000);
        });

    }


    private void loadData() {
        loadCan1E5Data();

    }



    private int setWindDirectionItemSelectedByCmd(AirflowPatternAdapter adapter, String pattern) {
        if(TextUtils.isEmpty(pattern)){
            pattern = AirflowPatternCmdValue.CONCENTRATE.getValue();
        }
        for (int i = 0; i < adapter.getItemCount(); i++) {
            AirflowPatternItem item = adapter.getItem(i);
            if (item != null) {
                if (item.getCmdValue().getValue().equalsIgnoreCase(pattern)) {
                    item.setSelected(true);
                    adapter.notifyItemChanged(i);
                    return i;
                }
            }
        }
        return 0;
    }


    private void resetAdapterData(AirflowPatternAdapter adapter) {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            AirflowPatternItem item = adapter.getItem(i);
            if (item != null) {
                item.setSelected(false);
                adapter.notifyItemChanged(i);

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
         if (event.type == MessageEvent.Type.UPDATE_CAN1E5_TO_VIEW) {
            loadCan1E5Data();
        }
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
