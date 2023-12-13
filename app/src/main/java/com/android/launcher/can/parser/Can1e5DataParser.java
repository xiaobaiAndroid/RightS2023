package com.android.launcher.can.parser;

import com.android.launcher.entity.BinaryEntity;
import com.bzf.module_db.entity.Can1E5Table;

import module.common.utils.LogUtils;

/**
 * Can1E5数据解析器
 * @date： 2023/11/12
 * @author: 78495
*/
public class Can1e5DataParser {

    private Can1E5Table can1E5Table;
    private BinaryEntity statusBinaryEntity;

    public Can1e5DataParser(Can1E5Table table){
        can1E5Table = table;
        if(can1E5Table!=null){
            String status = can1E5Table.getStatus();
            if(status!=null && status.length() == 2){
                statusBinaryEntity = new BinaryEntity(status);
            }
        }
    }


    //压缩机开启状态
    public boolean isCompressorOpen(){
        if(statusBinaryEntity!=null){
            String acOff = statusBinaryEntity.getB7();
            if(BinaryEntity.Value.NUM_0.getValue().equals(acOff)){
                return true;
            }
        }
        return false;
    }

    //空调开启状态
    public boolean isAcOpen(){
        if(statusBinaryEntity!=null){
            LogUtils.printI(Can1e5DataParser.class.getSimpleName(), "isAcOpen---statusBinaryEntity="+statusBinaryEntity);
            String acOff = statusBinaryEntity.getB5();
            if(BinaryEntity.Value.NUM_0.getValue().equals(acOff)){
                return true;
            }
        }
        return false;
    }


    //副驾风向自动
    public boolean isFrontSeatWindDirAuto(){
        if(statusBinaryEntity!=null){
            String auto = statusBinaryEntity.getB4();
            if(BinaryEntity.Value.NUM_1.getValue().equals(auto)){
                return true;
            }
        }
        return false;
    }

    //主驾风向自动
    public boolean isDriveSeatWindDirAuto(){
        if(statusBinaryEntity!=null){
            String auto = statusBinaryEntity.getB3();
            if(BinaryEntity.Value.NUM_1.getValue().equals(auto)){
                return true;
            }
        }
        return false;
    }

    //副驾风速自动
    public boolean isFrontSeatWindAuto(){
        if(statusBinaryEntity!=null){
            String auto = statusBinaryEntity.getB2();
            if(BinaryEntity.Value.NUM_1.getValue().equals(auto)){
                return true;
            }
        }
        return false;
    }

    //主驾风速自动
    public boolean isDriveSeatWindAuto(){
        if(statusBinaryEntity!=null){
            String auto = statusBinaryEntity.getB1();
            if(BinaryEntity.Value.NUM_1.getValue().equals(auto)){
                return true;
            }
        }
        return false;
    }

    //自动空调模式
    public boolean isAutoAcMode(){
        if(isDriveSeatWindDirAuto() && isDriveSeatWindAuto() && isFrontSeatWindDirAuto() && isFrontSeatWindAuto()){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Can1e5DataParser{" +
                "can1E5Table=" + can1E5Table +
                ", statusBinaryEntity=" + statusBinaryEntity +
                '}';
    }

    public String getAirflowPattern() {
        if(can1E5Table!=null){
           return can1E5Table.getAirflowMode();
        }
        return null;
    }
}
