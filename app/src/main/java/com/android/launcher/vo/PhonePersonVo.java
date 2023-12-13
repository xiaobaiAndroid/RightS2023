package com.android.launcher.vo;

public class PhonePersonVo {

    public long id ;
    public int phoneIcon;
    public String phoneName ;
    public String phoneNum ;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPhoneIcon() {
        return phoneIcon;
    }

    public void setPhoneIcon(int phoneIcon) {
        this.phoneIcon = phoneIcon;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "PhonePersonVo{" +
                "id=" + id +
                ", phoneIcon='" + phoneIcon + '\'' +
                ", phoneName='" + phoneName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
