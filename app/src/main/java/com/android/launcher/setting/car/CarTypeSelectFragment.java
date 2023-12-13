package com.android.launcher.setting.car;

import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import module.common.MessageEvent;

import com.android.launcher.common.CommonCarAdapter;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.view.LinearSpaceDecoration;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.common.CommonItem;
import com.android.launcher.type.CarType;
import com.android.launcher.usbdriver.SendHelperLeft;

import module.common.utils.DensityUtil;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 汽车类型选择
 *
 * @date： 2023/10/17
 * @author: 78495
 */
public class CarTypeSelectFragment extends FragmentBase {

    private CommonCarAdapter commonCarAdapter;

    private int currentPosition = 0;

    private CarType mCurrentCarType = CarType.S500;
    private RecyclerView contentRV;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        contentRV = view.findViewById(R.id.contentRV);
        contentRV.setLayoutManager(new LinearLayoutManager(getContext()));
        commonCarAdapter = new CommonCarAdapter(new ArrayList<>());
        contentRV.setAdapter(commonCarAdapter);
        contentRV.setItemAnimator(null);

        LinearSpaceDecoration linearSpaceDecoration = new LinearSpaceDecoration(commonCarAdapter, (int) getResources().getDimension(R.dimen.item_space));
        linearSpaceDecoration.setmDrawLastItem(false);
        contentRV.addItemDecoration(linearSpaceDecoration);

        commonCarAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if (position != currentPosition) {
                commonCarAdapter.getItem(currentPosition).setSelected(false);
                commonCarAdapter.notifyItemChanged(currentPosition);

                commonCarAdapter.getItem(position).setSelected(true);
                commonCarAdapter.notifyItemChanged(position);

                currentPosition = position;
                mCurrentCarType = getCarType(currentPosition);
                updateCarType();
            }
        });
    }

    private void updateCarType() {
        mExecutor.execute(() -> {
            SPUtils.putInt(getContext(), SPUtils.SP_CAR_TYPE, mCurrentCarType.ordinal());
            try {
                String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.CAR_TYPE.getTypeValue() + "0" + mCurrentCarType.ordinal() + SerialPortDataFlag.END_FLAG;
                SendHelperLeft.handler(send.toUpperCase());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_common_list;
    }


    @Override
    protected void setupData() {
        super.setupData();

        List<CommonItem> list = new ArrayList<>();
        list.add(new CommonItem("S65", false));
        list.add(new CommonItem("S300", false));
        list.add(new CommonItem("S350", false));
        list.add(new CommonItem("S400", false));
        list.add(new CommonItem("S450", false));
        list.add(new CommonItem("S500", false));
        list.add(new CommonItem("S600", false));
        commonCarAdapter.setNewData(list);


        mExecutor.execute(() -> {
            int carType = SPUtils.getInt(getContext(), SPUtils.SP_CAR_TYPE, CarType.S500.ordinal());
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    LogUtils.printI(TAG, "carType=" + carType);
                    commonCarAdapter.getItem(mCurrentCarType.ordinal()).setSelected(true);
                    commonCarAdapter.notifyItemChanged(mCurrentCarType.ordinal());

                    if (mCurrentCarType.ordinal() != currentPosition) {
                        commonCarAdapter.getItem(currentPosition).setSelected(false);
                        commonCarAdapter.notifyItemChanged(currentPosition);
                    }
                    contentRV.scrollToPosition(currentPosition);
                    currentPosition = mCurrentCarType.ordinal();
                });
            }
        });
    }

    private CarType getCarType(int position) {
        CarType carType;
        switch (position) {
            case 0:
                carType = CarType.S65;
                break;
            case 1:
                carType = CarType.S300;
                break;
            case 2:
                carType = CarType.S350;
                break;
            case 3:
                carType = CarType.S400;
                break;
            case 4:
                carType = CarType.S450;
                break;
            case 6:
                carType = CarType.S600;
                break;
            default:
                carType = CarType.S500;
                break;
        }
        return carType;
    }
}
