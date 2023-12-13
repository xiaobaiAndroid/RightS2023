package com.android.launcher.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.launcher.R;
import module.common.utils.LogUtils;

/**
* @description:
* @createDate: 2023/6/13
*/
public class LoadingView extends FrameLayout {

    private ImageView loadingIV;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_view, this, true);
        loadingIV = view.findViewById(R.id.loadingIV);


    }

    public void destroy(){
        try {
            setVisibility(View.GONE);
            loadingIV.clearAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startLoadingView() {
        setVisibility(View.VISIBLE);
        try {
            loadingIV.clearAnimation();

            float size = getResources().getDimension(R.dimen.dp_40);
            int centerX = (int) (size / 2);
            int centerY = (int) (size / 2);

            LogUtils.printI(LoadingView.class.getSimpleName(), "centerX="+centerX + ", centerY="+centerY);

            RotateAnimation rotateAnimation = new RotateAnimation(0, 360, centerX, centerY);
            rotateAnimation.setRepeatMode(ValueAnimator.RESTART);
            rotateAnimation.setDuration(1000);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
            loadingIV.startAnimation(rotateAnimation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
