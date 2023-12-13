package com.android.launcher.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.android.launcher.MyApp;
import com.android.launcher.serialport.ttl.SerialHelperttlLd;
import com.android.launcher.serialport.ttl.SerialHelperttlLd3;
import com.android.launcher.vo.MusicVo;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import module.common.utils.LogUtils;


public class FuncUtil {

    public static String CAN1BB = "";
    public static boolean PRESSHOME = false;
    //蓝牙是否连接
//    public static volatile boolean BLUETOOTHCONNCTED = false;

    //串口
    public static SerialHelperttlLd serialHelperttl;
    //串口
    public static SerialHelperttlLd3 serialHelperttl3;
    public static Timer timer ;

    //20b数据
    public static String b20  ;

    //当前Activity
    public static String currentActivity;
    public static String bb ;


    @Deprecated
    public static String bcHandler="FRONT" ;

    @Deprecated
    public static List<String> bcHandlerdata;

    public static MusicVo musicVo;
    public static volatile boolean sendCDFLG;
    public static volatile boolean openFM;

    public static volatile boolean open1BB ;
    public static volatile boolean pointOk;


    public FuncUtil() {
    }



    public  static synchronized void  sendShellCommand(String cmd){
        OutputStream oup = null ;
        try {

            Process process = Runtime.getRuntime().exec(cmd);
             oup =  process.getOutputStream() ;
             oup.flush();
             oup.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oup != null) {
                oup = null ;
            }
        }
    }


    /**
     *
     * 获取 当前时间
     *
     * @return
     */
    public static String getCurrentDate(){

        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  dfs.format(new Date());
    }

    public static BigDecimal add(BigDecimal num1,BigDecimal num2) {
        return  num1.add(num2);
    }



    public static  void saveBrightness(Context context, int brightness) {

        Log.i("brightness",brightness+"----------==========++++") ;
        Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        Settings.System.putInt(MyApp.getGlobalContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        context.getContentResolver().notifyChange(uri, null);

    }


    public static String time="" ;

    public static void setBluetooth() {

        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

            LogUtils.printI(FuncUtil.class.getSimpleName(), "setBluetooth-----adapter.isEnabled="+adapter.isEnabled());
            if (!adapter.isEnabled()){ //测试几次，判断不准
                adapter.enable() ;
            }


            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);
            setDiscoverableTimeout.invoke(adapter, 0);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 0);

            LogUtils.printI(FuncUtil.class.getSimpleName(), "setBluetooth-----open Bluetooth search");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initSerialHelper() {

        try {
            serialHelperttl = new SerialHelperttlLd("/dev/ttyS1",115200) ;
            serialHelperttl.open();

            initSerialHelper3();

            FuncUtil.sendCDFLG = true ;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initSerialHelper3() throws IOException {
        serialHelperttl3 = new SerialHelperttlLd3("/dev/ttyS3",115200) ;
        serialHelperttl3.open();
    }


    public static List<String> getCan20b(String data) {
        ArrayList<String> arr = new ArrayList<String>() ;
        arr.add("20b") ;
        arr.add("7") ;
        data = data.substring(4,data.length()) ;

        String temp = "";
        for(int i=0;i<data.length();i++){

            if(i%2==0){//每隔两个
                temp = data.charAt(i)+"";
            }else {
                temp = temp + data.charAt(i);
            }

            if(temp.length()==2) {
                arr.add(temp) ;
            }
        }
        return arr ;
    }

    public static List<String> getCanbb(String data) {
        ArrayList<String> arr = new ArrayList<String>() ;
        arr.add("bb") ;
        arr.add("5") ;
        data = data.substring(4,data.length()) ;

        String temp = "";
        for(int i=0;i<data.length();i++){

            if(i%2==0){//每隔两个
                temp = data.charAt(i)+"";
            }else {
                temp = temp + data.charAt(i);
            }

            if(temp.length()==2) {
                arr.add(temp) ;
            }
        }
        return arr ;
    }

    public static List<String> getCanbc(String data) {
        ArrayList<String> arr = new ArrayList<String>() ;
        arr.add("bc") ;
        arr.add("6") ;
        data = data.substring(4,data.length()) ;

        String temp = "";
        for(int i=0;i<data.length();i++){

            if(i%2==0){//每隔两个
                temp = data.charAt(i)+"";
            }else {
                temp = temp + data.charAt(i);
            }

            if(temp.length()==2) {
                arr.add(temp) ;
            }
        }
        return arr ;
    }



    /**
    * @description:
    * @createDate: 2023/6/1
    */
    public static void resetCanData() {

    }
}
