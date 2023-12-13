package com.android.launcher.util;

import android.text.TextUtils;

import module.common.utils.LogUtils;

public class DataUtils {

    /**
     * 将 16 进制字符串转换为字节数组
     *
     * @param hexString 要转换的 16 进制字符串，例如 "01 02 03 0A"
     * @return 转换后得到的字节数组
     */
    public static synchronized byte[] hexStringToByteArray(String hexString) throws IllegalArgumentException {
        hexString = hexString.replaceAll("\\s+", ""); // 去掉字符串中多余的空格
        int len = hexString.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("hexString must have even number of characters");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * @description: 16进制转二进制
     * @createDate: 2023/7/24
     */
    public static synchronized String hexStringToBinaryString(String hexData) {
        try {
            if (TextUtils.isEmpty(hexData) || hexData.length() != 2) {
                return "";
            }
            int decimal = Integer.parseInt(hexData, 16);
            String data = Integer.toBinaryString(decimal);

            if (data.length() == 1) {
                data = "0000" + "000" + data;
            } else if (data.length() == 2) {
                data = "0000" + "00" + data;
            } else if (data.length() == 3) {
                data = "0000" + "0" + data;
            } else if (data.length() == 4) {
                data = "0000" + data;
            } else if (data.length() == 5) {
                data = "000" + data;
            } else if (data.length() == 6) {
                data = "00" + data;
            } else if (data.length() == 7) {
                data = "0" + data;
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @description: 二进制到16进制
     * @createDate: 2023/7/25
     */
    public static synchronized String binaryStringToHexString(String binary) {
        try {
            if (TextUtils.isEmpty(binary)) {
                return "";
            }
            int decimal = Integer.parseInt(binary, 2);
            return Integer.toHexString(decimal);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }
}
