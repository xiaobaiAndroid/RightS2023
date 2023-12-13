package com.android.launcher.usbdriver;

import com.android.launcher.util.FuncUtil;
import module.common.utils.LogUtils;
import com.android.launcher.serialport.ttl.SerialHelperttlLd;


public class SendHelperLeft {

    public synchronized static void handler(String hex){
        try {
            hex = hex.toUpperCase() ;
            Thread.sleep(10);
            LogUtils.printI(SendHelperLeft.class.getSimpleName() ,"发送的数据=============+"+hex);
//            ClientSocket.getInstance().sendMessage(hex);
            if (FuncUtil.serialHelperttl!=null){
                SerialHelperttlLd.sendHex(hex);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
