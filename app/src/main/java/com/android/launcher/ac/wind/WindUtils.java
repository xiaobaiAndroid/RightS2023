package com.android.launcher.ac.wind;

import android.text.TextUtils;

import com.android.launcher.util.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 风速
 *
 * @date： 2023/10/31
 * @author: 78495
 */
public class WindUtils {

    //风速值转成指令值
    public static synchronized String numberToCmd(int number) {
        if (number == 1) {
            return WindCmdValue.WIND1.getValue();
        } else if (number == 2) {
            return WindCmdValue.WIND2.getValue();
        }else if (number == 3) {
            return WindCmdValue.WIND3.getValue();
        }else if (number == 4) {
            return WindCmdValue.WIND4.getValue();
        }else if (number == 5) {
            return WindCmdValue.WIND5.getValue();
        }else if (number == 6) {
            return WindCmdValue.WIND6.getValue();
        }else{
            return WindCmdValue.WIND7.getValue();
        }
    }

    //指令值转成风速值
    public static synchronized int cmdToNumber(String cmd) {
        if (WindCmdValue.WIND1.getValue().equalsIgnoreCase(cmd)) {
            return 1;
        } else if (WindCmdValue.WIND2.getValue().equalsIgnoreCase(cmd)) {
            return 2;
        }else if (WindCmdValue.WIND3.getValue().equalsIgnoreCase(cmd)) {
            return 3;
        }else if (WindCmdValue.WIND4.getValue().equalsIgnoreCase(cmd)) {
            return 4;
        }else if (WindCmdValue.WIND5.getValue().equalsIgnoreCase(cmd)) {
            return 5;
        }else if (WindCmdValue.WIND6.getValue().equalsIgnoreCase(cmd)) {
            return 6;
        }else{
            return 7;
        }

    }

    //风速值矫正，风速在20B中读取到的值不一定是表格中的值，在增大时，选择最近的高一档。减小时，选择最近的低一档。
    public static String rectify(String cmd) {
        if(TextUtils.isEmpty(cmd)){
            return null;
        }
        int value = Integer.parseInt(cmd, 16);
        int wind1 = Integer.parseInt(WindCmdValue.WIND1.getValue(), 16);
        int wind2 = Integer.parseInt(WindCmdValue.WIND2.getValue(), 16);
        int wind3 = Integer.parseInt(WindCmdValue.WIND3.getValue(), 16);
        int wind4 = Integer.parseInt(WindCmdValue.WIND4.getValue(), 16);
        int wind5 = Integer.parseInt(WindCmdValue.WIND5.getValue(), 16);
        int wind6 = Integer.parseInt(WindCmdValue.WIND6.getValue(), 16);
        int wind7 = Integer.parseInt(WindCmdValue.WIND7.getValue(), 16);

        List<Integer> list = new ArrayList<>();
        list.add(wind1);
        list.add(wind2);
        list.add(wind3);
        list.add(wind4);
        list.add(wind5);
        list.add(wind6);
        list.add(wind7);

        int closestValue = list.get(0); // 假设列表中的第一个元素是最接近的初始值
        int minDifference = Math.abs(value - closestValue); // 初始化最小差值为与第一个元素的差值

        for (int i = 1; i < list.size(); i++) {
            int currentValue = list.get(i);
            int difference = Math.abs(value - currentValue);

            if (difference < minDifference) {
                minDifference = difference;
                closestValue = currentValue;
            }
        }

        return Integer.toHexString(closestValue);
    }
}
