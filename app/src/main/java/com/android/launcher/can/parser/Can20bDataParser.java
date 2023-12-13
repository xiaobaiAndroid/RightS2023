package com.android.launcher.can.parser;

import com.android.launcher.entity.BinaryEntity;
import com.bzf.module_db.entity.Can1E5Table;
import com.bzf.module_db.entity.Can20BTable;

/**
 * @date： 2023/11/12
 * @author: 78495
*/
public class Can20bDataParser {

    private Can20BTable can20BTable;
    private BinaryEntity statusBinaryEntity;

    public Can20bDataParser(Can20BTable table){
        can20BTable = table;
        if(can20BTable!=null){
            String status = can20BTable.getAcKeyStatus();
            if(status!=null && status.length() == 2){
                statusBinaryEntity = new BinaryEntity(status);
            }
        }
    }

//            String leftAutMode = d5Binary.getB0();
//        String rightAutMode = d5Binary.getB1();
//        String innerLoopStatus = d5Binary.getB4();
//        String frontDemistStatus = d5Binary.getB2();


    //左侧空调自动模式
    public boolean isLeftAutoMode(){
        if(statusBinaryEntity!=null){
            String leftAutMode = statusBinaryEntity.getB0();
            if(BinaryEntity.Value.NUM_1.getValue().equals(leftAutMode)){
                return true;
            }
        }
        return false;
    }


    //右侧空调自动模式
    public boolean isRightAutoMode(){
        if(statusBinaryEntity!=null){
            String rightAutMode = statusBinaryEntity.getB1();
            if(BinaryEntity.Value.NUM_1.getValue().equals(rightAutMode)){
                return true;
            }
        }
        return false;
    }


    //内循环是否开启
    public boolean innerLoopIsOpen(){
        if(statusBinaryEntity!=null){
            String innerLoopStatus = statusBinaryEntity.getB4();
            if(BinaryEntity.Value.NUM_1.getValue().equals(innerLoopStatus)){
                return true;
            }
        }
        return false;
    }

    //前挡风除雾是否开启
    public boolean frontDemistIsOpen(){
        if(statusBinaryEntity!=null){
            String frontDemistStatus = statusBinaryEntity.getB2();
            if(BinaryEntity.Value.NUM_1.getValue().equals(frontDemistStatus)){
                return true;
            }
        }
        return false;
    }

    public String getFrontSeatWind() {
        return can20BTable.getFrontSeatWind();
    }

    public String getDriverWind() {
        return can20BTable.getDriverWind();
    }

    public String getFrontSeatTemp() {
        return can20BTable.getFrontSeatTemp();
    }

    public String getDriverTemp() {
        return can20BTable.getDriverTemp();
    }
}
