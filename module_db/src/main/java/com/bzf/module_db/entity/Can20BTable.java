package com.bzf.module_db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bzf.module_db.DBTableNames;

/**
 * @description: 前空调数据
 * @createDate: 2023/5/7
 */
@Entity(tableName = DBTableNames.TABLE_NAME_CAN20B)
public class Can20BTable {

    @PrimaryKey
    @NonNull
    private String deviceId = "";

    private String driverTemp;
    private String frontSeatTemp;
    private String driverWind;
    private String frontSeatWind;
    //空调按键开关状态
    private String acKeyStatus;

    private String data1 = "";
    private String data2 = "";
    private String data3 = "";

    public String getDriverTemp() {
        return driverTemp;
    }

    public void setDriverTemp(String driverTemp) {
        this.driverTemp = driverTemp;
    }

    public String getFrontSeatTemp() {
        return frontSeatTemp;
    }

    public void setFrontSeatTemp(String frontSeatTemp) {
        this.frontSeatTemp = frontSeatTemp;
    }

    public String getDriverWind() {
        return driverWind;
    }

    public void setDriverWind(String driverWind) {
        this.driverWind = driverWind;
    }

    public String getFrontSeatWind() {
        return frontSeatWind;
    }

    public void setFrontSeatWind(String frontSeatWind) {
        this.frontSeatWind = frontSeatWind;
    }

    public String getAcKeyStatus() {
        return acKeyStatus;
    }

    public void setAcKeyStatus(String acKeyStatus) {
        this.acKeyStatus = acKeyStatus;
    }


    @NonNull
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(@NonNull String deviceId) {
        this.deviceId = deviceId;
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

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    @Override
    public String toString() {
        return "Can20bTable{" +
                "deviceId='" + deviceId + '\'' +
                ", leftTemp='" + driverTemp + '\'' +
                ", rightTemp='" + frontSeatTemp + '\'' +
                ", leftWind='" + driverWind + '\'' +
                ", rightWind='" + frontSeatWind + '\'' +
                ", airOnOff='" + acKeyStatus + '\'' +
                ", data1='" + data1 + '\'' +
                ", data2='" + data2 + '\'' +
                ", data3='" + data3 + '\'' +
                '}';
    }
}
