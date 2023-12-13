package com.bzf.module_db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bzf.module_db.DBTableNames;

/**
 * 控制空调状态
 * @date： 2023/11/3
 * @author: 78495
 */
@Entity(tableName = DBTableNames.TABLE_NAME_CAN1E5)
public class Can1E5Table {

    @PrimaryKey
    @NonNull
    private String deviceId = "";

    private String driverTemp;
    private String frontSeatTemp;
    private String driverWind;
    private String frontSeatWind;

    //风向
    private String windDirection;

    //气流模式
    private String airflowMode;
    //声音模式
    private String audioMode;
    //空调状态，包含空调开关，压缩机开关，风速自动，风向自动
    private String status = "00";

    private String data1 = "";
    private String data2 = "";

    @NonNull
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(@NonNull String deviceId) {
        this.deviceId = deviceId;
    }

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

    public String getAirflowMode() {
        return airflowMode;
    }

    public void setAirflowMode(String airflowMode) {
        this.airflowMode = airflowMode;
    }

    public String getAudioMode() {
        return audioMode;
    }

    public void setAudioMode(String audioMode) {
        this.audioMode = audioMode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    @Override
    public String toString() {
        return "Can1E5Table{" +
                "deviceId='" + deviceId + '\'' +
                ", driverTemp='" + driverTemp + '\'' +
                ", frontSeatTemp='" + frontSeatTemp + '\'' +
                ", driverWind='" + driverWind + '\'' +
                ", frontSeatWind='" + frontSeatWind + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", airflowMode='" + airflowMode + '\'' +
                ", audioMode='" + audioMode + '\'' +
                ", status='" + status + '\'' +
                ", data1='" + data1 + '\'' +
                ", data2='" + data2 + '\'' +
                '}';
    }
}
