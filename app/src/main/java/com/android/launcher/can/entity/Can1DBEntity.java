package com.android.launcher.can.entity;

import com.android.launcher.entity.BinaryEntity;

/**
* @description:
* @createDate: 2023/7/25
*/
public class Can1DBEntity {

    private BinaryEntity d1;
    private BinaryEntity d2;
    private BinaryEntity d3;
    private BinaryEntity d4 = new BinaryEntity("FF");
    private BinaryEntity d5;

    public BinaryEntity getD1() {
        return d1;
    }

    public void setD1(BinaryEntity d1) {
        this.d1 = d1;
    }

    public BinaryEntity getD2() {
        return d2;
    }

    public void setD2(BinaryEntity d2) {
        this.d2 = d2;
    }

    public BinaryEntity getD3() {
        return d3;
    }

    public void setD3(BinaryEntity d3) {
        this.d3 = d3;
    }

    public BinaryEntity getD4() {
        return d4;
    }


    public BinaryEntity getD5() {
        return d5;
    }

    public void setD5(BinaryEntity d5) {
        this.d5 = d5;
    }

    @Override
    public String toString() {
        return "Can1DBEntity{" +
                "d1=" + d1 +
                ", d2=" + d2 +
                ", d3=" + d3 +
                ", d4=" + d4 +
                ", d5=" + d5 +
                '}';
    }
}
