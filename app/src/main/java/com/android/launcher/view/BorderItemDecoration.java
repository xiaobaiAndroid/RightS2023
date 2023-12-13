package com.android.launcher.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
* @description:
* @createDate: 2023/5/30
*/
public class BorderItemDecoration extends RecyclerView.ItemDecoration {
    private final int borderWidth;
    private final int borderColor;

    public BorderItemDecoration(int borderWidth, int borderColor) {
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(borderWidth, borderWidth, borderWidth, borderWidth);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {

    }
}