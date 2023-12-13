package com.bzf.module_db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import com.bzf.module_db.DBTableNames;

/**
 * @date： 2023/11/16
 * @author: 78495
*/
@Entity(tableName = DBTableNames.TABLE_NAME_MUSIC)
public class MusicTable {

    @PrimaryKey
    @NonNull
    private String musicId;

    private String deviceId = "";

    //u盘的标识
    private String usbDeviceLabel;

    private String title;

    //歌词文件
    private String lyricFilePath;

    private String album;
    private String singer;

    private String path;


    public MusicTable(@NonNull String musicId, String deviceId) {
        this.musicId = musicId;
        this.deviceId = deviceId;
    }

    @NonNull
    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(@NonNull String musicId) {
        this.musicId = musicId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUsbDeviceLabel() {
        return usbDeviceLabel;
    }

    public void setUsbDeviceLabel(String usbDeviceLabel) {
        this.usbDeviceLabel = usbDeviceLabel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLyricFilePath() {
        return lyricFilePath;
    }

    public void setLyricFilePath(String lyricFilePath) {
        this.lyricFilePath = lyricFilePath;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "MusicTable{" +
                "musicId='" + musicId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", usbDeviceLabel='" + usbDeviceLabel + '\'' +
                ", title='" + title + '\'' +
                ", lyricFileId='" + lyricFilePath + '\'' +
                ", album='" + album + '\'' +
                ", singer='" + singer + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
