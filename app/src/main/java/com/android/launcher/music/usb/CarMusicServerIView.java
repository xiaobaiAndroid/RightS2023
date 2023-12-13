package com.android.launcher.music.usb;

import com.android.launcher.base.IView;
import com.android.launcher.music.CarMusicItem;

import java.util.List;

public interface CarMusicServerIView extends IView {


    void getDataResult(List<CarMusicItem> list);

    void getDataFail();
}
