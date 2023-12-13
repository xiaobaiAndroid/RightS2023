package com.android.launcher.util;

import android.content.Context;

import com.android.launcher.type.LanguageType;
import com.android.launcher.R;

import java.util.Calendar;
import java.util.TimeZone;

import module.common.utils.SPUtils;

/**
 * @date： 2023/10/19
 * @author: 78495
*/
public class CarDateUtils {


    /**
     * 初始化日期
     * @param week
     * @param date
     */
    public  static String getDate(Context context, String timeZoneId) {
        final Calendar c = Calendar.getInstance();
        int languageType = SPUtils.getInt(context, SPUtils.SP_SELECT_LANGUAGE, LanguageType.SYSTEM.ordinal());
        if(LanguageUtils.isCN()){
            c.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        }else if(languageType == LanguageType.SYSTEM.ordinal()){
            if (LanguageUtils.isCN()) {
                c.setTimeZone(TimeZone.getTimeZone(timeZoneId));
            }
        }

        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码


        String date;
        if(languageType == LanguageType.ZH.ordinal()){
            date = mYear + "/" + mMonth + "/" + mDay;
        }else if(languageType == LanguageType.SYSTEM.ordinal()){
            if (LanguageUtils.isCN()) {
                date = mYear + "/" + mMonth + "/" + mDay;
            }else{
                String month = getENMonthName(c);
                date = month +" "+mDay +","+mYear;
            }
        }else{
            String month = getENMonthName(c);
            date = month +" "+mDay +","+mYear;
        }

        return date;
    }

    public static String getWeek(Context context, String timeZoneId){
        final Calendar c = Calendar.getInstance();
        int languageType = SPUtils.getInt(context, SPUtils.SP_SELECT_LANGUAGE, LanguageType.SYSTEM.ordinal());
        if(LanguageUtils.isCN()){
            c.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        }else if(languageType == LanguageType.SYSTEM.ordinal()){
            if (LanguageUtils.isCN()) {
                c.setTimeZone(TimeZone.getTimeZone(timeZoneId));
            }
        }

        String week = String.valueOf(c.get(Calendar.DAY_OF_WEEK));

        if ("1".equals(week)) {
            week = context.getResources().getString(R.string.sunday);
        } else if ("2".equals(week)) {
            week = context.getResources().getString(R.string.monday);
        } else if ("3".equals(week)) {
            week = context.getResources().getString(R.string.tuesday);
        } else if ("4".equals(week)) {
            week = context.getResources().getString(R.string.wednesday);
        } else if ("5".equals(week)) {
            week = context.getResources().getString(R.string.thursday);
        } else if ("6".equals(week)) {
            week = context.getResources().getString(R.string.friday);
        } else if ("7".equals(week)) {
            week = context.getResources().getString(R.string.saturday);
        }

        return week;
    }


    /**
     * @description:
     *             //January（一月）
     *             //February（二月）
     *             //March（三月）
     *             //April（四月）
     *             //May（五月）
     *             //June（六月）
     *             //July（七月）
     *             //August（八月）
     *             //September（九月）
     *             //October（十月）
     *             //November（十一月）
     *             //December（十二月）
     * @createDate: 2023/9/14
     */
    private static String getENMonthName(Calendar c) {

        String month;
        switch (c.get(Calendar.MONTH)){
            case 0:
                month = "January";
                break;
            case 1:
                month = "February";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            case 11:
                month = "December";
                break;
            default:
                month ="";
                break;
        }
        return month;
    }
}
