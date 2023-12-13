package com.android.launcher.util;

import java.text.DecimalFormat;

/**
* @description:
* @createDate: 2023/9/3
*/
public class NumberUtils {

    /**
    * @description:
    * @createDate: 2023/9/3
     * @param retain 保留多少位小数
    */
    public static synchronized float floatRoundDTo(float value,int retain){
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0; i< retain; i++){
                stringBuilder.append("#");
            }
            DecimalFormat df = new DecimalFormat("#."+stringBuilder.toString());
            String formattedNumber = df.format(value);

            return Float.parseFloat(formattedNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static double roundToTwoDecimalPlaces(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(value));
    }
}
