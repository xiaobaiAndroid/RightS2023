package com.android.launcher.map;

import com.amap.api.navi.model.AMapNaviPath;
import com.android.launcher.R;
import com.android.launcher.util.CarDateUtils;
import com.android.launcher.util.DataUtils;
import com.android.launcher.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 算路策略
 * @date： 2023/12/1
 * @author: 78495
*/
public class NavStrategyAdapter extends BaseQuickAdapter<StrategyItem, BaseViewHolder> {

    public NavStrategyAdapter(List<StrategyItem> data) {
        super(R.layout.item_nav_strategy,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StrategyItem item) {
        if(item.isSelected()){
            int color = mContext.getResources().getColor(R.color.ffffff);
            helper.setTextColor(R.id.nameTV,color)
                    .setTextColor(R.id.distanceTv,color)
                    .setBackgroundColor(R.id.itemRootCL,mContext.getResources().getColor(R.color.cl_indicator))
                    .setTextColor(R.id.timeTv,color)
                    .setTextColor(R.id.trafficLightCountTv,color);
        }else{
            int color = mContext.getResources().getColor(R.color.cl_000000);
            helper.setTextColor(R.id.nameTV,color)
                    .setTextColor(R.id.distanceTv,color)
                    .setBackgroundColor(R.id.itemRootCL,0)
                    .setTextColor(R.id.timeTv,color)
                    .setTextColor(R.id.trafficLightCountTv,color);
        }
        helper.setText(R.id.nameTV,item.getName())
                .setText(R.id.distanceTv,item.getDistance()+mContext.getResources().getString(R.string.km))
                .setText(R.id.trafficLightCountTv,item.getTrafficLightCount()+"");
        long time = item.getTime();
        if(time<=0){
            helper.setText(R.id.timeTv, "");
        }else{
            helper.setText(R.id.timeTv, TimeUtils.covertFormat1(mContext,time)) ;
        }

    }
}
