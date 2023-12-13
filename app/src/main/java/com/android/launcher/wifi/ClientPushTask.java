package com.android.launcher.wifi;

import module.common.utils.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;

import java.io.PrintStream;
import java.net.Socket;

/**
 * @author xxh
 * @date 2018/12/11
 */
public class ClientPushTask extends ThreadUtils.SimpleTask {
    Socket socket;
    String message;
    ClientPushTask(Socket socket,String message) {
        this.socket = socket;
        this.message = message;
    }
    @Override
    public Object doInBackground() throws Throwable {

        PrintStream printStream = new PrintStream(socket.getOutputStream());
        //向该输出流中写入要广播的内容
        printStream.println(message);

        LogUtils.printI(this.getClass().getName(), "socket="+socket +", printStream="+printStream);
        return null;
    }

    @Override
    public void onSuccess(Object result) {

    }
}
