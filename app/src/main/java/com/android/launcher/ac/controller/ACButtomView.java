package com.android.launcher.ac.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.android.launcher.R;
import module.common.utils.DensityUtil;

/**
 * 空调开关按键
 *
 * @date： 2023/10/13
 * @author: 78495
 */
public class ACButtomView extends View {

    private boolean selected = false;
    private TextPaint textPaint;
    private Paint paint;
    private String content;


    public ACButtomView(Context context) {
        this(context, null);
    }

    public ACButtomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ACButtomView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ACButtomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupInit(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ACButtomView);
        content = typedArray.getString(R.styleable.ACButtomView_text);
        typedArray.recycle();
    }

    private void setupInit(Context context) {
        textPaint = new TextPaint();
        paint = new Paint();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtil.sp2px(getContext(), 18));
        textPaint.setColor(Color.WHITE);


        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        float textWidth = textPaint.measureText(content);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();

        float textHeight = (Math.abs(fontMetrics.ascent) + fontMetrics.descent);

        canvas.drawText(content, centerX - textWidth / 2, centerY + textHeight / 4, textPaint);


        if (selected) {
            paint.setColor(Color.parseColor("#727375"));
        } else {
            paint.setColor(Color.parseColor("#BC0102"));
        }

        final int lineHeight = DensityUtil.dip2px(getContext(), 1f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(lineHeight);
        canvas.drawLine(lineHeight * 6, centerY + textHeight/1.5f, getWidth() - lineHeight * 6, centerY + textHeight/1.5f, paint);
        canvas.restore();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        invalidate();
    }
}
