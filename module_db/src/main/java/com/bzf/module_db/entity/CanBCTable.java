package com.bzf.module_db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bzf.module_db.DBTableNames;

/**
 * @description: 后排空调
 * @createDate: 2023/5/7
 */
@Entity(tableName = DBTableNames.TABLE_NAME_CANBC)
public class CanBCTable {

    @PrimaryKey
    @NonNull
    private String deviceId = "";

    private String lefttemp = "96";
    private String righttemp = "96";
    private String wind = "60"; //风速
    private String winddic = "88"; //风向

    //状态
    private String status = "00";

    //是否前后空调控制状态 前=00  后=02
    private String frontBackStatus = "00";

    private String data1 = "";
    private String data2 = "";
    private String data3 = "";

    public String getLefttemp() {
        return lefttemp;
    }

    public void setLefttemp(String lefttemp) {
        this.lefttemp = lefttemp;
    }

    public String getRighttemp() {
        return righttemp;
    }

    public void setRighttemp(String righttemp) {
        this.righttemp = righttemp;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWinddic() {
        return winddic;
    }

    public void setWinddic(String winddic) {
        this.winddic = winddic;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getFrontBackStatus() {
        return frontBackStatus;
    }

    public void setFrontBackStatus(String frontBackStatus) {
        this.frontBackStatus = frontBackStatus;
    }

    @Override
    public String toString() {
        return "CanBCTable{" +
                "deviceId='" + deviceId + '\'' +
                ", lefttemp='" + lefttemp + '\'' +
                ", righttemp='" + righttemp + '\'' +
                ", wind='" + wind + '\'' +
                ", winddic='" + winddic + '\'' +
                ", status='" + status + '\'' +
                ", frontBackStatus='" + frontBackStatus + '\'' +
                ", data1='" + data1 + '\'' +
                ", data2='" + data2 + '\'' +
                ", data3='" + data3 + '\'' +
                '}';
    }
}
