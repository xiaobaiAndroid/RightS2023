package com.android.launcher.vo;


/**
 * 选项
 */
public class ItemContentVo {

    public String name ;
    public int image ;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ItemContent{" +
                "name='" + name + '\'' +
                ", image=" + image +
                '}';
    }
}
