package com.android.launcher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.launcher.R;

import org.jetbrains.annotations.Nullable;


public class VerticalProgressBarView extends View {

    public float width;
    public float height;
    private Paint mpaint;
    public float progress;

    private Bitmap bitmap2;
    private Bitmap bitmap;
    private Bitmap bitmap3;

    private RectF oval3 = new RectF();
    private RectF oval4 = new RectF();

    public VerticalProgressBarView(Context context) {
        this(context, null);
    }

    public VerticalProgressBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mpaint = new Paint();
        mpaint.setColor(Color.RED);
        mpaint.setAntiAlias(true);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.e_b);
        bitmap2  = BitmapFactory.decodeResource(context.getResources(), R.drawable.e);
        bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.e_k);
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
        canvas.drawBitmap(bitmap2, null, oval3, mpaint);// 第二个參数是x半径。第三个參数是y半径
        canvas.restore();


        oval4.set(0, h - progress, w, h - 2);
        canvas.save();
        canvas.drawBitmap(bitmap, null, oval4, mpaint);// 第二个參数是x半径。第三个參数是y半径
        canvas.restore();

        canvas.save();
        canvas.drawBitmap(bitmap3, null, oval3, mpaint);// 第二个參数是x半径。第三个參数是y半径
        canvas.restore();
    }

    /**
     * 设置progressbar进度
     */
    public void setProgress(float progress) {
        Log.i("VerticalProgressBarView", "progress="+progress);
        this.progress = progress;
        postInvalidate();
    }
}
