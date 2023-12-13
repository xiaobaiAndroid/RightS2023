package com.android.launcher.ac.winddirection.front;

import androidx.cardview.widget.CardView;

import com.android.launcher.R;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lihang.ShadowLayout;

import java.util.List;

/**
 * 前空调
 * @date： 2023/10/15
 * @author: 78495
 */
public class FrontWindDirectionAdapter extends BaseMultiItemQuickAdapter<WindDirectionItem, BaseViewHolder> {


    public FrontWindDirectionAdapter(List<WindDirectionItem> data) {
        super(data);
        addItemType(WindDirectionItem.Type.NORMAL.ordinal(), R.layout.item_airflow);
        addItemType(WindDirectionItem.Type.AUTO.ordinal(), R.layout.item_airflow_auto);
    }

    @Override
    protected void convert(BaseViewHolder helper, WindDirectionItem item) {

        if (item.getItemType() == WindDirectionItem.Type.NORMAL.ordinal()) {
            helper.setImageResource(R.id.airflowIV, item.getResId());
        }
        CardView windDirCV = helper.getView(R.id.windDirCV);
        if(item.isSelected()){
            windDirCV.setCardBackgroundColor(mContext.getResources().getColor(R.color.cl_indicator));
        }else {
            windDirCV.setCardBackgroundColor(mContext.getResources().getColor(R.color.cl_carview));
        }
        helper.addOnClickListener(R.id.windDirCV);
    }

}
