package com.android.launcher.view;


import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * com.qiaocat.app.widget
 *
 * @author: Mr Bai
 * @date: 2018/1/15
 */

public class LinearSpaceDecoration extends RecyclerView.ItemDecoration {

    private int space;

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

    /*第一项左边距*/
    private int firstLeftPadding = 0;

    /*最后一项右边距*/
    private int lastRightPadding = 0;

    /**
     * 方向
     */
    private int orientation;

    public LinearSpaceDecoration(Context context, BaseQuickAdapter adapter) {
        this.space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
        this.adapter = adapter;
        orientation = LinearLayoutManager.VERTICAL;
    }


    public LinearSpaceDecoration(BaseQuickAdapter adapter, int space, int orientation) {
        this.space = space;
        this.adapter = adapter;
        this.orientation = orientation;
    }

    public LinearSpaceDecoration(BaseQuickAdapter adapter, int space) {
        this.space = space;
        this.adapter = adapter;
        this.orientation = LinearLayoutManager.VERTICAL;
    }

    /**
     * @param adapter
     * @param space        单位：dp
     * @param paddingLeft  单位：dp
     * @param paddingRight 单位：dp
     * @param orientation  方向
     */
    public LinearSpaceDecoration(BaseQuickAdapter adapter, int space, int paddingLeft, int paddingRight, int orientation) {
        this.space = space;
        this.adapter = adapter;
        this.mPaddingLeft = paddingLeft;
        this.mPaddingRight = paddingRight;
        this.orientation = orientation;
    }

    /**
     * @param adapter
     * @param space        单位：dp
     * @param paddingLeft  单位：dp
     * @param paddingRight 单位：dp
     */
    public LinearSpaceDecoration(BaseQuickAdapter adapter, int space, int paddingLeft, int paddingRight) {
        this.space = space;
        this.adapter = adapter;
        this.mPaddingLeft = paddingLeft;
        this.mPaddingRight = paddingRight;
        this.orientation = LinearLayoutManager.VERTICAL;
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
        int halfSpace = space / 2;

        if (orientation == LinearLayoutManager.VERTICAL) {
            if (isDrawHead(position, adapter, drawHeader)) {
                outRect.set(mPaddingRight, space, mPaddingRight, 0);

            } else if (isFirstItem(headerCount, position)) {
                outRect.set(mPaddingRight, space, mPaddingRight, halfSpace);
            } else if (isLastItem(dataSize, headerCount, position)) {
                outRect.set(mPaddingLeft, halfSpace, mPaddingRight, space);
            } else if (isNormalItem(dataSize, headerCount, position)) {
                outRect.set(mPaddingLeft, halfSpace, mPaddingRight, halfSpace);

            } else if (isDrawFooter(dataSize, headerCount, footerCount, position)) {
                outRect.set(mPaddingRight, 0, mPaddingRight, space);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        } else {
            if (isDrawHead(position, adapter, drawHeader)) {
                outRect.set(space, space, 0, space);

            } else if (isFirstItem(headerCount, position)) {
                if (firstLeftPadding != 0) {
                    outRect.set(firstLeftPadding, space, halfSpace, space);
                } else {
                    outRect.set(space, space, halfSpace, space);
                }
            } else if (isLastItem(dataSize, headerCount, position)) {

                if (lastRightPadding != 0) {
                    outRect.set(halfSpace, space, lastRightPadding, space);
                } else {
                    outRect.set(halfSpace, space, space, space);
                }
            } else if (isNormalItem(dataSize, headerCount, position)) {
                outRect.set(halfSpace, space, halfSpace, space);

            } else if (isDrawFooter(dataSize, headerCount, footerCount, position)) {
                outRect.set(0, space, space, space);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }
    }

    /**
     * 是否绘制尾部
     *
     * @param dataSize
     * @param headerCount
     * @param footerCount
     * @param position
     * @return
     */
    private boolean isDrawFooter(int dataSize, int headerCount, int footerCount, int position) {
        return position > (dataSize - 1 + headerCount) && position < (dataSize - 1 + headerCount + footerCount);
    }

    /**
     * 判断是否是正常的item
     *
     * @param dataSize
     * @param headerCount
     * @param position
     * @return
     */
    private boolean isNormalItem(int dataSize, int headerCount, int position) {
        return position > headerCount && position < (dataSize - 1 + headerCount);
    }

    /**
     * 判断是否是数据最后一项
     *
     * @param dataSize
     * @param headerCount
     * @param position
     * @return
     */
    private boolean isLastItem(int dataSize, int headerCount, int position) {
        return position == (dataSize - 1 + headerCount);
    }

    /**
     * 判断是否是数据第一项
     *
     * @param headerCount
     * @param position
     * @return
     */
    private boolean isFirstItem(int headerCount, int position) {
        return position == headerCount;
    }

    /**
     * 是否绘制尾部
     *
     * @param position
     * @param adapter
     * @param drawFooter
     * @return
     */
    private boolean isDrawFooter(int position, BaseQuickAdapter adapter, boolean drawFooter) {
        int headerLayoutCount = adapter.getHeaderLayoutCount();
        int footerLayoutCount = adapter.getFooterLayoutCount();

        return position >= (adapter.getData().size() + headerLayoutCount) && position < (adapter.getData().size() + headerLayoutCount + footerLayoutCount) && drawFooter;
    }

    /**
     * 是否绘制item
     *
     * @param position
     * @param adapter
     * @return
     */
    private boolean isDrawItem(int position, BaseQuickAdapter adapter) {
        int headerLayoutCount = adapter.getHeaderLayoutCount();
        return position >= headerLayoutCount && position < (adapter.getData().size() + headerLayoutCount);
    }

    /**
     * 是否绘制头部
     *
     * @param position
     * @param adapter
     * @param drawHeader
     * @return
     */
    private boolean isDrawHead(int position, BaseQuickAdapter adapter, boolean drawHeader) {
        int headerCount = adapter.getHeaderLayoutCount();
        return drawHeader && position < headerCount;
    }

    public void setFirstLeftPadding(int firstLeftPadding) {
        this.firstLeftPadding = firstLeftPadding;
    }

    public void setLastRightPadding(int lastRightPadding) {
        this.lastRightPadding = lastRightPadding;
    }
}