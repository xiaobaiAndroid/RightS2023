package com.android.test;

public class ADBTest {

    private static final String TAG = ADBTest.class.getSimpleName();

    public static void test(){
        new Thread(() -> {

            //查看所有应用

//           String result = ShellCommandUtils.executeResult(" pm list packages");
//
//            LogUtils.printI(TAG,result);

            // 查看前台 Activity
//             result = ShellCommandUtils.executeResult(" dumpsys activity activities | grep mFocusedActivity");
//
//            LogUtils.printI(TAG,result);

//            result = ShellCommandUtils.executeResult("am start -a com.xyauto.Settings");

//            result = ShellCommandUtils.executeResult("am force-stop com.autonavi.amapauto");

//            LogUtils.printI(TAG,result);
        }).start();

    }
}
