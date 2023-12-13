package com.android.launcher.setting.offlinemap;

import android.widget.TextView;

import com.android.launcher.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @date： 2023/12/7
 * @author: 78495
*/
public class OfflineMapProvinceAdapter extends BaseQuickAdapter<OfflineMapProvinceItem, BaseViewHolder> {

    public OfflineMapProvinceAdapter(List<OfflineMapProvinceItem> data) {
        super(R.layout.item_offlinemap_province,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OfflineMapProvinceItem item) {
        TextView titleTV = helper.getView(R.id.titleTV);
        if(item.isSelected()){
            titleTV.setTextColor(mContext.getResources().getColor(R.color.cl_indicator));
            titleTV.setTextSize(22);
        }else{
            titleTV.setTextColor(mContext.getResources().getColor(R.color.cl_ffffff));
            titleTV.setTextSize(18);
        }
        helper.setText(R.id.titleTV,item.getOfflineMapProvince().getProvinceName());
    }
}
