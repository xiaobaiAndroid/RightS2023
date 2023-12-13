package com.android.launcher.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;

import com.android.launcher.MyApp;
import com.android.launcher.R;
import com.android.launcher.ac.controller.ControlCenterFrameLayout;
import com.android.launcher.ac.winddirection.WindDirectionHomeView;
import com.android.launcher.type.LanguageType;
import com.android.launcher.util.FuncUtil;
import com.android.launcher.util.LanguageUtils;
import com.android.launcher.view.StatusBarView;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import module.common.MessageEvent;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;

/**
 * @date： 2023/11/14
 * @author: 78495
*/
public abstract class Activity2Base extends AppCompatActivity {


    protected boolean isShow = false;

    // 创建一个根据需要创建新线程的线程池，可扩容
    protected ExecutorService mExecutor;
    protected StatusBarView statusBarView;
    protected ConstraintLayout rootCL;
    protected FragmentContainerView fragmentContainerView;
    protected WindDirectionHomeView windDirectionHomeView;

    private ImageView homeIB;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            int language = SPUtils.getInt(this, SPUtils.SP_SELECT_LANGUAGE, LanguageType.SYSTEM.ordinal());
            if (language == LanguageType.ZH.ordinal()) {
                LanguageUtils.setLang(this, LanguageType.ZH);
            } else if (language == LanguageType.EN.ordinal()) {
                LanguageUtils.setLang(this, LanguageType.EN);
            } else {
                if (LanguageUtils.isCN()) {
                    LanguageUtils.setLang(this, LanguageType.ZH);
                } else {
                    LanguageUtils.setLang(this, LanguageType.EN);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().register(this);

        ImmersionBar.with(this).fullScreen(true).hideBar(BarHide.FLAG_HIDE_BAR).init();

        setContentView(R.layout.activity_base2);


        mExecutor = Executors.newCachedThreadPool();

        rootCL = findViewById(R.id.rootCL);
        statusBarView = findViewById(R.id.statusBarView);
        fragmentContainerView = findViewById(R.id.fragmentContainerView);
        windDirectionHomeView = findViewById(R.id.windDirectionHomeView);
        homeIB = findViewById(R.id.homeIB);

        LogUtils.printI(this.getClass().getSimpleName(), "onCreate-----");

        homeIB.setOnClickListener(v -> {
            homeIB.setEnabled(false);
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.KEY_HOME));
            new Handler(Looper.getMainLooper()).postDelayed(() -> homeIB.setEnabled(true), 1000);
        });

        initView(savedInstanceState);
        setupData();

    }


    protected abstract void setupData();

    protected abstract void initView(Bundle savedInstanceState);



    @Override
    protected void onStart() {
        super.onStart();
        MyApp.currentActivityStr = getClass().getName();
        FuncUtil.currentActivity = this.getClass().getName();
        LogUtils.printI(this.getClass().getSimpleName(), "onStart-----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (mExecutor != null) {
                mExecutor.shutdownNow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.printI(this.getClass().getSimpleName(), "onResume-----");
        isShow = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.printI(this.getClass().getSimpleName(), "onPause-----");
        isShow = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        disposeMessageEvent(event);
    }


    public void disposeMessageEvent(MessageEvent event) {
        if (event.type == MessageEvent.Type.CLOCK) {
            try {
                String time = (String) event.data;
                statusBarView.setTime(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
