package com.android.launcher.util;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

@Deprecated
public class SendUsbToCan {
//    public static Timer fTimer = new Timer();
//    public static Timer yTimer = new Timer();
//    public static Timer sTimer = new Timer();
//    public static Timer mTimer = new Timer();
//
//    public static void start() {
//
//        if (fTimer != null) {
//            fTimer.schedule(new TimerTask() {//
//                @Override
//                public void run() {
//                    try {
//                        String cmd1 = "AA000004000000FD0000000000000000";
//                        String cmd2 = "AA000003000001C50000030000000000";
//                        SendcanCD.handler(cmd1);
//                        SendcanCD.handler(cmd2);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 0, 100);
//        }
//        if (sTimer != null) {
//            sTimer.schedule(new TimerTask() {//
//                @Override
//                public void run() {
//                    try {
//
//                        if (!FuncUtil.CAN1BB.equals("")) {
//                            String data = FuncUtil.CAN1BB.replace("1bb8", "");
////                            Log.i("can1bbreturn","AA000008000000FB"+data+"------------2--") ;
//                            String cmd = "AA000008000000FB" + data;
//                            SendcanCD.handler(cmd);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 0, 100);
//        }
//
//        if (yTimer != null) {
//            fTimer.schedule(new TimerTask() {//
//                @Override
//                public void run() {
//                    try {
//                        String cmd = "AA000002000000FA7F00000000000000";
//                        SendcanCD.handler(cmd);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 0, 300);
//        }
//
//        if (mTimer != null) {
//            mTimer.schedule(new TimerTask() {//
//                @Override
//                public void run() {
//                    try {
//                        String cmd = "AA0000080000042AFD0A840F00FFFF0E";
//                        SendcanCD.handler(cmd);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 0, 300);
//        }
//
//        new Thread(() -> {
//            try {
//                Thread.sleep(2000);
//                point();
//
//                FuncUtil.open1BB = true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    public static void point() {
//
//        Log.i("cdsend1bbchushi", "=--------------=========================================----------------1111");
//        try {
//            // 三个返回
//            SendcanCD.handler("AA000004000000FD0200000000000000");
//            Thread.sleep(100);
//            SendcanCD.handler("AA000004000000FD0200000000000000");
//            Thread.sleep(100);
//            SendcanCD.handler("AA000004000000FD0200000000000000");
//            Thread.sleep(100);
//            // 五个上
//            SendcanCD.handler("AA000004000000FD0000010000000000");
//            Thread.sleep(100);
//            SendcanCD.handler("AA000004000000FD0000010000000000");
//            Thread.sleep(100);
//            SendcanCD.handler("AA000004000000FD0000010000000000");
//            Thread.sleep(100);
//            SendcanCD.handler("AA000004000000FD0000010000000000");
//            Thread.sleep(100);
//            SendcanCD.handler("AA000004000000FD0000010000000000");
//            Thread.sleep(100);
//
//            // 五个右
//            SendcanCD.handler("AA000004000000FD0000040000000000");
//            Thread.sleep(100);
//            SendcanCD.handler("AA000004000000FD0000040000000000");
//            Thread.sleep(100);
//            SendcanCD.handler("AA000004000000FD0000040000000000");
//            Thread.sleep(100);
//            SendcanCD.handler("AA000004000000FD0000040000000000");
//            Thread.sleep(100);
//            SendcanCD.handler("AA000004000000FD0000040000000000");
//            Thread.sleep(100);
//            FuncUtil.pointOk = true;
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
