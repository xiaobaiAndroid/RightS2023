package com.android.launcher.vo;

public class CarInfoLampFrontBackVo {

    public long id ;
    public int carInfoLampFrontBackIcon ;
    public String carInfoLampFrontBackString ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCarInfoLampFrontBackIcon() {
        return carInfoLampFrontBackIcon;
    }

    public void setCarInfoLampFrontBackIcon(int carInfoLampFrontBackIcon) {
        this.carInfoLampFrontBackIcon = carInfoLampFrontBackIcon;
    }

    public String getCarInfoLampFrontBackString() {
        return carInfoLampFrontBackString;
    }

    public void setCarInfoLampFrontBackString(String carInfoLampFrontBackString) {
        this.carInfoLampFrontBackString = carInfoLampFrontBackString;
    }

    @Override
    public String toString() {
        return "CarInfoLampFrontBackVo{" +
                "id=" + id +
                ", carInfoLampFrontBackIcon=" + carInfoLampFrontBackIcon +
                ", carInfoLampFrontBackString='" + carInfoLampFrontBackString + '\'' +
                '}';
    }
}
