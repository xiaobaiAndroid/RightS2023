package com.android.launcher.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;

import com.android.launcher.MainActivity;

import module.common.MessageEvent;
import module.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @createDate: 2023/6/2
 */
public abstract class FragmentBase extends Fragment {

    // 创建一个根据需要创建新线程的线程池，可扩容
    protected ExecutorService mExecutor;

    protected boolean isShow = false;

    protected   String TAG;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(getContentLayoutId(), container, false);
            LogUtils.printI(this.getClass().getSimpleName(), "onCreateView------");
            TAG = this.getClass().getSimpleName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mExecutor = Executors.newCachedThreadPool();
        registerEventBus();
        initView(view,savedInstanceState);

    }

    protected  void setupData(){

    };

    protected void registerEventBus() {
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        disposeMessageEvent(event);
    }

    public abstract void disposeMessageEvent(MessageEvent event);

    protected abstract void initView(View view, Bundle savedInstanceState);

    protected abstract int getContentLayoutId();


    @Override
    public void onResume() {
        super.onResume();
        LogUtils.printI(this.getClass().getSimpleName(), "onResume------");
        isShow = true;
        setupData();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.printI(this.getClass().getSimpleName(), "onPause------");
        isShow = false;
    }

    @Override
    public void onStart() {
        super.onStart();
//        MainActivity.currentFragment = this.getClass().getSimpleName();
        LogUtils.printI(this.getClass().getSimpleName(), "onStart------");
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unregisterEventBus();
        try {
            mExecutor.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.printI(this.getClass().getSimpleName(), "onDestroyView------");
    }

    protected void unregisterEventBus() {
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.printI(this.getClass().getSimpleName(), "onDestroy------");
    }

}
