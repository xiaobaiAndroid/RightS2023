package com.android.launcher.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.launcher.MyApp;
import com.android.launcher.type.LanguageType;
import com.android.launcher.util.FuncUtil;
import com.android.launcher.util.LanguageUtils;
import com.android.launcher.view.StatusBarView;

import module.common.MessageEvent;

import com.android.launcher.R;

import module.common.utils.LogUtils;
import module.common.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @createDate: 2023/5/30
 */
public abstract class ActivityBase<T extends IPresenter> extends AppCompatActivity {

    protected T mPresenter;

    protected boolean isShow = false;

    // 创建一个根据需要创建新线程的线程池，可扩容
    protected ExecutorService mExecutor;
    protected StatusBarView statusBarView;
    protected ConstraintLayout rootCL;

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

        final ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_FULLSCREEN;
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(decorView.getSystemUiVisibility() | uiOptions);

        setContentView(R.layout.activity_base);


        mExecutor = Executors.newCachedThreadPool();

        rootCL = findViewById(R.id.rootCL);
        FrameLayout contentFL = findViewById(R.id.contentFL);
        statusBarView = findViewById(R.id.statusBarView);
        View view = getLayoutInflater().inflate(getContentLayoutId(), null);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        contentFL.addView(view, layoutParams);

        mPresenter = createPresenter();
        initView(savedInstanceState);
        setupData();

    }

    protected abstract T createPresenter();

    protected abstract void setupData();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract int getContentLayoutId();


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
        if (mPresenter != null) {
            mPresenter.release();
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
