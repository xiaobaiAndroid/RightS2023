package com.android.launcher.ac.winddirection.rear;

import androidx.cardview.widget.CardView;

import com.android.launcher.R;
import com.android.launcher.ac.winddirection.front.WindDirectionItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lihang.ShadowLayout;

import java.util.List;

/**
 * 后空调
 * @date： 2023/10/15
 * @author: 78495
 */
public class RearWindDirectionAdapter extends BaseMultiItemQuickAdapter<RearWindDirectionItem, BaseViewHolder> {


    public RearWindDirectionAdapter(List<RearWindDirectionItem> data) {
        super(data);
        addItemType(WindDirectionItem.Type.NORMAL.ordinal(), R.layout.item_airflow);
        addItemType(WindDirectionItem.Type.AUTO.ordinal(), R.layout.item_airflow_auto);
    }

    @Override
    protected void convert(BaseViewHolder helper, RearWindDirectionItem item) {

        if (item.getItemType() == RearWindDirectionItem.Type.NORMAL.ordinal()) {
            helper.setImageResource(R.id.airflowIV, item.getResId());
        }
        CardView windDirCV = helper.getView(R.id.windDirCV);
        if(item.isSelected()){
            windDirCV.setCardBackgroundColor(mContext.getResources().getColor(R.color.cl_indicator));
        }else{
            windDirCV.setCardBackgroundColor(mContext.getResources().getColor(R.color.cl_carview));
        }
    }

}
