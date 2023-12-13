package com.android.launcher.ac.temp;

/**
 * 温度
 *
 * @date： 2023/10/31
 * @author: 78495
 */
public class TempUtils {

    //风速值转成指令值
    public static synchronized String tempToCmd(String temp) {
        if (TempDisplayValue.TEMP_LO.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_LO.getValue();
        } else if (TempDisplayValue.TEMP_16.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_16.getValue();
        }else if (TempDisplayValue.TEMP_17.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_17.getValue();
        }else if (TempDisplayValue.TEMP_18.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_18.getValue();
        }else if (TempDisplayValue.TEMP_19.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_19.getValue();
        }else if (TempDisplayValue.TEMP_20.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_20.getValue();
        }else if (TempDisplayValue.TEMP_21.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_21.getValue();
        }else if (TempDisplayValue.TEMP_22.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_22.getValue();
        }else if (TempDisplayValue.TEMP_23.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_23.getValue();
        }else if (TempDisplayValue.TEMP_24.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_24.getValue();
        }else if (TempDisplayValue.TEMP_25.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_25.getValue();
        }else if (TempDisplayValue.TEMP_26.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_26.getValue();
        }else if (TempDisplayValue.TEMP_27.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_27.getValue();
        }else if (TempDisplayValue.TEMP_28.getValue().equalsIgnoreCase(temp)) {
            return TempCmdValue.TEMP_28.getValue();
        }else{
            return TempCmdValue.TEMP_HI.getValue();
        }
    }

    //指令值转成风速值
    public static synchronized String cmdToTemp(String cmd) {
        if (TempCmdValue.TEMP_LO.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_LO.getValue();

        } else if (TempCmdValue.TEMP_16.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_16.getValue();

        } else if (TempCmdValue.TEMP_17.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_17.getValue();

        } else if (TempCmdValue.TEMP_18.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_18.getValue();

        } else if (TempCmdValue.TEMP_19.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_19.getValue();

        } else if (TempCmdValue.TEMP_20.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_20.getValue();

        } else if (TempCmdValue.TEMP_21.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_21.getValue();

        } else if (TempCmdValue.TEMP_22.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_22.getValue();

        } else if (TempCmdValue.TEMP_23.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_23.getValue();

        } else if (TempCmdValue.TEMP_24.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_24.getValue();

        }else if (TempCmdValue.TEMP_25.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_25.getValue();

        }else if (TempCmdValue.TEMP_26.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_26.getValue();

        }else if (TempCmdValue.TEMP_27.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_27.getValue();

        }else if (TempCmdValue.TEMP_28.getValue().equalsIgnoreCase(cmd)) {
            return TempDisplayValue.TEMP_28.getValue();

        }else{
            return TempDisplayValue.TEMP_HI.getValue();
        }
    }
}
