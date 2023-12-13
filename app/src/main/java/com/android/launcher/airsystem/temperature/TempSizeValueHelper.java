package com.android.launcher.airsystem.temperature;

/**
 * @description:
 * @createDate: 2023/5/1
 */
public class TempSizeValueHelper {

//        int tipIcon[]=new int[]{
//            R.drawable.temp_icon_hi ,
//            R.drawable.temp_icon_28,
//            R.drawable.temp_icon_27,
//            R.drawable.temp_icon_26,
//            R.drawable.temp_icon_25,
//            R.drawable.temp_icon_24,
//            R.drawable.temp_icon_23,
//            R.drawable.temp_icon_22,
//            R.drawable.temp_icon_21,
//            R.drawable.temp_icon_20,
//            R.drawable.temp_icon_19,
//            R.drawable.temp_icon_18,
//            R.drawable.temp_icon_17,
//            R.drawable.temp_icon_16,
//            R.drawable.temp_icon_lo}

    public static String getValue(int index) {
        switch (index) {
            case 0:
                return TempSizeType.TEMP_HI;
            case 1:
                return TempSizeType.TEMP_28;
            case 2:
                return TempSizeType.TEMP_27;
            case 3:
                return TempSizeType.TEMP_26;
            case 4:
                return TempSizeType.TEMP_25;
            case 5:
                return TempSizeType.TEMP_24;
            case 6:
                return TempSizeType.TEMP_23;
            case 7:
                return TempSizeType.TEMP_22;
            case 8:
                return TempSizeType.TEMP_21;
            case 9:
                return TempSizeType.TEMP_20;
            case 10:
                return TempSizeType.TEMP_19;
            case 11:
                return TempSizeType.TEMP_18;
            case 12:
                return TempSizeType.TEMP_17;
            case 13:
                return TempSizeType.TEMP_16;
            case 14:
                return TempSizeType.TEMP_LO;
            default:
                return "";
        }
    }
}
