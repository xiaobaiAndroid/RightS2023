package com.android.launcher.airsystem;

/**
* @description: 空调开启状态
* @createDate: 2023/5/2
*/
public enum  AirCondOnStatus {


    AIR_ON("0"),
    AIR_OFF("2");

    private String status;


   private AirCondOnStatus(String status){
        this.status = status;
   }

   public String getValue(){
       return status;
   }
}
