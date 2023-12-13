package com.android.launcher.setting.offlinemap;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.android.launcher.R;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * @date： 2023/12/7
 * @author: 78495
*/
public class MapDownloadDialog extends CenterPopupView {

    private NumberProgressBar numberProgressBar;

    private OfflineMapCity offlineMapCity;
    private TextView hintTv;
    private TextView cancelTv;

    private OnClickListener cancelListener;

    public MapDownloadDialog(Context context, OfflineMapCity offlineMapCity, OnClickListener listener) {
        super(context);
        this.offlineMapCity = offlineMapCity;
        this.cancelListener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_map_download;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView cityTv = findViewById(R.id.cityTv);
        hintTv = findViewById(R.id.hintTv);
        numberProgressBar = findViewById(R.id.numberProgressBar);

        cancelTv = findViewById(R.id.cancelTv);
        if(offlineMapCity!=null){
            cityTv.setText(offlineMapCity.getCity());
//            if(offlineMapCity.getcompleteCode() == 100){
//                hintTv.setText(getResources().getString(R.string.updating));
//            }else{
                hintTv.setText(getResources().getString(R.string.downloading));
//            }
        }
        cancelTv.setOnClickListener(cancelListener);
    }

    public void updateProgress(int status, int progress){
        if(status == 1){
            hintTv.setText(getResources().getString(R.string.unzip));
        }
        numberProgressBar.setProgress(progress);
    }

}
