package com.bzf.module_db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.bzf.module_db.DBTableNames;

/**
 * @description: 汽车设定参数
 * @createDate: 2023/5/29
 */
@Entity(tableName = DBTableNames.TABLE_NAME_CARSETUP)
public class CarSetupTable {

    @PrimaryKey
    @NonNull
    private String deviceId = "";

    //速度限制
    private String speedLimit = "";

    //中央门锁反馈
    private boolean centerDoorLock = true;

    //锁车时自动合拢
    private boolean automaticRarFolding = true;

    //后舱盖高度限制
    private boolean trunkOpenLimit = true;

    //转向柱辅助是否开启
    private boolean steeringColumnAssist = true;
    //座位辅助是否开启
    private boolean seatAssist = true;

    //后驶时后视镜下降
    private boolean rearviewMirrorDown = true;

    //环境照明
    private String ambientLighting = "75";

    //内部照明延迟设定 3C:延迟60秒
    private String innerLighting = "3C";
    //外部灯光延迟设定
    private String externalLighting = "3C";

    //方向盘滚轮 音量调节的方向，上加下减
    private boolean wheelDirection = true;

    //仪表盘警报的音量
    private String dashBoardVolume = "30";

    //头枕放倒
    private boolean headrestRecline = true;

    //安全带辅助
    private boolean seatBeltAssist = true;

    //中央显示屏亮度
    private int centralDisplayLum = 255;
    //仪表盘亮度
    private int meterDisplayLum = 255;

    private String data1 = "";
    private String data2 = "";
    private String data3 = "";
    private String data4 = "";
    private String data5 = "";
    private String data6 = "";
    private String data7 = "";


    @Ignore
    private boolean isSelect;

    @NonNull
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(@NonNull String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(String speedLimit) {
        this.speedLimit = speedLimit;
    }

    public boolean isCenterDoorLock() {
        return centerDoorLock;
    }

    public void setCenterDoorLock(boolean centerDoorLock) {
        this.centerDoorLock = centerDoorLock;
    }

    public boolean isAutomaticRarFolding() {
        return automaticRarFolding;
    }

    public void setAutomaticRarFolding(boolean automaticRarFolding) {
        this.automaticRarFolding = automaticRarFolding;
    }

    public boolean isTrunkOpenLimit() {
        return trunkOpenLimit;
    }

    public void setTrunkOpenLimit(boolean trunkOpenLimit) {
        this.trunkOpenLimit = trunkOpenLimit;
    }



    public boolean isRearviewMirrorDown() {
        return rearviewMirrorDown;
    }

    public void setRearviewMirrorDown(boolean rearviewMirrorDown) {
        this.rearviewMirrorDown = rearviewMirrorDown;
    }

    public String getAmbientLighting() {
        return ambientLighting;
    }

    public void setAmbientLighting(String ambientLighting) {
        this.ambientLighting = ambientLighting;
    }

    public String getInnerLighting() {
        return innerLighting;
    }

    public void setInnerLighting(String innerLighting) {
        this.innerLighting = innerLighting;
    }

    public String getExternalLighting() {
        return externalLighting;
    }

    public void setExternalLighting(String externalLighting) {
        this.externalLighting = externalLighting;
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

    public String getData4() {
        return data4;
    }

    public void setData4(String data4) {
        this.data4 = data4;
    }

    public String getData5() {
        return data5;
    }

    public void setData5(String data5) {
        this.data5 = data5;
    }

    public String getData6() {
        return data6;
    }

    public void setData6(String data6) {
        this.data6 = data6;
    }

    public String getData7() {
        return data7;
    }

    public void setData7(String data7) {
        this.data7 = data7;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isWheelDirection() {
        return wheelDirection;
    }

    public void setWheelDirection(boolean wheelDirection) {
        this.wheelDirection = wheelDirection;
    }

    public String getDashBoardVolume() {
        return dashBoardVolume;
    }

    public void setDashBoardVolume(String dashBoardVolume) {
        this.dashBoardVolume = dashBoardVolume;
    }


    public boolean isHeadrestRecline() {
        return headrestRecline;
    }

    public void setHeadrestRecline(boolean headrestRecline) {
        this.headrestRecline = headrestRecline;
    }

    public boolean isSeatBeltAssist() {
        return seatBeltAssist;
    }

    public void setSeatBeltAssist(boolean seatBeltAssist) {
        this.seatBeltAssist = seatBeltAssist;
    }

    public int getCentralDisplayLum() {
        return centralDisplayLum;
    }

    public void setCentralDisplayLum(int centralDisplayLum) {
        this.centralDisplayLum = centralDisplayLum;
    }

    public int getMeterDisplayLum() {
        return meterDisplayLum;
    }

    public void setMeterDisplayLum(int meterDisplayLum) {
        this.meterDisplayLum = meterDisplayLum;
    }

    public boolean isSteeringColumnAssist() {
        return steeringColumnAssist;
    }

    public void setSteeringColumnAssist(boolean steeringColumnAssist) {
        this.steeringColumnAssist = steeringColumnAssist;
    }

    public boolean isSeatAssist() {
        return seatAssist;
    }

    public void setSeatAssist(boolean seatAssist) {
        this.seatAssist = seatAssist;
    }

    @Override
    public String toString() {
        return "CarSetupTable{" +
                "deviceId='" + deviceId + '\'' +
                ", speedLimit='" + speedLimit + '\'' +
                ", centerDoorLock=" + centerDoorLock +
                ", automaticRarFolding=" + automaticRarFolding +
                ", trunkOpenLimit=" + trunkOpenLimit +
                ", steeringColumnAssist=" + steeringColumnAssist +
                ", seatAssist=" + seatAssist +
                ", rearviewMirrorDown=" + rearviewMirrorDown +
                ", ambientLighting='" + ambientLighting + '\'' +
                ", innerLighting='" + innerLighting + '\'' +
                ", externalLighting='" + externalLighting + '\'' +
                ", wheelDirection=" + wheelDirection +
                ", dashBoardVolume='" + dashBoardVolume + '\'' +
                ", headrestRecline=" + headrestRecline +
                ", seatBeltAssist=" + seatBeltAssist +
                ", centralDisplayLum=" + centralDisplayLum +
                ", meterDisplayLum=" + meterDisplayLum +
                ", data1='" + data1 + '\'' +
                ", data2='" + data2 + '\'' +
                ", data3='" + data3 + '\'' +
                ", data4='" + data4 + '\'' +
                ", data5='" + data5 + '\'' +
                ", data6='" + data6 + '\'' +
                ", data7='" + data7 + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
