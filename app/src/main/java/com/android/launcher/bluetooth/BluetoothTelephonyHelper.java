package com.android.launcher.bluetooth;

import android.content.Context;
import android.content.Intent;

/**
* @description: 蓝牙电话
* @createDate: 2023/5/31
*/
public class BluetoothTelephonyHelper {

    private static final String PHONE_ACTION = "com.unisound.customer.bluetooth.telephony";

    /**
    * @description: 打电话
    * @createDate: 2023/5/31
    */
    public static void call(Context context, String phone){
        try {
            Intent mIntent = new Intent(PHONE_ACTION);
            mIntent.putExtra("behavior", "call");
            mIntent.putExtra("phone_number", phone);
            context.sendBroadcast(mIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * @description: 接电话
    * @createDate: 2023/5/31
    */
    public static void answer(Context context){
        try {
            Intent intent = new Intent(PHONE_ACTION);
            intent.putExtra("behavior", "answer");
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
    * @description: 挂断电话
    * @createDate: 2023/5/31
    */
    public static void handUp(Context context){
        try {
            Intent intent = new Intent(PHONE_ACTION);
            intent.putExtra("behavior", "hangup");
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 取消拨打电话 （不起作用）
     * @createDate: 2023/5/31
     */
    public static void cancel(Context context){
        try {
            Intent intent = new Intent(PHONE_ACTION);
            intent.putExtra("behavior", "cancel");
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
