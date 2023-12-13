package com.android.launcher.util;

import android.content.Context;

import com.android.launcher.R;

import java.util.Calendar;

/**
* @description:
* @createDate: 2023/5/3
*/
public class TimeUtils {


    /**
     * 获取时间
     * @return
     */
    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改


        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        String hourStr;
        if(hour < 10){
            hourStr = "0"+hour;
        }else{
            hourStr = String.valueOf(hour);
        }
        String minuteStr;
        if(minute < 10){
            minuteStr = "0"+minute;
        }else{
            minuteStr = String.valueOf(minute);
        }

        return hourStr+":"+minuteStr ;
    }

    public static String covertFormat1(Context context,long time) {
        if(time == 0){
            return "00:00";
        }
        int day = (int) (time / 60 / 60 / 24);
        int hour = (int) (time / 60 / 60);
        int minute = (int) (time / 60);

        StringBuilder stringBuilder =  new StringBuilder();
        if(day > 0){
            stringBuilder.append(day).append(context.getResources().getString(R.string.day));
            if(hour < 10){
                stringBuilder.append(0).append(hour);
            }else{
                stringBuilder.append(hour);
            }
            stringBuilder.append(context.getResources().getString(R.string.hour));
            if(minute < 10){
                stringBuilder.append(0).append(minute);
            }else{
                stringBuilder.append(minute);
            }
            stringBuilder.append(context.getResources().getString(R.string.mniute));
        }else if(hour > 0){
            if(hour < 10){
                stringBuilder.append(0).append(hour);
            }else{
                stringBuilder.append(hour);
            }
            stringBuilder.append(context.getResources().getString(R.string.hour));
            if(minute < 10){
                stringBuilder.append(0).append(minute);
            }else{
                stringBuilder.append(minute);
            }
            stringBuilder.append(context.getResources().getString(R.string.mniute));
        }else{
            if(minute < 10){
                stringBuilder.append(0).append(minute);
            }else{
                stringBuilder.append(minute);
            }
            stringBuilder.append(context.getResources().getString(R.string.mniute));
        }

        return stringBuilder.toString();
    }
}
