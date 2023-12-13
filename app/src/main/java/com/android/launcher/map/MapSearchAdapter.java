package com.android.launcher.map;

import com.amap.api.services.core.PoiItemV2;
import com.android.launcher.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import module.common.utils.StringUtils;

/**
 * @date： 2023/11/30
 * @author: 78495
 */
public class MapSearchAdapter extends BaseQuickAdapter<PoiItemV2, BaseViewHolder> {

    public MapSearchAdapter(List<PoiItemV2> data) {
        super(R.layout.item_map_result, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoiItemV2 item) {
        helper.setText(R.id.addressTV, StringUtils.removeNull(item.getProvinceName() + item.getCityName() + item.getAdName()+ item.getTitle()));
    }
}
