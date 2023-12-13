package com.android.launcher.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;


/**
 * @name: com.qiaocat.app.widget
 * @describe 描述: LinearLayoutManager 分割线
 * @author: Baizhengfu
 * @date: 2018/1/15
 */
public class LinearDividerDecoration<T extends Drawable> extends RecyclerView.ItemDecoration {


    private T mDrawable;
    private int mHeight;

    /**
     * 左边距
     */
    private int mPaddingLeft;
    /**
     * 右边距
     */
    private int mPaddingRight;
    /**
     * 是否绘制最后一项
     */
    private boolean mDrawLastItem = false;

    /**
     * 绘制头部
     */
    private boolean drawHeader = false;
    /**
     * 绘制尾部
     */
    private boolean drawFoot = false;


    private BaseQuickAdapter adapter;

    /**
     * 方向
     */
    private int orientation;

//    public LinearDividerDecoration(Context context, BaseQuickAdapter adapter, T t) {
//        this.mDrawable = t;
//        this.mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
//        this.adapter = adapter;
//        orientation = LinearLayoutManager.VERTICAL;
//    }
//
//
//    public LinearDividerDecoration(BaseQuickAdapter adapter, T t, int height, int orientation) {
//        this.mDrawable = t;
//        this.mHeight = height;
//        this.adapter = adapter;
//        this.orientation = orientation;
//    }

    public LinearDividerDecoration(BaseQuickAdapter adapter, T t, int height) {
        this.mDrawable = t;
        this.mHeight = height;
        this.adapter = adapter;
        this.orientation = LinearLayoutManager.VERTICAL;
    }

    /**
     * @param adapter
     * @param t
     * @param height       单位：dp
     * @param paddingLeft  单位：dp
     * @param paddingRight 单位：dp
     * @param orientation  方向
     */
    public LinearDividerDecoration(BaseQuickAdapter adapter, T t, int height, int paddingLeft, int paddingRight, int orientation) {
        this.mDrawable = t;
        this.mHeight = height;
        this.adapter = adapter;
        this.mPaddingLeft = paddingLeft;
        this.mPaddingRight = paddingRight;
        this.orientation = orientation;
    }

    /**
     * @param adapter
     * @param t
     * @param height       单位：dp
     * @param paddingLeft  单位：dp
     * @param paddingRight 单位：dp
     */
    public LinearDividerDecoration(BaseQuickAdapter adapter, T t, int height, int paddingLeft, int paddingRight) {
        this.mDrawable = t;
        this.mHeight = height;
        this.adapter = adapter;
        this.mPaddingLeft = paddingLeft;
        this.mPaddingRight = paddingRight;
        this.orientation = LinearLayoutManager.VERTICAL;
    }


    public void setmPaddingLeft(int mPaddingLeft) {
        this.mPaddingLeft = mPaddingLeft;
    }

    public void setmPaddingRight(int mPaddingRight) {
        this.mPaddingRight = mPaddingRight;
    }

    public void setmDrawLastItem(boolean mDrawLastItem) {
        this.mDrawLastItem = mDrawLastItem;
    }

    public void setDrawHeader(boolean drawHeader) {
        this.drawHeader = drawHeader;
    }

    public void setDrawFoot(boolean drawFoot) {
        this.drawFoot = drawFoot;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (adapter.getData() == null || adapter.getData().size() <= 0) {
            return;
        }

        int headerCount = adapter.getHeaderLayoutCount();
        int footerCount = adapter.getFooterLayoutCount();
        int dataSize = adapter.getData().size();

        int position = parent.getChildAdapterPosition(view);

        if (orientation == LinearLayoutManager.VERTICAL) {
            if (isDrawHeader(position, headerCount)) {
                outRect.bottom = mHeight;
            } else if (isDrawItem(dataSize, headerCount, position)) {//正常设置item的偏移量
                outRect.bottom = mHeight;
            } else if (isDrawLastItem(dataSize, headerCount, position)) {
                outRect.bottom = mHeight;
            } else if (isDrawFooter(dataSize, position, headerCount, footerCount)) {
                outRect.bottom = mHeight;
            } else {
                outRect.bottom = 0;
            }
        } else {

        }
    }

    /**
     * 是否绘制尾部
     *
     * @param dataSize
     * @param position
     * @param headerCount
     * @param footerCount
     * @return
     */
    private boolean isDrawFooter(int dataSize, int position, int headerCount, int footerCount) {
        return dataSize > 0 && position >= (dataSize + headerCount) && drawFoot;
    }


    /**
     * 是否绘制item
     *
     * @param dataSize
     * @param headerCount
     * @param position
     * @return
     */
    private boolean isDrawItem(int dataSize, int headerCount, int position) {
        return dataSize > 0 && position >= headerCount && position < (headerCount + dataSize - 1);
    }

    /**
     * 是否绘制最后一项
     *
     * @param dataSize
     * @param headerCount
     * @param position
     * @return
     */
    private boolean isDrawLastItem(int dataSize, int headerCount, int position) {
        return dataSize > 0 && position == (headerCount + dataSize - 1) && mDrawLastItem;
    }


    /**
     * 是否绘制头部
     *
     * @param position
     * @param headerCount
     * @return
     */
    private boolean isDrawHeader(int position, int headerCount) {
        return position < headerCount && drawHeader;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (adapter.getData().size() <= 0) {
            return;
        }

        int headerCount = adapter.getHeaderLayoutCount();
        int footerCount = adapter.getFooterLayoutCount();
        int dataSize = adapter.getData().size();

//        LogUtils.i("bzf", "headerCount=" + headerCount + ",footerCount=" + footerCount + ",dataSize=" + dataSize);

        int count = dataSize + headerCount + footerCount;

        if (orientation == LinearLayoutManager.VERTICAL) {
            for (int i = 0; i < count; i++) {

                View childView = parent.getChildAt(i);

                if (childView != null) {
                    int position = parent.getChildAdapterPosition(childView);

                    if (isDrawHeader(position, headerCount)
                            || isDrawItem(dataSize, headerCount, position)
                            || isDrawLastItem(dataSize, headerCount, position)
                            || isDrawFooter(dataSize, position, headerCount, footerCount)) {

                        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) childView.getLayoutParams();

                        int left = childView.getLeft() + mPaddingLeft;
                        int top = childView.getBottom() + layoutParams.bottomMargin;
                        int right = childView.getRight() - mPaddingRight;
                        int bottom = top + mHeight;
                        mDrawable.setBounds(left, top, right, bottom);

                        mDrawable.draw(c);
                    }
                }

            }
        }
    }
}