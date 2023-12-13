package com.android.launcher.can.parser;

import android.view.View;

import com.android.launcher.entity.BinaryEntity;
import com.bzf.module_db.AcKeyStatus;
import com.bzf.module_db.entity.Can007Table;
import com.bzf.module_db.entity.Can20BTable;

import module.common.utils.ButtonUtils;

/**
 * @date： 2023/11/12
 * @author: 78495
*/
public class Can007DataParser {

    private Can007Table table;
    private BinaryEntity statusBinaryEntity;

    public Can007DataParser(Can007Table table){
        this.table = table;
        if(table!=null){
            String status = table.getD3();
            if(status!=null && status.length() == 2){
                statusBinaryEntity = new BinaryEntity(status);
            }
        }
    }


    //后挡风除雾状态
    public AcKeyStatus getRearDemistStatus(){
        if(statusBinaryEntity!=null){
            if (BinaryEntity.Value.NUM_1.getValue().equals(statusBinaryEntity.getB6()) && BinaryEntity.Value.NUM_1.getValue().equals(statusBinaryEntity.getB7())) {
                //后挡风除雾指示灯闪烁
                return AcKeyStatus.BREAKDOWN;
            } else if (BinaryEntity.Value.NUM_1.getValue().equals(statusBinaryEntity.getB6())) {
                //后挡风除雾指示灯长亮
                return AcKeyStatus.OPEN;
            } else {
               return AcKeyStatus.CLOSE;
            }
        }
        return AcKeyStatus.CLOSE;
    }

}
