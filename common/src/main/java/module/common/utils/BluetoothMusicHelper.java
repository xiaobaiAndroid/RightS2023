package module.common.utils;

import android.content.Context;
import android.content.Intent;

/**
* @description: 蓝牙音乐播放器
* @createDate: 2023/5/27
*/
public class BluetoothMusicHelper {

    public static void play(Context context){
        try {
            Intent intent = new Intent("xy.android.gtpkey.play");
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pause(Context context){
        try {
            Intent intent = new Intent("xy.android.gtpkey.ic_music_pause");
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * @description: 下一首
    * @createDate: 2023/5/27
    */
    public static void next(Context context){
        try {
            Intent intent = new Intent("xy.android.gtpkey.frd");
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * @description: 上一首
    * @createDate: 2023/5/27
    */
    public static void previous(Context context){
        try {
            Intent intent = new Intent("xy.android.gtpkey.rev");
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
