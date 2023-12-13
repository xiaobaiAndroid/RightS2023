package com.android.launcher.vo;


public class CarInfoCarsetSpeedLimitVo  {

    public long id ;
    public int carInfoCarsetSpeedLimitIcon;
    public String carInfoCarsetSpeedLimitName ;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCarInfoCarsetSpeedLimitIcon() {
        return carInfoCarsetSpeedLimitIcon;
    }

    public void setCarInfoCarsetSpeedLimitIcon(int carInfoCarsetSpeedLimitIcon) {
        this.carInfoCarsetSpeedLimitIcon = carInfoCarsetSpeedLimitIcon;
    }

    public String getCarInfoCarsetSpeedLimitName() {
        return carInfoCarsetSpeedLimitName;
    }

    public void setCarInfoCarsetSpeedLimitName(String carInfoCarsetSpeedLimitName) {
        this.carInfoCarsetSpeedLimitName = carInfoCarsetSpeedLimitName;
    }

    @Override
    public String toString() {
        return "CarInfoCarsetSpeedLimitVo{" +
                "id=" + id +
                ", carInfoCarsetSpeedLimitIcon=" + carInfoCarsetSpeedLimitIcon +
                ", carInfoCarsetSpeedLimitName='" + carInfoCarsetSpeedLimitName + '\'' +
                '}';
    }
}
