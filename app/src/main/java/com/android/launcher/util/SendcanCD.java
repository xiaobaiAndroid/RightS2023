package com.android.launcher.util;


import com.android.launcher.serialport.ttl.SerialHelperttlLd3;

import module.common.utils.LogUtils;

/**
 * @date： 2023/10/19
 * @author: 78495
*/
public class SendcanCD {

    public synchronized static void handler(String hex){
        try {
            if (FuncUtil.sendCDFLG){

                if(hex.length() <32){
                    LogUtils.printI(SendcanCD.class.getSimpleName(),"hex="+hex);
                }
                SerialHelperttlLd3.sendHex(hex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
