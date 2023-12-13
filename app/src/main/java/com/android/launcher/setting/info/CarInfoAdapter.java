package com.android.launcher.setting.info;

import com.android.launcher.R;
import module.common.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public class CarInfoAdapter extends BaseQuickAdapter<CarInfoItem, BaseViewHolder> {

    public CarInfoAdapter(List<CarInfoItem> data) {
        super(R.layout.item_car_info,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarInfoItem item) {
        helper.setText(R.id.titleTV, StringUtils.removeNull(item.getTitle()))
                .setText(R.id.contentTV,StringUtils.removeNull(item.getContent()));
    }
}
