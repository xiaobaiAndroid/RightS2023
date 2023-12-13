package com.android.launcher.vo;

public class CarInfoAirVo {

    public long id ;

    public int carInfoIcon;
    public String carInfoName ;
    public String checkStatus ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCarInfoIcon() {
        return carInfoIcon;
    }

    public void setCarInfoIcon(int carInfoIcon) {
        this.carInfoIcon = carInfoIcon;
    }

    public String getCarInfoName() {
        return carInfoName;
    }

    public void setCarInfoName(String carInfoName) {
        this.carInfoName = carInfoName;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    @Override
    public String toString() {
        return "CarInfoAirVo{" +
                "id=" + id +
                ", carInfoIcon=" + carInfoIcon +
                ", carInfoName='" + carInfoName + '\'' +
                ", checkStatus='" + checkStatus + '\'' +
                '}';
    }
}
