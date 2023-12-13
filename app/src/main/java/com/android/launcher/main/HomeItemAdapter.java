package com.android.launcher.main;

import com.android.launcher.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @date： 2023/10/12
 * @author: 78495
*/
public class HomeItemAdapter extends BaseQuickAdapter<HomeItem, BaseViewHolder> {

    public HomeItemAdapter(List<HomeItem> data) {
        super(R.layout.item_right_home,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeItem item) {
        helper.setImageResource(R.id.itemIV,item.getResId())
                .setText(R.id.contentTV,item.getName());
    }
}
