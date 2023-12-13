package com.android.launcher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.android.launcher.R;

import org.jetbrains.annotations.Nullable;

//能源View
public class VerticalEnergyView extends View {

    public float width;
    public float height;
    private Paint mpaint;
    public float progress;
    private  RectF oval3 = new RectF();
    private  Bitmap bitmap2;
    private  Bitmap bitmap;
    private  RectF oval4 = new RectF();

    public VerticalEnergyView(Context context) {
        this(context, null);
    }

    public VerticalEnergyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalEnergyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mpaint = new Paint();
        mpaint.setColor(Color.RED);
        mpaint.setAntiAlias(true);

        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.right_car_info_dym_energydata_kedu);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.e_b);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

//            Log.i("AAAAaA",w+"=============="+h) ;

        oval3.set(0, 0,
                w, h);

        canvas.save();
        if(bitmap2 != null){
            canvas.drawBitmap(bitmap2, null, oval3, mpaint);// 第二个參数是x半径。第三个參数是y半径
        }
        canvas.restore();


        oval4.set(0, h - progress, w, h);
        canvas.save();
        if(bitmap != null){
            canvas.drawBitmap(bitmap, null, oval4, mpaint);// 第二个參数是x半径。第三个參数是y半径
        }
        canvas.restore();
    }

    /**
     * 设置progressbar进度
     */
    public void setProgress(float progress) {
        this.progress = progress;
        postInvalidate();
    }
}
