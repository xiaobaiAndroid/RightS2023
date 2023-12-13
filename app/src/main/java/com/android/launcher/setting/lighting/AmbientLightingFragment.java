package com.android.launcher.setting.lighting;

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
 * 环境照明
 * @date： 2023/10/18
 * @author: 78495
*/
public class AmbientLightingFragment extends CarLightingFragmentBase{

    @Override
    protected void updateData(int position) {
        mExecutor.execute(() -> {
            final String send;
            String status;
            switch (position) {
                case 0:
                status = Can35dStatus.D3.DELAY_0.getValue();
                break;
                case 1:
                    status = Can35dStatus.D3.DELAY_1.getValue();
                    break;
                case 2:
                    status = Can35dStatus.D3.DELAY_2.getValue();
                    break;
                case 3:
                    status = Can35dStatus.D3.DELAY_3.getValue();
                    break;
                case 4:
                    status = Can35dStatus.D3.DELAY_4.getValue();
                    break;
                default:
                    status = Can35dStatus.D3.DELAY_5.getValue();
                    break;
            }

            CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getContext(), AppUtils.getDeviceId(getContext()));
            carSetupTable.setInnerLighting(status);
            CarSetupRepository.getInstance().updateData(getContext(),carSetupTable);

            send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.AMBIENT_LIGHTING.getTypeValue() +  status + SerialPortDataFlag.END_FLAG;
            SendHelperLeft.handler(send);
        });
    }

    @Override
    protected void setupData() {
        super.setupData();

        List<CommonItem> list = new ArrayList<>();
        list.add(new CommonItem("0",false));
        list.add(new CommonItem("1",false));
        list.add(new CommonItem("2",false));
        list.add(new CommonItem("3",false));
        list.add(new CommonItem("4",false));
        list.add(new CommonItem("5",false));

        if(currentPosition >= 0){
            list.get(currentPosition).setSelected(true);
        }
        mAdapter.setNewData(list);
    }
}
