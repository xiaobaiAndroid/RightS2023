package com.android.launcher.setting.car.assist;

import com.android.launcher.common.CommonItem;
import com.android.launcher.view.SwitchButton;
import com.android.launcher.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @date： 2023/10/17
 * @author: 78495
*/
public class CarAssistAdapter extends BaseQuickAdapter<CommonItem, BaseViewHolder> {


    public CarAssistAdapter(List<CommonItem> data) {
        super(R.layout.item_car_assist,data);
    }



    @Override
    protected void convert(BaseViewHolder helper, CommonItem item) {
        helper.setText(R.id.titleTV,item.getTitle());

        SwitchButton switchButton = helper.getView(R.id.switchButton);
        switchButton.setChecked(item.isSelected(),false);

//        LogUtils.printI(CarAssistAdapter.class.getSimpleName(),"isChecked="+switchButton.isChecked() +", isSelected="+item.isSelected());
    }


}
