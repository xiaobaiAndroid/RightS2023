package com.android.launcher.vo;

public class CarInfoVo {


    public String id ;

    public String carInfoName ;

    public int carInfoIcon ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarInfoName() {
        return carInfoName;
    }

    public void setCarInfoName(String carInfoName) {
        this.carInfoName = carInfoName;
    }

    public int getCarInfoIcon() {
        return carInfoIcon;
    }

    public void setCarInfoIcon(int carInfoIcon) {
        this.carInfoIcon = carInfoIcon;
    }

    @Override
    public String toString() {
        return "CarInfo{" +
                "id='" + id + '\'' +
                ", carInfoName='" + carInfoName + '\'' +
                ", carInfoIcon=" + carInfoIcon +
                '}';
    }


}
