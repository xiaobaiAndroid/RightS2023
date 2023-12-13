package com.android.launcher.setting.car;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.launcher.MainActivity;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.common.CommonCarAdapter;
import com.android.launcher.common.CommonItem;
import com.android.launcher.type.UnitType;
import com.android.launcher.view.LinearSpaceDecoration;

import module.common.MessageEvent;
import com.android.launcher.R;

import module.common.utils.DensityUtil;
import module.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 时速限制
 * @date： 2023/10/17
 * @author: 78495
*/
public class SpeedLimitFragment extends FragmentBase {

    private CommonCarAdapter mAdapter;

    private int currentPosition = 0;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        RecyclerView contentRV = view.findViewById(R.id.contentRV);
        contentRV.setLayoutManager(new LinearLayoutManager(getContext()));

        List<CommonItem> list = new ArrayList<>();
        list.add(new CommonItem(getResources().getString(R.string.close), false));
        if(MainActivity.unitType == UnitType.KM.ordinal()){
            list.add(new CommonItem("100km/h", false));
            list.add(new CommonItem("150km/h", false));
            list.add(new CommonItem("200km/h", false));
            list.add(new CommonItem("250km/h", false));
        }else{
            list.add(new CommonItem("100mph", false));
            list.add(new CommonItem("150mph", false));
            list.add(new CommonItem("200mph", false));
            list.add(new CommonItem("250mph", false));
        }

        mAdapter = new CommonCarAdapter(list);
        contentRV.setAdapter(mAdapter);

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
                updateData();
            }
        });
    }

    private void updateData() {
        mExecutor.execute(() -> SPUtils.putInt(getContext(), SPUtils.SPEED_LIMIT,currentPosition));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_common_list;
    }

    @Override
    protected void setupData() {
        super.setupData();
        mExecutor.execute(() -> {
            currentPosition = SPUtils.getInt(getContext(), SPUtils.SPEED_LIMIT,0);
            if(getActivity()!=null){
                getActivity().runOnUiThread(() -> {
                    if(currentPosition > 0){
                        mAdapter.getItem(currentPosition).setSelected(true);
                        mAdapter.notifyItemChanged(currentPosition);
                    }
                });
            }
        });
    }
}
