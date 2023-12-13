package com.android.launcher.setting.car.onoffbus;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.can.status.Can35dStatus;
import com.android.launcher.common.CommonCarAdapter;
import com.android.launcher.common.CommonItem;
import com.android.launcher.setting.lighting.CarLightingFragmentBase;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.android.launcher.view.LinearSpaceDecoration;
import com.bzf.module_db.entity.CarSetupTable;
import com.bzf.module_db.repository.CarSetupRepository;

import java.util.ArrayList;
import java.util.List;

import module.common.MessageEvent;
import module.common.utils.AppUtils;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;

/**
 * 上下车辅助
 * @date： 2023/10/18
 * @author: 78495
*/
public class OnOffBusAssistFragment extends FragmentBase {

    private CommonCarAdapter activateAdapter = new CommonCarAdapter(new ArrayList<>());

    private int currentPosition = 0;

    private List<CommonItem> modes = new ArrayList<>();

    private RecyclerView contentRV;
    private CarSetupTable carSetupTable;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        contentRV = view.findViewById(R.id.contentRV);
        contentRV.setLayoutManager(new LinearLayoutManager(getContext()));

        contentRV.setAdapter(activateAdapter);
        contentRV.setItemAnimator(null);

        LinearSpaceDecoration linearSpaceDecoration = new LinearSpaceDecoration(activateAdapter, (int) getResources().getDimension(R.dimen.item_space));
        linearSpaceDecoration.setmDrawLastItem(false);

        contentRV.addItemDecoration(linearSpaceDecoration);

        activateAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if (position != currentPosition) {
                activateAdapter.getItem(position).setSelected(true);
                activateAdapter.notifyItemChanged(position);

                if (currentPosition != -1) {
                    activateAdapter.getItem(currentPosition).setSelected(false);
                    activateAdapter.notifyItemChanged(currentPosition);
                }

                currentPosition = position;
                sendData();
            }
        });
    }

    private void sendData() {
        mExecutor.execute(() -> {
            if(carSetupTable!=null){
                String status;
                if(currentPosition == 0){
                    carSetupTable.setSteeringColumnAssist(false);
                    carSetupTable.setSeatAssist(false);
                    status = "00";
                }else if(currentPosition == 1){
                    carSetupTable.setSteeringColumnAssist(true);
                    carSetupTable.setSeatAssist(false);
                    status = "01";
                }else if(currentPosition == 2){
                    carSetupTable.setSeatAssist(true);
                    carSetupTable.setSteeringColumnAssist(false);
                    status = "10";
                }else{
                    carSetupTable.setSteeringColumnAssist(true);
                    carSetupTable.setSeatAssist(true);
                    status = "11";
                }

                CarSetupRepository.getInstance().updateData(getContext(),carSetupTable);

                String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.IN_AND_OUT_ASSIST.getTypeValue() + status + SerialPortDataFlag.END_FLAG;

                LogUtils.printI(TAG,"sendData--send="+send);

                SendHelperLeft.handler(send);
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

        modes.clear();

        modes.add(new CommonItem(getResources().getString(R.string.close),false));
        modes.add(new CommonItem(getResources().getString(R.string.steering_column),false));
        modes.add(new CommonItem(getResources().getString(R.string.seat),false));
        modes.add(new CommonItem(getResources().getString(R.string.steering_column_seat),false));

        activateAdapter.setNewData(modes);

        mExecutor.execute(() -> {

            carSetupTable = CarSetupRepository.getInstance().getData(getContext(), AppUtils.getDeviceId(getContext()));
            if (carSetupTable != null) {
                getActivity().runOnUiThread(() -> {
                    try {
                        boolean steeringColumnAssist = carSetupTable.isSteeringColumnAssist();
                        boolean seatAssist = carSetupTable.isSeatAssist();

                        if(steeringColumnAssist && seatAssist){
                            currentPosition = 3;
                        }else if(seatAssist){
                            currentPosition = 2;
                        }else if(steeringColumnAssist){
                            currentPosition = 1;
                        }else{
                            currentPosition = 0;
                        }
                        activateAdapter.getItem(currentPosition).setSelected(true);
                        activateAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

    }

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }
}
