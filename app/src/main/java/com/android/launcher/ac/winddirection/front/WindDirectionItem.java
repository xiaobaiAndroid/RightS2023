package com.android.launcher.ac.winddirection.front;

import com.android.launcher.ac.winddirection.rear.RearWindDirectionCmdValue;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
* @description:
* @createDate: 2023/10/15
*/
public class WindDirectionItem implements MultiItemEntity {

    private int resId;

    private boolean selected;

    private FrontWindDirectionCmdValue cmdValue;

    private Type itemType = Type.NORMAL;


    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public WindDirectionItem(int resId, boolean selected) {
        this.resId = resId;
        this.selected = selected;
    }

    public WindDirectionItem(Type itemType,FrontWindDirectionCmdValue cmdValue) {
        this.cmdValue = cmdValue;
        this.itemType = itemType;
    }

    public WindDirectionItem(int resId, FrontWindDirectionCmdValue cmdValue) {
        this.resId = resId;
        this.cmdValue = cmdValue;
    }

    public FrontWindDirectionCmdValue getCmdValue() {
        return cmdValue;
    }

    public void setCmdValue(FrontWindDirectionCmdValue cmdValue) {
        this.cmdValue = cmdValue;
    }

    @Override
    public int getItemType() {
        return itemType.ordinal();
    }

    public enum Type{
        AUTO,
        NORMAL
    }
}
