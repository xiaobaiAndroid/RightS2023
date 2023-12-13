package module.assistant;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import module.common.utils.DensityUtil;


/**
 * 语音助手
 * @date： 2023/10/21
 * @author: 78495
*/
public class CarMICView extends FrameLayout{




    private static final String TAG = CarMICView.class.getSimpleName();


    public CarMICView(@NonNull Context context) {
        this(context,null);
    }

    public CarMICView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CarMICView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public CarMICView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupInit(context);
    }

    private void setupInit(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_mic,this,true);
    }



    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void destroy(){

    }

    /**
     * @description: 显示悬浮窗
     * @createDate: 2023/6/8
     */
    public static View showFloating(Context context) {
        try {
            WindowManager.LayoutParams params;
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            View floatingView = new CarMICView(context);
            floatingView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
            params.x = DensityUtil.dip2px(context,10);
            windowManager.addView(floatingView, params);

            return floatingView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 关闭悬浮窗
     * @createDate: 2023/6/8
     */
    public static void closeFloating(Context context, View view) {
        if (view != null) {
            try {
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                windowManager.removeView(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }






}
