package com.android.launcher.vo;

public class CarInfoLampVo   {

    public long id ;
    public int carInfoLampIcon ;
    public String carInfoLampString ;
    public String checkStatus ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CarInfoLampVo{" +
                "id=" + id +
                ", carInfoLampIcon=" + carInfoLampIcon +
                ", carInfoLampString='" + carInfoLampString + '\'' +
                ", checkStatus='" + checkStatus + '\'' +
                '}';
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public int getCarInfoLampIcon() {
        return carInfoLampIcon;
    }

    public void setCarInfoLampIcon(int carInfoLampIcon) {
        this.carInfoLampIcon = carInfoLampIcon;
    }

    public String getCarInfoLampString() {
        return carInfoLampString;
    }

    public void setCarInfoLampString(String carInfoLampString) {
        this.carInfoLampString = carInfoLampString;
    }


}
