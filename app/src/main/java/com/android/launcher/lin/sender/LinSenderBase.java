package com.android.launcher.lin.sender;

/**
* @description:
* @createDate: 2023/7/24
*/
public abstract class LinSenderBase {


    protected static final String DATA_HEAD = "AA000000000";

    protected boolean isRunnable = false;

    protected  String TAG = this.getClass().getSimpleName();


    public abstract void send();

    public void release(){
        isRunnable = false;
    };
}
