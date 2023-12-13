package com.android.launcher.music;

import java.util.List;

/**
* @description:
* @createDate: 2023/6/9
*/
public class MusicListEntity {
    private List<CarMusicItem> list;

    private int playPosition = 0;

    public List<CarMusicItem> getList() {
        return list;
    }

    public void setList(List<CarMusicItem> list) {
        this.list = list;
    }

    public int getPlayPosition() {
        return playPosition;
    }

    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }
}
