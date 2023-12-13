package com.bzf.module_db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bzf.module_db.DBTableNames;

/**
 * 除雾状态
 * @date： 2023/11/3
 * @author: 78495
*/
@Entity(tableName = DBTableNames.TABLE_NAME_CAN007)
public class Can007Table {

    @PrimaryKey
    @NonNull
    private String deviceId = "";

    private String d1 = "00";
    private String d2= "00";
    //后挡风除雾状态，转成二进制：b6=1：后挡风除雾指示灯长亮。b7=1,b6=1，后挡风除雾指示灯闪烁
    private String d3= "00";
    private String d4= "00";
    private String d5= "00";
    private String d6= "00";

    private String data1 = "";
    private String data2 = "";


    @NonNull
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(@NonNull String deviceId) {
        this.deviceId = deviceId;
    }

    public String getD1() {
        return d1;
    }

    public void setD1(String d1) {
        this.d1 = d1;
    }

    public String getD2() {
        return d2;
    }

    public void setD2(String d2) {
        this.d2 = d2;
    }

    public String getD3() {
        return d3;
    }

    public void setD3(String d3) {
        this.d3 = d3;
    }

    public String getD4() {
        return d4;
    }

    public void setD4(String d4) {
        this.d4 = d4;
    }

    public String getD5() {
        return d5;
    }

    public void setD5(String d5) {
        this.d5 = d5;
    }

    public String getD6() {
        return d6;
    }

    public void setD6(String d6) {
        this.d6 = d6;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }
}
