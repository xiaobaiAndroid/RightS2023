package com.android.launcher.map;

import com.amap.api.services.core.PoiItemV2;
import com.android.launcher.R;
import com.bzf.module_db.entity.HistorySearchTable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import module.common.utils.StringUtils;

public class SearchHistoryAdapter extends BaseQuickAdapter<HistorySearchTable, BaseViewHolder> {

    public SearchHistoryAdapter(List<HistorySearchTable> data) {
        super(R.layout.item_map_result, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistorySearchTable item) {
        helper.setText(R.id.addressTV, StringUtils.removeNull(item.getProvinceName() + item.getCityName() + item.getAdName() + item.getTitle()));
    }
}