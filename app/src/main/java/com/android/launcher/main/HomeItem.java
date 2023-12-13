package com.android.launcher.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @date： 2023/10/12
 * @author: 78495
*/
public class HomeItem implements Parcelable {

    private Type type;
    private int resId;
    private String name;
    private  boolean selected;



    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public HomeItem(Type type, int resId, String name, boolean selected) {
        this.type = type;
        this.resId = resId;
        this.name = name;
        this.selected = selected;
    }


    public enum Type{
        MAP,
        MEDIA,
        FM,
        PHONE,
        //第三方APP
        APPS,
        SETTING,
        INFO,
        //舒适性
        COMFORT
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeInt(this.resId);
        dest.writeString(this.name);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected HomeItem(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
        this.resId = in.readInt();
        this.name = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<HomeItem> CREATOR = new Parcelable.Creator<HomeItem>() {
        @Override
        public HomeItem createFromParcel(Parcel source) {
            return new HomeItem(source);
        }

        @Override
        public HomeItem[] newArray(int size) {
            return new HomeItem[size];
        }
    };
}
