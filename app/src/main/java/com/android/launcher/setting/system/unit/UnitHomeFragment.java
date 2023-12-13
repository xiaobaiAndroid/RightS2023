package com.android.launcher.setting.system.unit;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.launcher.MainActivity;

import module.common.MessageEvent;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.common.CommonItem;
import com.android.launcher.common.CommonCarAdapter;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.type.UnitType;
import com.android.launcher.usbdriver.SendHelperLeft;
import module.common.utils.DensityUtil;
import com.android.launcher.util.FuncUtil;
import module.common.utils.SPUtils;
import com.android.launcher.view.LinearSpaceDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 单位
 *
 * @date： 2023/10/19
 * @author: 78495
 */
public class UnitHomeFragment extends FragmentBase {

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
        list.add(new CommonItem("km/h", false, false));
        list.add(new CommonItem("mph", false, false));

        if (MainActivity.unitType == UnitType.KM.ordinal()) {
            list.get(0).setSelected(true);
        } else {
            list.get(1).setSelected(true);
        }

        mAdapter = new CommonCarAdapter(list);
        contentRV.setAdapter(mAdapter);

        LinearSpaceDecoration linearSpaceDecoration = new LinearSpaceDecoration(mAdapter, (int) getResources().getDimension(R.dimen.item_space));
        linearSpaceDecoration.setmDrawLastItem(false);
        contentRV.addItemDecoration(linearSpaceDecoration);

        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if(currentPosition == position){
                return;
            }
            CommonItem selectedItem = mAdapter.getItem(position);
            if(selectedItem!=null){
                selectedItem.setSelected(true);
            }
            mAdapter.notifyItemChanged(position);

            CommonItem lastedItem = mAdapter.getItem(currentPosition);
            if(lastedItem!=null){
                lastedItem.setSelected(false);
            }
            mAdapter.notifyItemChanged(currentPosition);

            updateData(position);
        });
    }

    private void updateData(int position) {
        mExecutor.execute(() -> {
            try {
                SPUtils.putInt(getContext(), SPUtils.SP_UNIT_TYPE, position);
                String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.UNIT_TYPE.getTypeValue() + position + SerialPortDataFlag.END_FLAG;
                SendHelperLeft.handler(send);

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FuncUtil.sendShellCommand("reboot");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_common_list;
    }
}
