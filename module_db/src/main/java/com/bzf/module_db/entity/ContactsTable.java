package com.bzf.module_db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.bzf.module_db.DBTableNames;

/**
* @description: 联系人
* @createDate: 2023/5/29
*/
@Entity(tableName = DBTableNames.TABLE_NAME_CONTACTS)
public class ContactsTable {

    @PrimaryKey
    @NonNull
    private String id = "";
    //蓝牙设备id
    private String deviceId="";
    private String name="";
    private String phone1="";
    private String phone2="";
    private String phone3="";
    //预留字段
    private String data1="";
    private String data2="";
    private String data3="";
    private String data4="";

    @Ignore
    private boolean isSelect = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public ContactsTable(){

    }

    @Override
    public String toString() {
        return "ContactsEntity{" +
                "id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", phone1='" + phone1 + '\'' +
                ", phone2='" + phone2 + '\'' +
                ", phone3='" + phone3 + '\'' +
                '}';
    }
}
