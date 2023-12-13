package com.android.launcher.ac.airflow;

import androidx.cardview.widget.CardView;

import com.android.launcher.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @date： 2023/11/14
 * @author: 78495
*/
public class AirflowPatternAdapter extends BaseQuickAdapter<AirflowPatternItem, BaseViewHolder> {

    public AirflowPatternAdapter(List<AirflowPatternItem> data) {
        super(R.layout.item_airflow_pattern,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AirflowPatternItem item) {
        CardView cardView = helper.getView(R.id.cardView);
        if(item.isSelected()){
            cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.cl_indicator));
        }else {
            cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.cl_carview));
        }
        helper.setText(R.id.contentTV,item.getName());
    }
}
