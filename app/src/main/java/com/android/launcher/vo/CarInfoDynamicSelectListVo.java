package com.android.launcher.vo;

public class CarInfoDynamicSelectListVo {

    public long id ;
    public int carInfoDynamicIcon;
    public String carInfoDynamicName ;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCarInfoDynamicIcon() {
        return carInfoDynamicIcon;
    }

    public void setCarInfoDynamicIcon(int carInfoDynamicIcon) {
        this.carInfoDynamicIcon = carInfoDynamicIcon;
    }

    public String getCarInfoDynamicName() {
        return carInfoDynamicName;
    }

    public void setCarInfoDynamicName(String carInfoDynamicName) {
        this.carInfoDynamicName = carInfoDynamicName;
    }

    @Override
    public String toString() {
        return "CarInfoDynamicSelectListVo{" +
                "id=" + id +
                ", carInfoDynamicIcon=" + carInfoDynamicIcon +
                ", carInfoDynamicName='" + carInfoDynamicName + '\'' +
                '}';
    }
}
