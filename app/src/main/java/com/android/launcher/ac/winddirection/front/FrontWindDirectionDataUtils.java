package com.android.launcher.ac.winddirection.front;

import android.content.Context;

import com.android.launcher.R;
import com.android.launcher.type.CarType;

import java.util.ArrayList;
import java.util.List;

/**
 * 风向数据
 * @date： 2023/11/3
 * @author: 78495
*/
public class FrontWindDirectionDataUtils {


    public static List<WindDirectionItem> getDataByCarType(int carType){
        List<WindDirectionItem> list = new ArrayList<>();
        list.add(new WindDirectionItem(WindDirectionItem.Type.AUTO, FrontWindDirectionCmdValue.AUTO));
        if(carType == CarType.S300.ordinal()){
            //胸
            list.add(new WindDirectionItem(R.drawable.car_air_chest, FrontWindDirectionCmdValue.S300_CHEST));
            // 脚和胸
            list.add(new WindDirectionItem(R.drawable.car_air_lfc, FrontWindDirectionCmdValue.S300_FOOT_CHEST));
            //吹脚
            list.add(new WindDirectionItem(R.drawable.car_air_foot, FrontWindDirectionCmdValue.S300_FOOT));

            //挡风和脚部
            list.add(new WindDirectionItem(R.drawable.car_air_fcl, FrontWindDirectionCmdValue.S300_WINDBREAK_FOOT));
            //挡风和胸部和脚部
            list.add(new WindDirectionItem(R.drawable.car_air_cfl, FrontWindDirectionCmdValue.S300_WINDBREAK_CHEST_FOOT));
            //挡风和胸部
            list.add(new WindDirectionItem(R.drawable.car_air_chest, FrontWindDirectionCmdValue.S300_WINDBREAK_CHEST));
            //挡风
            list.add(new WindDirectionItem(R.drawable.car_air_headup, FrontWindDirectionCmdValue.S300_WINDBREAK));
        }else{
            //吹顶部
            list.add(new WindDirectionItem(R.drawable.car_air_headup, FrontWindDirectionCmdValue.S500_TOP));
            //吹头部
            list.add(new WindDirectionItem(R.drawable.car_air_face, FrontWindDirectionCmdValue.S500_HEAD));
            //吹胸部
            list.add(new WindDirectionItem(R.drawable.car_air_chest, FrontWindDirectionCmdValue.S500_CHEST));
            //吹膝盖
            list.add(new WindDirectionItem(R.drawable.car_air_back_knee, FrontWindDirectionCmdValue.S500_KNEE));
            //吹脚部
            list.add(new WindDirectionItem(R.drawable.car_air_foot, FrontWindDirectionCmdValue.S500_FOOT));
            // 吹胸膝脚 （胸部的风大）
            list.add(new WindDirectionItem(R.drawable.car_air_cfl, FrontWindDirectionCmdValue.S500_CHEST_MAX));
            // 吹胸膝脚（膝盖的风大）
            list.add(new WindDirectionItem(R.drawable.car_air_lfc, FrontWindDirectionCmdValue.S500_KNEE_MAX));
            // 吹胸膝脚（脚部的风大）
            list.add(new WindDirectionItem(R.drawable.car_air_fcl, FrontWindDirectionCmdValue.S500_FOOT_MAX));
        }
        return list;
    }
}
