package com.android.launcher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 播放进度
 * @date： 2023/10/16
 * @author: 78495
*/
public class PlayProgressView extends View {

    public PlayProgressView(Context context) {
        super(context);
    }

    public PlayProgressView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PlayProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public PlayProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
