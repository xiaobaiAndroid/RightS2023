package module.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
* @description: SharedPreferences工具类
* @createDate: 2023/4/30
*/
public class SPUtils {



    private static final String PREFERENCE_NAME = "benz_right";
    private SPUtils() {}

    public static final String SP_AIRFLOW_MAIN_DRIVER = "airflow_main_driver";
    public static final String SP_AIRFLOW_COPILOT = "airflow_copilot";

    //汽车类型 S500, S300 ...
    public static final String SP_CAR_TYPE = "car_type";

    //空调类型
    public static final String SP_CURRENT_AIR_TYPE = "current_air_type";

    //连接的蓝牙设备id
    public static final String SP_BLUETOOTH_DEVICEID = "bluetooth_device_id";

    //U盘插入状态
    public static final String USB_OTG_STATUS = "usb_otg_status";

    //警报音量
    public static final String SP_CAR_ALARM_VOLUME = "car_alarm_volume";

    public static final String SP_USB_PATH = "usb_path";

    //音乐音量
    public static final String SP_MUSIC_VOLUME = "music_volume";

    //选择的语言
    public static final String SP_SELECT_LANGUAGE = "select_language";


    //里程单位类型
    public static final String SP_UNIT_TYPE = "unit_type";


    //CD机选择的模式
    public static final String SP_SELECT_MODE = "cd_select_mode";

    //速度限制
    public static final String SPEED_LIMIT = "speed_limit";

    //当前的前台app
    public static final String SP_FOREGROUND_APP = "foreground_app";

    //当前activity
    public static final String SP_CURRENT_ACTIVITY = "current_activity";

    //USB音乐播放位置
    public static final String SP_USB_MUSIC_PLAY_POS = "usb_music_play_pos";


    /**
     * 获取SharedPreferences对象
     *
     * @param context 上下文
     * @return SharedPreferences对象
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    /**
     * 获取Editor对象
     *
     * @param context 上下文
     * @return Editor对象
     */
    public static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return editor;
    }


    /**
     * 将文本数据存入本地
     *
     * @param context 上下文
     * @param key     key值
     * @param value   存入本地的字符串
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 通过key获取SharedPreferences指定的字符串
     *
     * @param context 上下文
     * @param key     key值
     * @return 本地存储的字符串
     * @see #getString(Context, String, String)
     */
    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    /**
     * 通过key获取SharedPreferences指定的字符串
     *
     * @param context      上下文
     * @param key          key值
     * @param defaultValue 默认的返回值
     * @return 本地存储的字符串或默认值
     */
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * put int preferences
     *
     * @param context 上下文
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static void putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * get int preferences
     *
     * @param context 上下文
     * @param key     key值
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a int
     * @see #getInt(Context, String, int)
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    /**
     * get int preferences
     *
     * @param context      context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a int
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * put long preferences
     *
     * @param context 上下文
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static void putLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * get long preferences
     *
     * @param context 上下文
     * @param key     key值
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a long
     * @see #getLong(Context, String, long)
     */
    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    /**
     * get long preferences
     *
     * @param context      context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a long
     */
    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * put float preferences
     *
     * @param context 上下文
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * get float preferences
     *
     * @param context 上下文
     * @param key     key值
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a float
     * @see #getFloat(Context, String, float)
     */
    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }

    /**
     * get float preferences
     *
     * @param context      context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a float
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getFloat(key, defaultValue);
    }

    /**
     * put boolean preferences
     *
     * @param context 上下文
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        return editor.commit();
    }

    /**
     * get boolean preferences, default is false
     *
     * @param context 上下文
     * @param key     key值
     * @return The preference value if it exists, or false. Throws ClassCastException if there is a preference with this
     * name that is not a boolean
     * @see #getBoolean(Context, String, boolean)
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * get boolean preferences
     *
     * @param context      context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a boolean
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 清空SharedPreferences缓存
     * @param context 上下文
     */
    public static void clear(Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.clear();
        editor.apply();
    }

}

