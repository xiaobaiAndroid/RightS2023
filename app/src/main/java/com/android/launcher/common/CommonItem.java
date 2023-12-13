package com.android.launcher.common;

/**
 * @date： 2023/10/17
 * @author: 78495
*/
public class CommonItem {

    private String title;

    private boolean selected;

    private int resId;

    private boolean goneResId = false;

    public CommonItem(String title, boolean selected) {
        this.title = title;
        this.selected = selected;
    }


    public CommonItem(String title, boolean selected, boolean goneResId) {
        this.title = title;
        this.selected = selected;
        this.goneResId = goneResId;
    }

    public boolean isGoneResId() {
        return goneResId;
    }

    public void setGoneResId(boolean goneResId) {
        this.goneResId = goneResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
