package com.android.launcher.util;

import module.common.utils.LogUtils;

import static com.android.launcher.serialport.sender.CdPlayerSender.CAN1BB;

/**
 * @description: 原车仪表盘操作
 * @createDate: 2023/9/18
 */
public class OriginalMeterOperationHelper {

    public static  void leftDirection(){
        new Thread(() -> SendcanCD.handler("AA000004000000FD0000400000000000")).start();
    }

    public static  void rightDirection(){
        new Thread(() -> {
            SendcanCD.handler("AA000004000000FD0000040000000000");
        }).start();

    }

    public static synchronized void upDirection(){
        new Thread(() -> {
            try {
                SendcanCD.handler("AA000004000000FD0000010000000000");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();



    }

    public static  void downDirection(){
        new Thread(() -> {
            try {
                SendcanCD.handler("AA000004000000FD0000100000000000");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void enter(){
        new Thread(() -> {
            SendcanCD.handler("AA000004000000FD0080000000000000");
        }).start();
    }

    public static void back(){
        new Thread(() -> {
            SendcanCD.handler("AA000004000000FD0200000000000000");
        }).start();
    }

    //旋钮向左旋转
    public static void leftRotate() {
        new Thread(() -> {
            try {
                if (CAN1BB.length() > 0) {

                    //不止有1bb8,还有1bb3,1bb4,1bb6...的情况
                    if (CAN1BB.contains("1bb8")) {
                        //                        String data = CAN1BB.replace("1bb8", "");
                        String data = CAN1BB.substring(4);

                        if(data.length() == 16){
                            //01 00 03 00 00 00 00 A3
                            String left = data.substring(0, 12);
                            String right = data.substring(14);
                            String cmd = "AA000008000000FB" + left + "87" + right;

                            LogUtils.printI(OriginalMeterOperationHelper.class.getSimpleName(),"CAN1BB="+CAN1BB +",data="+data +", cmd="+cmd +", length="+cmd.length());
                            SendcanCD.handler(cmd);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    //旋钮向右旋转
    public static void rightRotate() {
        new Thread(() -> {
            try {
                if (CAN1BB.length() > 0) {

                    //不止有1bb8,还有1bb3,1bb4,1bb6...的情况
                    if (CAN1BB.contains("1bb8")) {
                        //                        String data = CAN1BB.replace("1bb8", "");
                        String data = CAN1BB.substring(4);

                        if(data.length() == 16){
                            //01 00 03 00 00 00 00 A3
                            String left = data.substring(0, 12);
                            String right = data.substring(14);
                            String cmd = "AA000008000000FB" + left + "89" + right;

                            LogUtils.printI(OriginalMeterOperationHelper.class.getSimpleName(),"rightRotate---CAN1BB="+CAN1BB +",data="+data +", cmd="+cmd +", length="+cmd.length());
                            SendcanCD.handler(cmd);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
