package module.common.utils;

import android.text.TextUtils;

import java.util.HashMap;

/**
* @description:
* @createDate: 2023/4/29
*/
public class StringUtils {

    /**
     * 将16进制转换为二进制
     * @return
     */
    public static synchronized String hexString2binaryString(String hex) {
// variable to store the converted
        // Binary Sequence
        StringBuilder binary =  new StringBuilder();

        // converting the accepted Hexadecimal
        // string to upper case
        hex = hex.toUpperCase();

        // initializing the HashMap class
        HashMap<Character, String> hashMap
                = new HashMap<Character, String>();

        // storing the key value pairs
        hashMap.put('0', "0000");
        hashMap.put('1', "0001");
        hashMap.put('2', "0010");
        hashMap.put('3', "0011");
        hashMap.put('4', "0100");
        hashMap.put('5', "0101");
        hashMap.put('6', "0110");
        hashMap.put('7', "0111");
        hashMap.put('8', "1000");
        hashMap.put('9', "1001");
        hashMap.put('A', "1010");
        hashMap.put('B', "1011");
        hashMap.put('C', "1100");
        hashMap.put('D', "1101");
        hashMap.put('E', "1110");
        hashMap.put('F', "1111");

        int i;
        char[] arrChr = hex.toCharArray();
        char ch;

        for (i = 0; i < arrChr.length; i++) {
            // extracting each character
            ch = arrChr[i];

            if (hashMap.containsKey(ch)) {
                binary.append(hashMap.get(ch));
            }

        }

        // returning the converted Binary
        return binary.toString();

    }

    //判定是否为十六进制数
    public static synchronized boolean isHex(String hex) {
        int digit;
        try {	//字符串直接转换为数字，存在字符时会自动抛出异常
            digit = Integer.parseInt(hex);
        }catch (Exception e) {	//字符串存在字母时，捕获异常并转换为数字
            digit = hex.charAt(0) - 'A' + 10;
        }
        return 0 <= digit && digit <= 15;
    }

    /**
    * @description: 去除 null字符串
    * @createDate: 2023/5/29
    */
    public static String removeNull(String data){
        if(TextUtils.isEmpty(data)){
            return "";
        }
        if(data.toLowerCase().equals("null")){
            return "";
        }
        return data;
    }

    public static synchronized String stringToHex(String input) {
        // 将字符串转换为字节数组
        byte[] bytes = input.getBytes();

        // 创建一个 StringBuilder 用于存储转换后的16进制字符串
        StringBuilder sb = new StringBuilder();

        // 将每个字节转换为16进制并追加到 StringBuilder 中
        for (byte aByte : bytes) {
            sb.append(String.format("%02X", aByte));
        }

        // 返回最终的16进制字符串
        return sb.toString();
    }


}
