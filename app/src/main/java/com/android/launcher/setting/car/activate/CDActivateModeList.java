package com.android.launcher.setting.car.activate;

import com.android.launcher.util.FuncUtil;
import module.common.utils.LogUtils;
import com.android.launcher.util.SendcanCD;

/**
 * CD机的激活模式列表
 * @date： 2023/10/17
 * @author: 78495
*/
public class CDActivateModeList {

    private static final String TAG = CDActivateModeList.class.getSimpleName();


    public static void mode() {
        try {
            LogUtils.printI(TAG, "model-----");
            point();
            Thread.sleep(200);
            //
            SendcanCD.handler("AA000004000000FD0000400000000000");
            Thread.sleep(200);
            // 确定
            SendcanCD.handler("AA000004000000FD0080000000000000");

            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0080000000000000");
            FuncUtil.open1BB = false;
            Thread.sleep(200);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mode1() {
        // 基本步驟 三個返回 三個上五個左   （統一步驟）
        //兩次確認
        try {
            LogUtils.printI(TAG, "mode1-----");
            init1();
            // 兩次確認
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mode2() {
        // 基本步驟 三個返回 三個上五個左   （統一步驟）
        //步驟 確認  向下  確認
        try {
            LogUtils.printI(TAG, "mode2-----");
            init1();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void mode3() {
        // 基本步驟 三個返回 三個上五個左   （統一步驟）
        //步驟 確認  向下 兩次  確認
        try {
            LogUtils.printI(TAG, "mode3-----");
            init1();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void mode4() {
        // 基本步驟 三個返回 三個上五個左   （統一步驟）
        //步驟 確認  向下 三次  確認
        try {
            LogUtils.printI(TAG, "mode4-----");
            init1();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mode5() {
        // 基本步驟 三個返回 三個上五個左   （統一步驟）
        //步驟 確認  向下 四次  確認
        try {
            LogUtils.printI(TAG, "mode5-----");
            init1();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void mode6() {
        // 基本步驟 三個返回 三個上五個左   （統一步驟）
        //步驟 確認  向下 五次  確認
        try {
            LogUtils.printI(TAG, "mode6-----");
            init1();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mode7() {
        // 基本步驟 三個返回 三個上五個左   （統一步驟）
        //步驟 確認  向下 六次  確認
        try {
            LogUtils.printI(TAG, "mode7-----");
            init1();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void mode8() {
        // 基本步驟 三個返回 三個上 五個左 一個右  （統一步驟）
        //步驟 確認 確認
        try {
            LogUtils.printI(TAG, "mode8-----");
            init2();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
//            Thread.sleep(200);// 向下
//            SendcanCD.handler("AA000004000000FD0000100000000000");
//            Thread.sleep(200);// 向下
//            SendcanCD.handler("AA000004000000FD0000100000000000");
//            Thread.sleep(200);// 向下
//            SendcanCD.handler("AA000004000000FD0000100000000000");
//            Thread.sleep(200);// 向下
//            SendcanCD.handler("AA000004000000FD0000100000000000");
//            Thread.sleep(200);// 向下
//            SendcanCD.handler("AA000004000000FD0000100000000000");
//            Thread.sleep(200);// 向下
//            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mode9() {
        // 基本步驟 三個返回 三個上五個左  一個右  （統一步驟）
        //步驟 確認  向下 一次 確認
        try {
            LogUtils.printI(TAG, "mode9-----");

            init2();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");

            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mode10() {
        // 基本步驟 三個返回 三個上五個左  一個右  （統一步驟）
        //步驟 確認  向下 兩次 確認
        try {
            LogUtils.printI(TAG, "mode10-----");

            init2();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mode11() {
        // 基本步驟 三個返回 三個上五個左  一個右  （統一步驟）
        //步驟 確認  向下 三次 確認
        try {
            LogUtils.printI(TAG, "mode11-----");

            init2();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mode12() {
        // 基本步驟 三個返回 三個上五個左  一個右  （統一步驟）
        //步驟 確認  向下 四次 確認
        try {
            LogUtils.printI(TAG, "mode12-----");
            init2();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mode13() {
        // 基本步驟 三個返回 三個上五個左  一個右  （統一步驟）
        //步驟 確認  向下 五次 確認
        try {
            LogUtils.printI(TAG, "mode13-----");
            init2();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mode14() {
        // 基本步驟 三個返回 三個上五個左  一個右  （統一步驟）
        //步驟 確認  向下 六次 確認
        try {
            LogUtils.printI(TAG, "mode14-----");
            init2();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mode16() {
        try {
            LogUtils.printI(TAG, "mode16-----");
            init2();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void mode15() {
        try {
            LogUtils.printI(TAG, "mode15-----");
            init2();
            // 兩次確認
            Thread.sleep(200); //確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);// 向下
            SendcanCD.handler("AA000004000000FD0000100000000000");
            Thread.sleep(200);//確認
            SendcanCD.handler("AA000004000000FD0080000000000000");
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void init1() {
        try {
            // 三个返回
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0200000000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0200000000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0200000000000000");
            Thread.sleep(200);
            // 五个上
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);

            // 五个右
            SendcanCD.handler("AA000004000000FD0000400000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000400000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000400000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000400000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000400000000000");
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void init2() {
        try {
            // 三个返回
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0200000000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0200000000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0200000000000000");
            Thread.sleep(200);
            // 五个上
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);

            // 五个左
            SendcanCD.handler("AA000004000000FD0000400000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000400000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000400000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000400000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000400000000000");

            // 一個右
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000040000000000");
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public static void point() {

        try {
            // 三个返回
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0200000000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0200000000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0200000000000000");
            Thread.sleep(200);
            // 五个上
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000010000000000");
            Thread.sleep(200);

            // 五个右
            SendcanCD.handler("AA000004000000FD0000040000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000040000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000040000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000040000000000");
            Thread.sleep(200);
            SendcanCD.handler("AA000004000000FD0000040000000000");
//
            Thread.sleep(200);

            FuncUtil.pointOk = true;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
