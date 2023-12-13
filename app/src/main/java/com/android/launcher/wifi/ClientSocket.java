package com.android.launcher.wifi;

import android.util.Log;

import module.common.utils.LogUtils;

import java.net.Socket;

/**
 * @author xxh
 * @date 2018.12.11
 */
public class ClientSocket {

    private static final String TAG = "ClientSocket";
    private Boolean isStart = false;
    private Socket socket;
    private ClientRunnable clientRunnable;
    private Thread thread;

    private static class VideoClientSocketInstance {
        private static final ClientSocket INSTANCE = new ClientSocket();

    }

    public static ClientSocket getInstance() {
        return VideoClientSocketInstance.INSTANCE;
    }

    public void start() {
        LogUtils.printI(this.getClass().getName(), "start---");
        if (clientRunnable == null || !clientRunnable.isStart()) {
            LogUtils.printI(this.getClass().getName(), "ClientRunnable---start");
            clientRunnable = new ClientRunnable();
            thread = new Thread(clientRunnable);
            thread.start();
        } else {
            Log.i(TAG, "start: 客户端已经运行！");
        }
    }

    public void stop() {
        if(clientRunnable!=null){
            clientRunnable.stop();
            clientRunnable = null;
            isStart = false;
        }
    }

    private boolean isStart() {
        return isStart;
    }


    public void sendMessage(String message) {

        if (clientRunnable != null) {
            LogUtils.printI(this.getClass().getName(), "clientRunnable=" + clientRunnable + "message===" + message);
            clientRunnable.sendMessage(message);
        }
    }

}
