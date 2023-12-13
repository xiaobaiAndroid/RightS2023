package com.android.launcher.ac.winddirection.rear;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
* @description:
* @createDate: 2023/10/15
*/
public class RearWindDirectionItem implements MultiItemEntity {

    private int resId;

    private boolean selected;

    private RearWindDirectionCmdValue cmdValue;

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

    public RearWindDirectionItem(int resId, boolean selected) {
        this.resId = resId;
        this.selected = selected;
    }

    public RearWindDirectionItem(boolean selected, Type itemType) {
        this.selected = selected;
        this.itemType = itemType;
    }

    public RearWindDirectionItem(int resId, boolean selected, RearWindDirectionCmdValue cmdValue, Type itemType) {
        this.resId = resId;
        this.selected = selected;
        this.cmdValue = cmdValue;
        this.itemType = itemType;
    }

    public RearWindDirectionItem(boolean selected, RearWindDirectionCmdValue cmdValue, Type itemType) {
        this.selected = selected;
        this.cmdValue = cmdValue;
        this.itemType = itemType;
    }

    public RearWindDirectionCmdValue getCmdValue() {
        return cmdValue;
    }

    public void setCmdValue(RearWindDirectionCmdValue cmdValue) {
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
