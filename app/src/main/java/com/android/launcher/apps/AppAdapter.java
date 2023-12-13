package com.android.launcher.apps;

import android.content.pm.ApplicationInfo;

import com.android.launcher.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class AppAdapter extends BaseQuickAdapter<ApplicationInfo, BaseViewHolder> {

    public AppAdapter(List<ApplicationInfo> data) {
        super(R.layout.item_app,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApplicationInfo item) {
        helper.setImageDrawable(R.id.iconIV,item.loadIcon(mContext.getPackageManager()))
            .setText(R.id.nameTV,(String) item.loadLabel(mContext.getPackageManager()));
    }
}
