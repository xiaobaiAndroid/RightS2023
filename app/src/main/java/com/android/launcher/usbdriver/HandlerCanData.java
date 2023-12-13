package com.android.launcher.usbdriver;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

import com.android.launcher.util.FuncUtil;

import module.common.MessageEvent;
import module.common.utils.LogUtils;
import com.android.launcher.util.SendcanCD;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class HandlerCanData {

    public static final String TAG = HandlerCanData.class.getSimpleName();
    public static StringBuffer sb = new StringBuffer();
    private static String pre = "";
    private static String CHAR = "AA0000";
    public static CountDownTimer countDownTimer ;
    public static String pre1BBdata = "" ;
    public static String cdstatus ;

    //CD机发送命令是否完成
    public static volatile boolean cdCommandSendFinish = true;

    public static Handler handler = new Handler(Looper.getMainLooper()) ;

    private static MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CD_CONNECT);

    //cd机是否开机
    public static volatile boolean cdStatusIsSend = false;



    public static void handlerdata(String ch) {
        sb.append(ch);
        String hexdata = sb.toString();
        if (hexdata.length() > 32) {
            handlerCan(hexdata);
            sb.setLength(0);
        }
    }

    private static List<String> toHexString(String hex) {
        ArrayList<String> arr = new ArrayList<String>();

        String h = hex.substring(12, 16);

        int hh = Integer.parseInt(h, 16);
        String hhx = Integer.toHexString(hh);

        String len = hex.substring(7, 8);
        arr.add(hhx);
        arr.add(len);
        String temp = "";
        for (int i = 16; i < hex.length(); i++) {

            if (i % 2 == 0) {//每隔两个
                temp = hex.charAt(i) + "";
            } else {
                temp = temp + hex.charAt(i);
            }

            if (temp.length() == 2) {
                arr.add(temp);
            }
        }
        return arr;
    }


    private static void handlerCan(String ss) {

        ss = pre + ss;
        if (ss.endsWith(CHAR)) {
            String[] datastring = ss.split(CHAR);
            for (String str : datastring) {
                String canstr = CHAR + str;
                if (canstr.length() == 32 && canstr.startsWith(CHAR)) {
                    List<String> data = toHexString(canstr);
//                        System.out.println("candataTTL================1================"+data.toString());
                    String canId = data.get(0);
                    if (canId.toUpperCase().equals("1BB")) {
                        handler1BB(data);
                    }

                }
            }
            pre = CHAR;
        } else {
            String[] datastring = ss.split(CHAR);
            String dataString2 = CHAR + datastring[datastring.length - 1];
            if (dataString2.length() == 32) {
                for (int i = 0; i < datastring.length; i++) {
                    String canstr = CHAR + datastring[i];
                    if (canstr.length() == 32 && canstr.startsWith(CHAR)) {
                        List<String> data = toHexString(canstr);
                        String canId = data.get(0);
//                        System.out.println("candataTTL================2================"+data.toString());
                        if (canId.toUpperCase().equals("1BB")) {
                            handler1BB(data);
                        }
                    }
                }
                pre = "";
            } else {
                for (int i = 0; i < datastring.length - 1; i++) {
                    String canstr = CHAR + datastring[i];
                    if (canstr.length() == 32 && canstr.startsWith(CHAR)) {
                        List<String> data = toHexString(canstr);
//                        System.out.println("candataTTL================3================"+data.toString());
                        String canId = data.get(0);
                        if (canId.toUpperCase().equals("1BB")) {
                            handler1BB(data);
                        }
                    }
                }
                pre = dataString2;
            }
        }
    }



    // 1BB的处理 
    private static void handler1BB(List<String> data) {

//        LogUtils.printI("CDSEND1BBlld",data.toString()+"------------------") ;
        FuncUtil.CAN1BB = String.join("" ,data) ;
//        LogUtils.printI("can1bbreturn",data.toString()+"------------1------") ;
        LogUtils.printI(TAG,data.toString()+"------------1------");
        if (FuncUtil.open1BB){

            String datacontent = data.get(0)+data.get(1)+data.get(2);
            LogUtils.printI(TAG,data.toString()+"------------------") ;
            String cdstatus1 = data.get(2) ;



            if (!pre1BBdata.equals(datacontent) || cdstatus1.equals("02")){
                pre1BBdata = datacontent ;

                cdstatus = data.get(2) ;
                LogUtils.printI(TAG, "handler1BB-----cdStatus="+cdstatus +",cdCommandSendFinish="+cdCommandSendFinish);

                if(!cdCommandSendFinish){
                    return;
                }

                if(cdstatus.equals("02")){
                    messageEvent.data = false;
                    EventBus.getDefault().post(messageEvent);
                    cdStatusIsSend = false;

                    cdCommandSendFinish = false;
                    // 关机状态
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (countDownTimer==null){
                                countDownTimer = new CountDownTimer(5000,2000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }
                                    @Override
                                    public void onFinish() {
                                        LogUtils.printI("CDSEND1BB","-----结束发送开机命令-------------AA000004000000FD1000000000000000"+cdstatus) ;
                                        // 开机命令
                                        if (cdstatus.equals("02")){
                                            try {
                                                SendcanCD.handler("AA000004000000FD1000000000000000");
                                                Thread.sleep(200);
                                                SendcanCD.handler("AA000004000000FD0000010000000000");
                                                Thread.sleep(200);
                                                SendcanCD.handler("AA000004000000FD0000010000000000");
                                                Thread.sleep(200);
                                                SendcanCD.handler("AA000004000000FD0000010000000000");
                                                Thread.sleep(200);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (countDownTimer!=null){
                                            countDownTimer.cancel();
                                            countDownTimer = null ;
                                        }
                                        cdCommandSendFinish = true;
                                    }
                                }.start();
                            }
                        }
                    }) ;
                }else{
                    if(!cdStatusIsSend){
                        cdStatusIsSend = true;
                        messageEvent.data = true;
                        EventBus.getDefault().post(messageEvent);
                    }
                }

                if (cdstatus.equals("01")){
                    LogUtils.printI("CDSEND1BB","-----开机正常获取有几个菜单") ;
                    if(FuncUtil.pointOk){
                    }else{
                        point();
                    }
                }
            }

        }
    }




    /**
     *
     * 指定开启
     *
     */
    public static void point() {

        try {
            // 三个返回
            Thread.sleep(100);
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

            FuncUtil.pointOk= true;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
