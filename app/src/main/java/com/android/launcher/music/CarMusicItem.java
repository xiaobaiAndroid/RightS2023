package com.android.launcher.music;

/**
* @description:
* @createDate: 2023/6/9
*/
public class CarMusicItem {

    private String title;
    private String lyric;
    private String album;
    private String singer;

    private String path;

    private boolean isSelected;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "CarMusicItem{" +
                "title='" + title + '\'' +
                ", lyric='" + lyric + '\'' +
                ", album='" + album + '\'' +
                ", singer='" + singer + '\'' +
                ", path='" + path + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
