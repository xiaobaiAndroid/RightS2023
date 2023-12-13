package com.android.launcher.setting.car;

import android.widget.TextView;

import com.android.launcher.common.CommonItem;
import com.android.launcher.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @date： 2023/10/17
 * @author: 78495
*/
public class CarSettingTitleAdapter extends BaseQuickAdapter<CommonItem,BaseViewHolder> {

    public CarSettingTitleAdapter(List<CommonItem> data) {
        super(R.layout.item_car_setting_title,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommonItem item) {
        TextView titleTV = helper.getView(R.id.titleTV);
        if(item.isSelected()){
            titleTV.setTextColor(mContext.getResources().getColor(R.color.cl_indicator));
            titleTV.setTextSize(22);
        }else{
            titleTV.setTextColor(mContext.getResources().getColor(R.color.cl_ffffff));
            titleTV.setTextSize(18);
        }
        helper.setText(R.id.titleTV,item.getTitle());
    }
}
