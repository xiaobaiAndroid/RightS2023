package module.common.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕的宽度
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels; // 屏幕的宽度(像素)
        return screenWidth;
    }


    /**
     * 获取屏幕高度,不包含底部导航栏的高度
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        int realHeight = dm.heightPixels;
        return realHeight;
    }


    /**
     * 获取density
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }



    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getScreenRealHeight(Context context){
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi=displayMetrics.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
        }
        return dpi;
    }


    /**
     * sp转px
     *
     * @param context
     * @return
     */
    public static int sp2px(Context context, float spVal) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,

                spVal, context.getResources().getDisplayMetrics());

    }
}