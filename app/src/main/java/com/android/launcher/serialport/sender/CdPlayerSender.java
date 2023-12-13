package com.android.launcher.serialport.sender;


import android.util.Log;

import com.android.launcher.util.FuncUtil;
import com.android.launcher.util.SendcanCD;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import module.common.utils.LogUtils;

/**
 * CD机发送器
 *
 * @date： 2023/11/16
 * @author: 78495
 */
public class CdPlayerSender {

    private ScheduledExecutorService  scheduledService;

    private static final String CAN0FD_CMD = "AA000004000000FD0000000000000000";
    private static final String CAN1C5_CMD = "AA000003000001C50000030000000000";
    private static final String CAN0FA_CMD = "AA000002000000FA7F00000000000000";

    private static final String CAN42A_CMD = "AA0000080000042AFD0A840F00FFFF0E";

    private static final String CAN0FB_START = "AA000008000000FB";

    public static volatile String CAN1BB = "";
    public static volatile String lastCAN1BB = "";
    private final ExecutorService executorService;

    private static final int CMD_LENGTH = 32;

    public CdPlayerSender() {
        scheduledService = Executors.newScheduledThreadPool(2);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void start() {
        scheduledService.scheduleAtFixedRate(() -> {
            SendcanCD.handler(CAN0FD_CMD);
            SendcanCD.handler(CAN1C5_CMD);

            try {
                if (CAN1BB.length() > 0) {

                    //不止有1bb8,还有1bb3,1bb4,1bb6...的情况
                    if(CAN1BB.contains("1bb8")){
//                        String data = CAN1BB.replace("1bb8", "");
                        String data = CAN1BB.substring(4);
                        String cmd = CAN0FB_START + data;
                        if(cmd.length() == CMD_LENGTH){
                            SendcanCD.handler(cmd.toUpperCase());
                        }

                        if (!lastCAN1BB.equals(CAN1BB)) {
                            lastCAN1BB = CAN1BB;
                            LogUtils.printI(CdPlayerSender.class.getSimpleName(),"CAN1BB="+CAN1BB +", length="+CAN1BB.length() + ", cmd="+cmd +", length="+cmd.length());
                        }
                    }else{
                        LogUtils.printI(CdPlayerSender.class.getSimpleName(),"CAN1BB="+CAN1BB +", length="+CAN1BB.length());
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);


        scheduledService.scheduleAtFixedRate(() -> {
            SendcanCD.handler(CAN0FA_CMD);
            SendcanCD.handler(CAN42A_CMD);
        }, 0, 300, TimeUnit.MILLISECONDS);

        executorService.execute(() -> {
            try {
                Thread.sleep(2000);
                point();
                FuncUtil.open1BB = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void destroy() {
        try {
            if (executorService != null) {
                executorService.shutdown();
            }
            if(executorService!=null){
                executorService.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void point() {

        Log.i("cdsend1bbchushi", "=--------------=========================================----------------1111");
        try {
            // 三个返回
            SendcanCD.handler("AA000004000000FD0200000000000000");
            Thread.sleep(100);
            SendcanCD.handler("AA000004000000FD0200000000000000");
            Thread.sleep(100);
            SendcanCD.handler("AA000004000000FD0200000000000000");
            Thread.sleep(100);
            // 五个上
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(100);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(100);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(100);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(100);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(100);

            // 五个右
            SendcanCD.handler("AA000004000000FD0000040000000000");
            Thread.sleep(100);
            SendcanCD.handler("AA000004000000FD0000040000000000");
            Thread.sleep(100);
            SendcanCD.handler("AA000004000000FD0000040000000000");
            Thread.sleep(100);
            SendcanCD.handler("AA000004000000FD0000040000000000");
            Thread.sleep(100);
            SendcanCD.handler("AA000004000000FD0000040000000000");
            Thread.sleep(100);
            FuncUtil.pointOk = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}