package com.android.launcher.setting.car.mileage;

import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;

import com.android.launcher.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public class NumberAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public NumberAdapter(List<String> data) {
        super(R.layout.item_number,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.numberTV,item);
    }

}
