package com.android.launcher.wifi;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.android.launcher.MyApp;

import module.common.MessageEvent;
import module.common.utils.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author xxh
 * @date 2018/12/11
 */
@Deprecated
public class ClientRunnable implements Runnable {

    private static final String TAG = ClientRunnable.class.getSimpleName();
    private boolean isStart = false;
    private Socket socket = null;
    private String ip;
    //定义ServerSocket的端口号

    private  BufferedReader bufferedReader;

    //超时1分钟
    private static final int CHECK_WIFI_MAX_COUNT = 120;
    public int checkWifiCount = 0;
    public boolean isStop = false;

    @Override
    public void run() {
        try {
            isStop = false;
            while (true){
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.WIFI_CONNECTING));
                ip = getWifiRouteIPAddress(MyApp.getGlobalContext()) ;
                if(TextUtils.isEmpty(ip) || ip.startsWith("0")){ //未连上热点
                    checkWifiCount++ ;
                }else{
                    break;
                }
                if(checkWifiCount >= CHECK_WIFI_MAX_COUNT){ //一直连不上，提示用户重启
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.WIFI_CONNECT_FAIL));
                    break;
                }
                if(isStop){
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(isStop){
                return;
            }
            checkWifiCount = 0;

            LogUtils.printI(ClientRunnable.class.getName(),"run----");

            socket = new Socket(ip, 35182);

            isStart = true;
            StartSucces();
            try {
                //获取该socket对应的输入流
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.WIFI_CONNECT_SUCCESS));

                String content = "";
                while (bufferedReader!= null && (content = bufferedReader.readLine()) != null) {
//                    Log.i(TAG, "run: 收到来自服务器的消息：" + content);
                    LogUtils.printI(ClientRunnable.class.getName(),"run: 收到来自服务器的消息：" + content);
                    handlerData(content.toUpperCase());
                }
                LogUtils.printI(ClientRunnable.class.getName(),"run finish");

            } catch (SocketException e) {
                LogUtils.printI(ClientRunnable.class.getName(),e.getMessage());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.WIFI_CONNECT_INTERRUPT));
                Log.i(TAG, "与服务端失去连接：");
                e.printStackTrace();
                if (!MyApp.INITFLG){
                    try {
                        ClientSocket.getInstance().stop();
                        Thread.sleep(10000);
                        ClientSocket.getInstance().start();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

            }  catch (Exception e) {
                e.printStackTrace();
                LogUtils.printI(ClientRunnable.class.getName(),e.getMessage());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.WIFI_CONNECT_INTERRUPT));
            }
        } catch (IOException e) {
            try {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.WIFI_CONNECT_INTERRUPT));
                if (!MyApp.INITFLG){
                    try {
                        ClientSocket.getInstance().stop();
                        Thread.sleep(10000);
                        ClientSocket.getInstance().start();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch ( Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

    }

    private static String getWifiRouteIPAddress(Context context) {
        WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();

        String routeIp = Formatter.formatIpAddress(dhcpInfo.gateway);
        Log.i("route ip", "wifi route ip：" + routeIp);

        WifiInfo wifiInfo = wifi_service.getConnectionInfo() ;

        String localIp = inToIp(wifiInfo.getIpAddress()) ;

       MyApp.rountIpLocalIp = routeIp+"-"+localIp ;

       Log.i("ipdizhi", MyApp.rountIpLocalIp+"---------------------------") ;
        return routeIp;

    }

    private static String inToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }

//    private static String intToIp(int paramInt) {
//        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
//                + (0xFF & paramInt >> 24);
//    }
    //用于发送消息
    public void sendMessage(String message) {
        LogUtils.printI(this.getClass().getName(),"isStart="+isStart + "， socket="+socket);
        if (isStart) {
            ThreadUtils.executeBySingle(new ClientPushTask(socket, message));
        } else {
            Log.e(TAG, "sendMessage: socket服务器未连接");
        }
    }

    //启动成功的操作在里面写
    private void StartSucces() {
        Log.i(TAG, "StartSucces: SocetServer启动成功");
    }

    public boolean isStart() {
        return isStart;
    }

    public void stop() {
        try {
            if(bufferedReader != null){
                bufferedReader.close();
                bufferedReader = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.printI(TAG, "stop----e="+e.getMessage());
        }
        try {
            if (socket != null && isStart) {
                socket.close();
                socket = null;
                isStart = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.printI(TAG, "stop----e="+e.getMessage());
        }
        isStop = true;
        LogUtils.printI(TAG, "stop----关闭连接");
    }


    private static void handlerData(String handlerData) {

        Log.i(ClientRunnable.class.getSimpleName(),"HandlerData-------"+handlerData);
//        if (handlerData.startsWith("010202")){
//            HandlerKeyData.handler(handlerData);
//            UsbManagerBenz.UsbResult1 = "key" ;
//        } else if (handlerData.startsWith("AABB")){
//            UsbManagerBenz.UsbResult1 = "Left";
//            HandlerLeftData.lefthandlerdata(handlerData);
//            FuncUtil.syscTime();
//        }else if (handlerData.startsWith("000302")){
//            UsbManagerBenz.UsbResult1 = "wind" ;
//            HandlerWindData.handler(handlerData) ;
//
//        } else if(handlerData.startsWith("CCCBBBAAA")){ //接收空调初始话状态参数, 自定义项目
//            try {
//                String tag = "CCCBBBAAA";
//                String data = handlerData.substring(tag.length());
//                if(!TextUtils.isEmpty(data) && data.length() >16){
//                    String status = data.substring(14,16);
//                    LogUtils.printI(TAG, "data="+data);
//                    String air_binary = StringUtils.hexString2binaryString(status);
//                    if(!TextUtils.isEmpty(air_binary)){
//                        String deviceAccOffStatus = air_binary.substring(0,1);
//                        LogUtils.printI(TAG, "deviceAccOffStatus="+deviceAccOffStatus);
//
//                        if("0".equals(deviceAccOffStatus)){ //空调压缩机开启状态
//                            ACache.get(App.getGlobalContext()).put("acoff","N");
//                        }else{ //空调压缩机关闭状态
//                            ACache.get(App.getGlobalContext()).put("acoff","Y");
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}
