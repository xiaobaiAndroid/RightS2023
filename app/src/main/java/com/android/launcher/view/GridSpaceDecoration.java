package com.android.launcher.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * GridLayout间距
 * com.qiaocat.app.widget
 * @author: Mr Bai
 * @date: 2018/1/11
 */

public class GridSpaceDecoration extends RecyclerView.ItemDecoration {



    private int space;

    private BaseQuickAdapter adapter;

    /**
     * 绘制头部
     */
    private boolean drawHeader = true;
    /**
     * 绘制尾部
     */
    private boolean drawFooter = true;

    /**
     * 最左边的item是否只绘制一半的space
     */
    private boolean leftDrawHalf = true;
    /**
     * 最右边的item是否只绘制一般的space
     */
    private boolean rightDrawHalf = true;


    private int margin  = 0;

    /**
     * 列数
     */
    private int spanCount = 2;

    /*最左和最后的距离*/
    private int leftmost = 0;
    private int rightmost = 0;
    private final int halfSpace;


    public GridSpaceDecoration(BaseQuickAdapter adapter, int space, int spanCount) {
        this.space = space;
        halfSpace = space / 2;
        this.adapter = adapter;
        this.spanCount = spanCount;
    }


    public void setDrawHeader(boolean drawHeader) {
        this.drawHeader = drawHeader;
    }

    public void setDrawFooter(boolean drawFooter) {
        this.drawFooter = drawFooter;
    }

    public void setLeftDrawHalf(boolean leftDrawHalf) {
        this.leftDrawHalf = leftDrawHalf;
    }

    public void setRightDrawHalf(boolean rightDrawHalf) {
        this.rightDrawHalf = rightDrawHalf;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(adapter.getData()==null || adapter.getData().size()<=0){
            return;
        }

        int position = parent.getChildAdapterPosition(view);

        if((position+1)>adapter.getHeaderLayoutCount()){
            if(isLeftmost(position)){
                if(margin==0){
                    outRect.set(halfSpace,halfSpace,halfSpace,halfSpace);
                }else{
                    outRect.set(margin,halfSpace,halfSpace,halfSpace);
                }
            }else if(isRightmost(position)){
                if(margin==0){
                    outRect.set(halfSpace,halfSpace,halfSpace,halfSpace);
                }else{
                    outRect.set(halfSpace,halfSpace,margin,halfSpace);
                }
            }else{
                outRect.set(halfSpace,halfSpace,halfSpace,halfSpace);
            }
        }
    }

    /**
     * @describe: 判断是否最左边
     * @date: 2019/8/19
     */
    private boolean isLeftmost(int position) {
        int headerLayoutCount = adapter.getHeaderLayoutCount();
        position   = position - headerLayoutCount;
        if(spanCount==2){
            return position%2==0;
        }
        return false;
    }

    /**
     * @describe: 判断是否最右边
     * @date: 2019/8/19
     * @author: Mr Bai
     */
    private boolean isRightmost(int position) {
        int headerLayoutCount = adapter.getHeaderLayoutCount();
        position   = position - headerLayoutCount;
        if(spanCount==2){
            return position%2==1;
        }
        return false;
    }


    public void setMargin(int margin) {
        this.margin = margin;
    }
}