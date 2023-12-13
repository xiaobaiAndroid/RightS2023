package com.android.launcher.util;

import android.content.Context;
import android.media.AudioManager;

import com.android.launcher.MyApp;
import com.bzf.module_db.entity.CarSetupTable;

import java.util.HashMap;
import java.util.Map;

public class CommonUtil {




    public static char[] ch = "0000000000000000".toCharArray();
    public static Map<String, String> map = new HashMap<String, String>() {{
        put("0", "13");
        put("1", "14");
        put("2", "12");
        put("3", "11");
        put("4", "7");
        put("5", "8");
        put("6", "6");
        put("7", "5");
        put("12", "9");
        put("13", "15");
        put("14", "10");
        put("15", "16");
    }};

    public static String getTime(int progress) {// 将毫秒转换成00:00
        int sec = progress / 1000; // 获取秒
        int min = sec / 60; // 获取分
        sec = sec % 60;// 获取剩余秒数 (分:秒)
        return String.format("%02d:%02d", min, sec);
    }


    /**
     * 16进制字符串转二进制字符串
     *
     * @param hexString
     * @return
     */
    public static String convertHexToBinary(String hexString) {
        long l = Long.parseLong(hexString, 16);
        String binaryString = Long.toBinaryString(l);
        int shouldBinaryLen = hexString.length() * 4;
        StringBuffer addZero = new StringBuffer();
        int addZeroNum = shouldBinaryLen - binaryString.length();
        for (int i = 1; i <= addZeroNum; i++) {
            addZero.append("0");
        }
        return addZero.toString() + binaryString;
    }




//
//    /**
//     * 获取 id为35d的内容
//     * @return
//     * @param carSetupTable
//     */
//    public static String getCommand35d(CarSetupTable carSetupTable) {
//        //内部照明
//        String preLamp = "AA0000060000035D";
//
//        String LAMP = carSetupTable.getInnerLighting();
//        String LAMPBACK = carSetupTable.getExternalLighting();
//        String paramenvValue = carSetupTable.getAmbientLighting();
//        boolean getOnOrOffTheBus = carSetupTable.isGetOnOrOffTheBus();
//        String GETTINGOFF = "";
//        if(getOnOrOffTheBus){
//            GETTINGOFF = "50";
//        }else{
//            GETTINGOFF = "55";
//        }
//
//        //后舱盖高度限制
//        //开启
//        String REAR = "D";
//        //关闭
////        String REAR = "C";
//
//        //外后视镜
//        String MIRROR = "1"; //开启
////        String MIRROR = "0"; //关闭
////
//
////        Log.i("carinfolamp",preLamp+"=====1===="+LAMP+"===2="+LAMPBACK+"==3="+paramenvValue+"==4=="+GETTINGOFF+"===5=="+REAR+"==6=="+MIRROR+"===7="+"E50000") ;
//        String command = preLamp + LAMP + LAMPBACK + paramenvValue + GETTINGOFF + REAR + MIRROR + "E50000";
//
//        return command;
//    }


    /**
     *
     * 二进制字符串转16进制字符串
     *
     * @param bString
     * @return
     */
    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    /**
     *
     * 修改音量
     *
     * @param progress
     */
    public void updateVolume(int progress){
        AudioManager am = (AudioManager) MyApp.getGlobalContext().getSystemService(Context.AUDIO_SERVICE) ;
        am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0) ;
        int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC) ;
        am.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume,am.FLAG_SHOW_UI);
    }



}
