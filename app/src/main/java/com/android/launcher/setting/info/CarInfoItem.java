package com.android.launcher.setting.info;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public class CarInfoItem {

    private String title;
    private String content;

    public CarInfoItem(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
