package com.android.launcher.ac.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.android.launcher.R;
import com.android.launcher.ac.temp.TempDisplayValue;

import module.common.utils.DensityUtil;
import module.common.utils.LogUtils;

/**
 * 空调调节的View
 *
 * @date： 2023/10/12
 * @author: 78495
 */
public class ACAdjustView extends View {

    private Paint mPaint;
    private TextPaint mTextPaint;

    private int strokeWidth;

    //中间的信息区域
    private RectF infoRect = new RectF();
    //温度降低触控区域
    private RectF reduceTempRect = new RectF();
    //温度增加触控区域
    private RectF increaseTempRect = new RectF();


    //风量降低触控区域
    private RectF reduceWindRect = new RectF();
    //温度增加触控区域
    private RectF increaseWindRect = new RectF();

    private String closeText;

    //温度 Lo, 16-28, Hi
    private String temp = "16";
    //风量 1-7
    private int wind = 7;

//    private Bitmap windDrawable;
//    private Bitmap tempDrawable;

    private GestureDetector gestureDetector;

    private TouchListener touchListener;

    private boolean accOff = false;

    //是否是自动
    private boolean auto = false;
    private float windTextSize;


    public ACAdjustView(Context context) {
        this(context, null);
    }

    public ACAdjustView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ACAdjustView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInit(context);
    }

    private void setInit(Context context) {
        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        strokeWidth = DensityUtil.dip2px(context, 0.5f);
        windTextSize = context.getResources().getDimension(R.dimen.wind_size);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(context.getResources().getColor(R.color.cl_cccccc));
        mPaint.setStrokeWidth(strokeWidth);

        mTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStrokeWidth(strokeWidth);
        mTextPaint.setTextSize(DensityUtil.dip2px(context, 28));

//        windDrawable = BitmapFactory.decodeResource(getResources(), R.drawable.ic_ac_wind_adjust);
//        tempDrawable = BitmapFactory.decodeResource(getResources(), R.drawable.ic_ac_temp_adjust);

        closeText = context.getResources().getString(R.string.close);

        gestureDetector = new GestureDetector(context, gestureListener);

    }

    public void setTouchListener(TouchListener touchListener) {
        this.touchListener = touchListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (infoRect.width() == 0) {
            setupRect();
        }

//        drawACBg(canvas);
//        drawLine(canvas);

        if (accOff) {
            drawClose(canvas);
        } else {
            if (!auto) {
//                drawIcon(canvas);
                drawTempText(canvas);
                drawWindText(canvas);
            }else{
                drawAutoText(canvas);
            }
        }

//        canvas.drawRect(infoRect, mPaint);
//        canvas.drawRect(reduceWindRect, mPaint);
//        canvas.drawRect(increaseWindRect, mPaint);
//        canvas.drawRect(reduceTempRect, mPaint);
//        canvas.drawRect(increaseTempRect, mPaint);
    }

    private void drawAutoText(Canvas canvas) {
        canvas.save();

        mTextPaint.setTextSize(windTextSize);
        float textWidth = mTextPaint.measureText("Auto");
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        canvas.translate(getWidth() / 2 - textWidth / 2, getHeight() / 2 + (Math.abs(fontMetrics.ascent) + fontMetrics.descent) / 4);
        canvas.drawText("Auto", 0, 0, mTextPaint);

        canvas.restore();
    }

    private void drawWindText(Canvas canvas) {
        canvas.save();
        mTextPaint.setTextSize(windTextSize);
        final String windText = String.valueOf(wind);

        float textWidth = mTextPaint.measureText(windText);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        canvas.translate(getWidth() / 2 - textWidth / 2, getHeight() / 2 + (Math.abs(fontMetrics.ascent) + fontMetrics.descent) + DensityUtil.dip2px(getContext(), 6));
        canvas.drawText(windText, 0, 0, mTextPaint);

        canvas.restore();

    }

    private void drawTempText(Canvas canvas) {
        canvas.save();
        mTextPaint.setTextSize(windTextSize);

        String tempText;
        if(temp.equalsIgnoreCase(TempDisplayValue.TEMP_LO.getValue()) || temp.equalsIgnoreCase(TempDisplayValue.TEMP_HI.getValue())){
            tempText = temp;
        }else{
            tempText = temp + "℃";
        }

        float textWidth = mTextPaint.measureText(tempText);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        canvas.translate(getWidth() / 2 - textWidth / 2, getHeight() / 2 - DensityUtil.dip2px(getContext(), 6));
        canvas.drawText(tempText, 0, 0, mTextPaint);

        canvas.restore();
    }

    private void drawClose(Canvas canvas) {
        canvas.save();

        mTextPaint.setTextSize(windTextSize);
        float textWidth = mTextPaint.measureText(closeText);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        canvas.translate(getWidth() / 2 - textWidth / 2, getHeight() / 2 + (Math.abs(fontMetrics.ascent) + fontMetrics.descent) / 4);
        canvas.drawText(closeText, 0, 0, mTextPaint);

        canvas.restore();

//        canvas.drawLine(0,getWidth()/2, getWidth(),getWidth()/2,mPaint);
    }

//    private void drawIcon(Canvas canvas) {
//        drawReduceWindIcon(canvas);
//        drawIncreaseWindIcon(canvas);
//
//        drawReduceTempIcon(canvas);
//        drawIncreaseTempIcon(canvas);
//    }

//    private void drawReduceTempIcon(Canvas canvas) {
//        canvas.save();
//
//        Matrix matrix = new Matrix();
//
//        int viewRadius = getWidth() / 2;
//        int innerCircleRadius = getWidth() / 4;
//        int width = windDrawable.getWidth();
//        int height = windDrawable.getHeight();
//
//        matrix.postTranslate(viewRadius - width / 2, innerCircleRadius / 2 - height / 2);
//
//        canvas.drawBitmap(tempDrawable, matrix, null);
//
//        canvas.restore();
//    }

//    private void drawIncreaseTempIcon(Canvas canvas) {
//        canvas.save();
//
//        Matrix matrix = new Matrix();
//
//        int viewRadius = getWidth() / 2;
//        int innerCircleRadius = getWidth() / 4;
//        int width = windDrawable.getWidth();
//        int height = windDrawable.getHeight();
//
//        matrix.postTranslate(viewRadius - width / 2, viewRadius + innerCircleRadius + innerCircleRadius / 2 - height / 2);
//        matrix.postRotate(180, viewRadius, viewRadius + innerCircleRadius + innerCircleRadius / 2);
//        canvas.drawBitmap(tempDrawable, matrix, null);
//
//        canvas.restore();
//    }

//    private void drawIncreaseWindIcon(Canvas canvas) {
//        canvas.save();
//
//        Matrix matrix = new Matrix();
//
//        int viewRadius = getWidth() / 2;
//        int innerCircleRadius = getWidth() / 4;
//        int width = windDrawable.getWidth();
//        int height = windDrawable.getHeight();
//
//        final float factor = 1.0f;
//        width = (int) (width * factor);
//        height = (int) (height * factor);
//        matrix.postScale(factor, factor);
//
//        matrix.postTranslate(viewRadius + innerCircleRadius + innerCircleRadius / 2 - width / 2 - strokeWidth, viewRadius - height / 2);
//
//        canvas.drawBitmap(windDrawable, matrix, null);
//
//        canvas.restore();
//    }
//
//    private void drawReduceWindIcon(Canvas canvas) {
//        canvas.save();
//
//        Matrix matrix = new Matrix();
//
//        int centerY = getWidth() / 2;
//        int centerX = getWidth() / 4;
//        int width = windDrawable.getWidth();
//        int height = windDrawable.getHeight();
//
//        final float factor = 0.8f;
//        width = (int) (width * factor);
//        height = (int) (height * factor);
//        matrix.postScale(factor, factor);
//
//        matrix.postTranslate(centerX / 2 - width / 2 + strokeWidth, centerY - height / 2);
//
//        canvas.drawBitmap(windDrawable, matrix, null);
//        canvas.restore();
//    }

    private void setupRect() {
        int centerX = getWidth() / 2;

        int padding = DensityUtil.dip2px(getContext(), 6);

        int radius = getWidth() / 4;
        float cos = (float) (radius * Math.cos(Math.toRadians(45f)));
        infoRect.set(centerX - cos, centerX - cos, centerX + cos, centerX + cos);

        reduceWindRect.set(padding, centerX - cos - padding, radius, centerX + cos + padding);

        increaseWindRect.set(centerX + radius, centerX - cos - padding, getWidth() - padding, centerX + cos + padding);

        increaseTempRect.set(centerX - cos - padding, padding, centerX + cos + padding, radius);
        reduceTempRect.set(centerX - cos - padding, centerX + radius, centerX + cos + padding, getHeight() - padding);
    }

    private void drawLine(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        int startX = getWidth() / 4 - strokeWidth;
        int startY = 0;

        int endX = getWidth() / 2 - strokeWidth * 2;
        int endY = 0;


        mPaint.setColor(getResources().getColor(R.color.cl_ffffff));

        canvas.rotate(45);
        canvas.drawLine(startX, startY, endX, endY, mPaint);

        canvas.rotate(90);
        canvas.drawLine(startX, startY, endX, endY, mPaint);

        canvas.rotate(90);
        canvas.drawLine(startX, startY, endX, endY, mPaint);

        canvas.rotate(90);
        canvas.drawLine(startX, startY, endX, endY, mPaint);
        canvas.restore();
    }

    private void drawACBg(Canvas canvas) {
        canvas.save();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int externalRadius = centerX - strokeWidth * 2;
        mPaint.setColor(getResources().getColor(R.color.cl_cccccc));
//        canvas.drawCircle(centerX, centerY, externalRadius, mPaint);

        mPaint.setColor(getResources().getColor(R.color.cl_ffffff));

        int innerRadius = centerX / 2 - strokeWidth * 2;
        canvas.drawCircle(centerX, centerY, innerRadius, mPaint);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            LogUtils.printI(ACAdjustView.class.getSimpleName(), "onDown---");

            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            LogUtils.printI(ACAdjustView.class.getSimpleName(), "onShowPress---");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (auto) {
                return true;
            }
            LogUtils.printI(ACAdjustView.class.getSimpleName(), "onSingleTapUp---");

            if (isClickReduceWind(e)) {

                if (wind > 1) {
                    wind--;
                    invalidate();
                    if (touchListener != null) {
                        touchListener.onWindReduce(wind);
                    }
                }

            } else if (isClickIncreaseWind(e)) {
                if (wind < 7) {
                    wind++;
                    invalidate();
                    if (touchListener != null) {
                        touchListener.onWindIncrease(wind);
                    }
                }

            } else if (isClickReduceTemp(e)) {
                if(!TempDisplayValue.TEMP_LO.getValue().equals(temp)) {
                    disposeTempReduce();
                }
            } else if (isClickIncreaseTemp(e)) {
                if(!TempDisplayValue.TEMP_HI.getValue().equals(temp)) {
                    disposeTempIncrease();
                }
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            LogUtils.printI(ACAdjustView.class.getSimpleName(), "onLongPress---");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LogUtils.printI(ACAdjustView.class.getSimpleName(), "onFling---");
            return false;
        }

        /**
         * 点击了温度增加
         * @date： 2023/10/13
         * @author: 78495
         */
        private boolean isClickIncreaseTemp(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            return increaseTempRect.contains(x, y);
        }

        /**
         * 点击了温度减少
         * @date： 2023/10/13
         * @author: 78495
         */
        private boolean isClickReduceTemp(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            if (reduceTempRect.contains(x, y)) {
                return true;
            }
            return false;
        }

        /**
         * 点击了风量增大
         * @date： 2023/10/13
         * @author: 78495
         */
        private boolean isClickIncreaseWind(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            if (increaseWindRect.contains(x, y)) {
                return true;
            }
            return false;
        }

        /**
         * 点击了风量减少
         * @date： 2023/10/13
         * @author: 78495
         */
        private boolean isClickReduceWind(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            return reduceWindRect.contains(x, y);
        }
    };

    private void disposeTempIncrease() {
        if(TempDisplayValue.TEMP_LO.getValue().equalsIgnoreCase(temp)){
            temp = TempDisplayValue.TEMP_16.getValue();
        }else if(TempDisplayValue.TEMP_16.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_17.getValue();
        }else if(TempDisplayValue.TEMP_17.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_18.getValue();
        }else if(TempDisplayValue.TEMP_18.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_19.getValue();
        }else if(TempDisplayValue.TEMP_19.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_20.getValue();
        }else if(TempDisplayValue.TEMP_20.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_21.getValue();
        }else if(TempDisplayValue.TEMP_21.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_22.getValue();
        }else if(TempDisplayValue.TEMP_22.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_23.getValue();
        }else if(TempDisplayValue.TEMP_23.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_24.getValue();
        }else if(TempDisplayValue.TEMP_24.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_25.getValue();
        }else if(TempDisplayValue.TEMP_25.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_26.getValue();
        }else if(TempDisplayValue.TEMP_26.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_27.getValue();
        }else if(TempDisplayValue.TEMP_27.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_28.getValue();
        }else if(TempDisplayValue.TEMP_28.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_HI.getValue();
        }
        invalidate();
        if (touchListener != null) {
            touchListener.onTempIncrease(temp);
        }
    }

    private void disposeTempReduce() {
        if(TempDisplayValue.TEMP_16.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_LO.getValue();
        }else if(TempDisplayValue.TEMP_17.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_16.getValue();
        }else if(TempDisplayValue.TEMP_18.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_17.getValue();
        }else if(TempDisplayValue.TEMP_19.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_18.getValue();
        }else if(TempDisplayValue.TEMP_20.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_19.getValue();
        }else if(TempDisplayValue.TEMP_21.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_20.getValue();
        }else if(TempDisplayValue.TEMP_22.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_21.getValue();
        }else if(TempDisplayValue.TEMP_23.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_22.getValue();
        }else if(TempDisplayValue.TEMP_24.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_23.getValue();
        }else if(TempDisplayValue.TEMP_25.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_24.getValue();
        }else if(TempDisplayValue.TEMP_26.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_25.getValue();
        }else if(TempDisplayValue.TEMP_27.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_26.getValue();
        }else if(TempDisplayValue.TEMP_28.getValue().equals(temp)){
            temp = TempDisplayValue.TEMP_27.getValue();
        }else if(TempDisplayValue.TEMP_HI.getValue().equalsIgnoreCase(temp)){
            temp = TempDisplayValue.TEMP_28.getValue();
        }
        invalidate();
        if (touchListener != null) {
            touchListener.onTempReduce(temp);
        }
    }


    public void setAccOff(boolean accOff) {
        this.accOff = accOff;
        invalidate();
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
        invalidate();
    }

    public void updateData(String temp, String wind) {
        try {
            this.temp = temp;
            this.wind = Integer.parseInt(wind);
            invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTemp(String temp) {
        this.temp = temp;
        invalidate();
    }

    public void setWind(int wind) {
        this.wind = wind;
        invalidate();
    }

    public String getTemp() {
        return temp;
    }

    public int getWind() {
        return wind;
    }

    public interface TouchListener {

        //风速增大
        void onWindIncrease(int wind);

        //风速减小
        void onWindReduce(int wind);

        //温度增大
        void onTempIncrease(String temp);

        //温度减小
        void onTempReduce(String temp);
    }
}
