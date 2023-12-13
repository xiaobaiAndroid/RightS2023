package com.android.launcher.common;

import android.graphics.Color;

import com.android.launcher.R;

import module.common.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @date： 2023/10/17
 * @author: 78495
*/
public class CommonCarAdapter extends BaseQuickAdapter<CommonItem, BaseViewHolder> {


    public CommonCarAdapter(List<CommonItem> data) {
        super(R.layout.item_car_type_select,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommonItem item) {
        if(item!=null){
            helper.setText(R.id.titleTV, StringUtils.removeNull(item.getTitle()));
            if(item.isGoneResId()){
                helper.setGone(R.id.selectedIV,false);
            }else{
                helper.setGone(R.id.selectedIV,true);
                if(item.isSelected()){
                    helper.setImageResource(R.id.selectedIV,R.drawable.car_model_icon_selected)
                            .setBackgroundColor(R.id.itemRootCL, mContext.getResources().getColor(R.color.cl_item_selected));
                }else{
                    helper.setImageResource(R.id.selectedIV,R.drawable.car_model_icon)
                            .setBackgroundColor(R.id.itemRootCL, 0);
                }
            }

        }

    }
}
