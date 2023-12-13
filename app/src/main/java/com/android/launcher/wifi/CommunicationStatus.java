package com.android.launcher.wifi;

/**
* @description: 通信状态
* @createDate: 2023/5/4
*/
public enum CommunicationStatus {
    CONNECT_INIT, //初始化
    CONNECTING, //连接中
    FAIL, //连接失败
    SUCCESS, //连接成功，开始通信
    INTERRUPT //连接中断
}
