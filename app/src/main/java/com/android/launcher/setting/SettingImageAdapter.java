package com.android.launcher.setting;

import com.android.launcher.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @date： 2023/10/17
 * @author: 78495
*/
public class SettingImageAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    public SettingImageAdapter(List<Integer> data) {
        super(R.layout.item_setting_image,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer resId) {
        helper.setImageResource(R.id.settingIV,resId);
    }
}
