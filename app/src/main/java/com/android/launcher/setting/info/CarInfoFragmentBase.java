package com.android.launcher.setting.info;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import module.common.MessageEvent;

import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import module.common.utils.DensityUtil;
import com.android.launcher.view.LinearDividerDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public abstract class CarInfoFragmentBase extends FragmentBase {

    protected CarInfoAdapter mAdapter;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected final void initView(View view, Bundle savedInstanceState) {
        RecyclerView contentRV = view.findViewById(R.id.contentRV);
        contentRV.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new CarInfoAdapter(new ArrayList<>());
        contentRV.setAdapter(mAdapter);

        int padding = (int) getResources().getDimension(R.dimen.item_margin);
        LinearDividerDecoration linearSpaceDecoration = new LinearDividerDecoration<ColorDrawable>(mAdapter, new ColorDrawable(getResources().getColor(R.color.cl_50ffffff)),DensityUtil.dip2px(getContext(), 0.5f),padding,padding);
        linearSpaceDecoration.setmDrawLastItem(false);

        contentRV.addItemDecoration(linearSpaceDecoration);
    }

    protected abstract List<CarInfoItem> getTitleList();

    @Override
    protected final int getContentLayoutId() {
        return R.layout.fragment_common_list;
    }

    @Override
    protected final void setupData() {
        super.setupData();
        mAdapter.setNewData(getTitleList());

    }
}
