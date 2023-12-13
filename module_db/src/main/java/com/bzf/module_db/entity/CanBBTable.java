package com.bzf.module_db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bzf.module_db.DBTableNames;

/**
* @description:  前排风向
* @createDate: 2023/5/7
*/
@Entity(tableName = DBTableNames.TABLE_NAME_CANBB)
public class CanBBTable {

    @PrimaryKey
    @NonNull
    private String deviceId = "";

    //主驾风向
    private String leftWindDirection = "";

    //副驾风向
    private String rightWindDirection = "";

    private String auto = "00";

    private String data1 = "";
    private String data2 = "";
    private String data3 = "";

    public String getLeftWindDirection() {
        return leftWindDirection;
    }

    public void setLeftWindDirection(String leftWindDirection) {
        this.leftWindDirection = leftWindDirection;
    }

    public String getRightWindDirection() {
        return rightWindDirection;
    }

    public void setRightWindDirection(String rightWindDirection) {
        this.rightWindDirection = rightWindDirection;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
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
        return "CanBBTable{" +
                "deviceId='" + deviceId + '\'' +
                ", mainDriverAirflow='" + leftWindDirection + '\'' +
                ", copilotAirflow='" + rightWindDirection + '\'' +
                ", auto='" + auto + '\'' +
                ", data1='" + data1 + '\'' +
                ", data2='" + data2 + '\'' +
                ", data3='" + data3 + '\'' +
                '}';
    }
}
