package com.android.launcher.setting.offlinemap;

import com.amap.api.maps.offlinemap.OfflineMapProvince;

public class OfflineMapProvinceItem {

    //已经下载了
    private boolean selected;

    private OfflineMapProvince OfflineMapProvince;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public OfflineMapProvince getOfflineMapProvince() {
        return OfflineMapProvince;
    }

    public void setProvince(OfflineMapProvince OfflineMapProvince) {
        this.OfflineMapProvince = OfflineMapProvince;
    }
}
