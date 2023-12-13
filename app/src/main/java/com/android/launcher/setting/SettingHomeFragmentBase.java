package com.android.launcher.setting;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import module.common.MessageEvent;

import com.android.launcher.common.CommonItem;
import com.android.launcher.setting.car.CarSettingTitleAdapter;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.common.CommonFragmentAdapter;

import module.common.utils.DensityUtil;
import module.common.utils.LogUtils;
import com.android.launcher.view.LinearDividerDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public abstract class SettingHomeFragmentBase extends FragmentBase {

    protected RecyclerView titleRV;
    protected ViewPager2 contentVP;

    protected CarSettingTitleAdapter titleAdapter;

    protected CommonFragmentAdapter fragmentAdapter;

    protected int currentPosition = 0;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected final void initView(View view, Bundle savedInstanceState) {
        titleRV = view.findViewById(R.id.titleRV);
        contentVP = view.findViewById(R.id.contentVP);
        contentVP.setUserInputEnabled(false);

        titleRV.setLayoutManager(new LinearLayoutManager(getContext()));
        titleRV.setItemAnimator(null);

        titleAdapter = new CarSettingTitleAdapter(new ArrayList<>());
        titleRV.setAdapter(titleAdapter);

        titleAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if(currentPosition != position){

                resetAdapterData(titleAdapter);

                CommonItem selectedItem = titleAdapter.getItem(position);
                if(selectedItem!=null){
                    selectedItem.setSelected(true);
                    titleAdapter.notifyItemChanged(position);
                }
                currentPosition = position;
                LogUtils.printI(TAG, "currentPosition="+currentPosition);
                contentVP.setCurrentItem(position,false);
            }
        });
    }

    private void resetAdapterData(CarSettingTitleAdapter titleAdapter) {
        for (int i=0; i<titleAdapter.getItemCount(); i++){
            CommonItem commonItem = titleAdapter.getItem(i);
            if(commonItem!=null){
                if(commonItem.isSelected()){
                    commonItem.setSelected(false);
                    titleAdapter.notifyItemChanged(i);
                }
            }
        }
    }

    @Override
    protected final void setupData() {
        super.setupData();

        titleAdapter.setNewData(getTitleList());
        int padding = DensityUtil.dip2px(getContext(),10);
        LinearDividerDecoration<ColorDrawable> dividerDecoration = new LinearDividerDecoration<>(titleAdapter, new ColorDrawable(getResources().getColor(R.color.cl_30ffffff)), DensityUtil.dip2px(getContext(), 0.5f),padding,padding);
        titleRV.addItemDecoration(dividerDecoration);

        fragmentAdapter=  new CommonFragmentAdapter(this,getFragmentList());
        contentVP.setAdapter(fragmentAdapter);

    }

    protected abstract List<CommonItem> getTitleList();

    protected abstract List<Fragment> getFragmentList();

    @Override
    protected final int getContentLayoutId() {
        return R.layout.fragment_car_setting;
    }
}
