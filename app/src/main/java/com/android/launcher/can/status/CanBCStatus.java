package com.android.launcher.can.status;

/**
* @description:ID
 *      B0   B1	B2	B3	B4	B5
 * 0BC:	5A	8C	42	88	00	00
* @createDate: 2023/6/5
*/
public class CanBCStatus {


    /**
    * @description: 前后控制状态
    * @createDate: 2023/6/5
    */
    public enum B5Status{
        FRONT_AIR("00"), //前空调控制
        BACK_AIR("02"); //后空调控制

        private String value;

        B5Status(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
