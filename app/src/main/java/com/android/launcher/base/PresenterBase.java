package com.android.launcher.base;

import android.content.Context;

import module.common.utils.LogUtils;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public abstract class PresenterBase<T extends IView> implements IPresenter{

    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected T mIView;

    protected Context mContext;

    public PresenterBase(Context context, T iView) {
        this.mIView = iView;
        this.mContext = context;
    }

    @Override
    public void release() {
        try {
            LogUtils.printI(this.getClass().getSimpleName(), "release----");
            mIView = null;
            mContext = null;
            compositeDisposable.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
