package com.android.launcher.music.bluetooth;

/**
 * 蓝牙音乐播放状态
 * @date： 2023/11/11
 * @author: 78495
*/
public enum PlayStatus {
    NONE(0),
    STOP(2),
    PLAY(3);

    private int value;

    PlayStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
