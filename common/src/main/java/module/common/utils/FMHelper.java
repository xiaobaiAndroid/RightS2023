package module.common.utils;

import android.content.Context;
import android.content.Intent;


/**
* @description: 收音机帮助类
* @createDate: 2023/4/28
*/
public class FMHelper {

    private static final String TAG = FMHelper.class.getSimpleName();

    public static void finishFM(Context context){
        try {
            Intent in = new Intent("xy.fmradio.quit");
            context.sendBroadcast(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.printI(TAG, "结束收音机");
    }

    public static void startFM(){
        LogUtils.printI(TAG, "打开收音机");
        new Thread(() -> ShellCommandUtils.execute(" am start --user 0 -n com.android.fmradio/.FmMainActivity")).start();

    }

    public static void up(Context context) {
        LogUtils.printI(TAG, "上一首");
        Intent fmintent = new Intent("com.car.fm.up");
        context.sendBroadcast(fmintent);
    }

    public static void down(Context context) {
        LogUtils.printI(TAG, "下一首");

        Intent fmintent = new Intent("com.car.fm.down");
        context.sendBroadcast(fmintent);
    }


    public static void killFMProcess() {
        try {
            ShellCommandUtils.execute(" am force-stop com.android.fmradio");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
