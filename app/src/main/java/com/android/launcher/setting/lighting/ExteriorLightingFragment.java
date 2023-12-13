package com.android.launcher.setting.lighting;

import com.android.launcher.R;
import com.android.launcher.can.status.Can35dStatus;
import com.android.launcher.common.CommonItem;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;
import module.common.utils.AppUtils;
import com.bzf.module_db.entity.CarSetupTable;
import com.bzf.module_db.repository.CarSetupRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * 外部照明
 * @date： 2023/10/18
 * @author: 78495
*/
public class ExteriorLightingFragment extends CarLightingFragmentBase {

    @Override
    protected void updateData(int position) {
        mExecutor.execute(() -> {
            final String send;
            String status;
            switch (position) {
                case 0:
                    status = Can35dStatus.D2.DELAY_60.getValue();
                    break;
                case 1:
                    status = Can35dStatus.D2.DELAY_45.getValue();
                    break;
                case 2:
                    status = Can35dStatus.D2.DELAY_30.getValue();
                    break;
                case 3:
                    status = Can35dStatus.D2.DELAY_15.getValue();
                    break;
                default:
                    status = Can35dStatus.D2.DELAY_0.getValue();
                    break;
            }

            CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getContext(), AppUtils.getDeviceId(getContext()));
            carSetupTable.setExternalLighting(status);
            CarSetupRepository.getInstance().updateData(getContext(),carSetupTable);

            send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.EXTERIOR_LIGHTING.getTypeValue() +  status + SerialPortDataFlag.END_FLAG;
            SendHelperLeft.handler(send);
        });
    }

    @Override
    protected void setupData() {
        super.setupData();
        currentPosition = 4;

        mExecutor.execute(() -> {
            CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getContext(), AppUtils.getDeviceId(getContext()));
            if(carSetupTable!=null){
                if (carSetupTable.getExternalLighting().equals(Can35dStatus.D2.DELAY_0.getValue())) {
                    currentPosition = 0;
                } else if (carSetupTable.getExternalLighting().equals(Can35dStatus.D2.DELAY_15.getValue())) {
                    currentPosition = 1;
                } else if (carSetupTable.getExternalLighting().equals(Can35dStatus.D2.DELAY_30.getValue())) {
                    currentPosition = 2;
                } else if (carSetupTable.getExternalLighting().equals(Can35dStatus.D2.DELAY_45.getValue())) {
                    currentPosition = 3;
                } else if (carSetupTable.getExternalLighting().equals(Can35dStatus.D2.DELAY_60.getValue())) {
                    currentPosition = 4;
                }

                if(getActivity()!=null){
                    getActivity().runOnUiThread(() -> {
                        String secondUnit = getResources().getString(R.string.second);

                        List<CommonItem> list = new ArrayList<>();
                        list.add(new CommonItem(getResources().getString(R.string.close),false));
                        list.add(new CommonItem("15"+secondUnit,false));
                        list.add(new CommonItem("30"+secondUnit,false));
                        list.add(new CommonItem("45"+secondUnit,false));
                        list.add(new CommonItem("60"+secondUnit,false));

                        if(currentPosition >=0){
                            list.get(currentPosition).setSelected(true);
                        }
                        mAdapter.setNewData(list);
                    });
                }
            }
        });
    }
}
