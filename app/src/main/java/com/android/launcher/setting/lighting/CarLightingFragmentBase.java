package com.android.launcher.setting.lighting;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import module.common.MessageEvent;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.common.CommonCarAdapter;
import module.common.utils.DensityUtil;
import com.android.launcher.view.LinearSpaceDecoration;

import java.util.ArrayList;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public abstract class CarLightingFragmentBase extends FragmentBase {

    protected CommonCarAdapter mAdapter;

    protected int currentPosition = 0;


    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        RecyclerView contentRV = view.findViewById(R.id.contentRV);
        contentRV.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new CommonCarAdapter(new ArrayList<>());
        contentRV.setAdapter(mAdapter);
        contentRV.setItemAnimator(null);

        LinearSpaceDecoration linearSpaceDecoration = new LinearSpaceDecoration(mAdapter, (int) getResources().getDimension(R.dimen.item_space));
        linearSpaceDecoration.setmDrawLastItem(false);

        contentRV.addItemDecoration(linearSpaceDecoration);

        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if (position != currentPosition) {
                mAdapter.getItem(position).setSelected(true);
                mAdapter.notifyItemChanged(position);

                mAdapter.getItem(currentPosition).setSelected(false);
                mAdapter.notifyItemChanged(currentPosition);
                currentPosition = position;
                if(isShow){
                    updateData(currentPosition);
                }
            }
        });
    }

    protected abstract void updateData(int position);

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_common_list;
    }
}
