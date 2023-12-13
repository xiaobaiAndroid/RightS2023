package com.android.launcher.vo;

public class CarSetVo {

    public String id ;

    public String carSetName ;

    public int carSetIcon ;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarSetName() {
        return carSetName;
    }

    public void setCarSetName(String carSetName) {
        this.carSetName = carSetName;
    }

    public int getCarSetIcon() {
        return carSetIcon;
    }

    public void setCarSetIcon(int carSetIcon) {
        this.carSetIcon = carSetIcon;
    }

    @Override
    public String toString() {
        return "CarSet{" +
                "id='" + id + '\'' +
                ", carSetName='" + carSetName + '\'' +
                ", carSetIcon='" + carSetIcon + '\'' +
                '}';
    }
}
