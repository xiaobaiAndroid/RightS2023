package module.common.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 参考：https://blog.csdn.net/weixin_39680609/article/details/111808733
* @description: 高德地图车机版 广播帮助类
* @createDate: 2023/4/28
*/
public class GaodeCarMapHelper {

    private static final String AMAP_RECEIVE_ACTION = "AUTONAVI_STANDARD_BROADCAST_RECV";

    public static void start(Context context){
        try {
            Intent intent1 = new Intent();
            intent1.setAction(AMAP_RECEIVE_ACTION);
            intent1.putExtra("KEY_TYPE", 10034);
            intent1.putExtra("SOURCE_APP","Third App");
            context.sendBroadcast(intent1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void finish(Context context){
        Log.i("bzf","finish---Gaode");
        try {
            Intent intent = new Intent();
            intent.setAction(AMAP_RECEIVE_ACTION);
//        intent.putExtra("KEY_TYPE", 10010);
            intent.putExtra("KEY_TYPE", 10021);
            intent.putExtra("SOURCE_APP","Third App");
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * @description: 设置昼夜模式
    * @createDate: 2023/4/30
     * @param type: 0:自动; 1：白天; 2：黑夜
    */
    public static void setDayAndNight(Context context, int type){
        Intent intent = new Intent();
        intent.setAction(AMAP_RECEIVE_ACTION);
        intent.putExtra("KEY_TYPE", 10048);
        intent.putExtra("EXTRA_DAY_NIGHT_MODE", type);
        context.sendBroadcast(intent);
    }

    /**
    * @description: 电子眼设置
    * @createDate: 2023/4/30
     * @param type: 0 : 所有 ；1: 路况播报；2: 电子眼播报； 3: 警示播报；
     * @param isOpenPlay:  0 : 打开 ；1: 关闭 ；
    */
    public static void setElectronicEye(Context context, int type, int isOpenPlay){
        Intent intent = new Intent();
        intent.setAction(AMAP_RECEIVE_ACTION);
        intent.putExtra("KEY_TYPE",10064);
        intent.putExtra("EXTRA_TYPE", type);
        intent.putExtra("EXTRA_OPERA", isOpenPlay);
        context.sendBroadcast(intent);
    }

    public static void search(Context context, String key) {
        Intent intent1 = new Intent();
        intent1.setAction(AMAP_RECEIVE_ACTION);
        intent1.putExtra("KEY_TYPE", 10036);
        intent1.putExtra("KEYWORDS", key);
        intent1.putExtra("SOURCE_APP", "aaa");
        context.sendBroadcast(intent1);
    }

    /**
    * @description: 选择搜索地址
    * @createDate: 2023/5/2
    */
    public static void selectSearchAddress(Context context, int index) {
        LogUtils.printI(GaodeCarMapHelper.class.getName(), "selectSearchAddress---index="+index);
        Intent intent = new Intent();
        intent.setAction(AMAP_RECEIVE_ACTION);
        intent.putExtra("KEY_TYPE", 12201);
        intent.putExtra("EXTRA_CHOICE", index);
        context.sendBroadcast(intent);

    }

    /**
     * @description: 选择线路
     * @createDate: 2023/5/2
     */
    public static void selectLineWay(Context context, int index) {
        Intent intent = new Intent();
        intent.setAction(AMAP_RECEIVE_ACTION);
        intent.putExtra("KEY_TYPE", 10055);
        intent.putExtra("EXTRA_CHANGE_ROAD", index);
        context.sendBroadcast(intent);
    }

    /**
    * @description: 切换到后台，最小化
    * @createDate: 2023/5/4
    */
    public static void toBackstage(Context context){
        Intent intent = new Intent();
        intent.setAction(AMAP_RECEIVE_ACTION);
        intent.putExtra("KEY_TYPE", 10031);
        context.sendBroadcast(intent);
    }

    /**
     * @description: 缩放地图
     * @createDate: 2023/5/4
     * @param type 0 放大 1 缩小  无效
     */
    public static void zoom(Context context, int type){
        Intent intent = new Intent("AUTONAVI_STANDARD_BROADCAST_SEND");
        intent.putExtra("KEY_TYPE",10074);
        intent.putExtra("EXTRA_AUTO_ZOOM_TYPE",type);
        intent.putExtra("EXTRA_AUTO_CAN_ZOOM",false);
        context.sendBroadcast(intent);
    }
}
