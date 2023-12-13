package com.android.launcher.setting.car.assist;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import module.common.MessageEvent;

import com.android.launcher.base.FragmentBase;
import com.android.launcher.can.status.Can35dStatus;
import com.android.launcher.can.status.RearHatchCoverStatus;
import com.android.launcher.common.CommonItem;
import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.android.launcher.util.CommonUtil;
import com.android.launcher.view.LinearSpaceDecoration;
import com.android.launcher.R;

import module.common.utils.AppUtils;
import module.common.utils.DensityUtil;
import module.common.utils.LogUtils;

import com.bzf.module_db.entity.CarSetupTable;
import com.bzf.module_db.repository.CarSetupRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆辅助
 *
 * @date： 2023/10/17
 * @author: 78495
 */
public class AssistListFragment extends FragmentBase {

    private CarAssistAdapter mAdapter;

    private int currentPosition = 0;
    private String deviceId;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        RecyclerView contentRV = view.findViewById(R.id.contentRV);
        contentRV.setLayoutManager(new LinearLayoutManager(getContext()));



        mAdapter = new CarAssistAdapter(new ArrayList<>());
        contentRV.setAdapter(mAdapter);
        contentRV.setItemAnimator(null);

        LinearSpaceDecoration linearSpaceDecoration = new LinearSpaceDecoration(mAdapter, (int) getResources().getDimension(R.dimen.item_space));
        linearSpaceDecoration.setmDrawLastItem(false);

        contentRV.addItemDecoration(linearSpaceDecoration);

        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            LogUtils.printI(TAG,"setOnItemClickListener---position="+position);
            CommonItem commonItem = mAdapter.getItem(position);
            if (commonItem != null) {
                commonItem.setSelected(!commonItem.isSelected());
                mAdapter.notifyItemChanged(position);
                if (position == 0) {
                    updateDoorLockResponseData(commonItem.isSelected());
                } else if (position == 1) {
                    updateAutomaticClosingData(commonItem.isSelected());
                } else if (position == 2) {
                    updateRearviewMirrorDownData(commonItem.isSelected());
                } else if (position == 3) {
                    updateSuitcaseOpeningLimitData(commonItem.isSelected());
                }else if(position == 4){
                    updateHeadrestReclineData(commonItem.isSelected());
                }else if(position == 5){
                    updateSeatBeltAssistData(commonItem.isSelected());
                }
            }
        });


    }


    //安全带辅助
    private void updateSeatBeltAssistData(boolean selected) {
        mExecutor.execute(() -> {
            try {
                CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getContext(), deviceId);
                carSetupTable.setSeatBeltAssist(selected);
                CarSetupRepository.getInstance().saveData(getContext(), deviceId, carSetupTable);

                String command;
                if(carSetupTable.isSeatBeltAssist()){
                    command = "01";
                }else{
                    command = "00";
                }
                String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.SEAT_BELT_ASSIST.getTypeValue() + command + SerialPortDataFlag.END_FLAG;
                LogUtils.printI(TAG,"updateSeatBeltAssistData--send="+send);

                SendHelperLeft.handler(send);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //头枕放倒
    private void updateHeadrestReclineData(boolean selected) {
        mExecutor.execute(() -> {
            try {
                CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getContext(), deviceId);
                carSetupTable.setHeadrestRecline(selected);
                CarSetupRepository.getInstance().saveData(getContext(), deviceId, carSetupTable);

                String command;
                if(carSetupTable.isHeadrestRecline()){
                    command = "01";
                }else{
                    command = "00";
                }
                String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.HEADREST_RECLINE.getTypeValue() + command + SerialPortDataFlag.END_FLAG;

                LogUtils.printI(TAG,"updateHeadrestReclineData--send="+send);
                SendHelperLeft.handler(send);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //后舱盖高度限制
    private void updateSuitcaseOpeningLimitData(boolean selected) {
        mExecutor.execute(() -> {
            try {
                CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getContext(), deviceId);
                carSetupTable.setTrunkOpenLimit(selected);
                CarSetupRepository.getInstance().saveData(getContext(), deviceId, carSetupTable);

                BinaryEntity binaryEntity = new BinaryEntity();
                if(carSetupTable.isAutomaticRarFolding()){
                    binaryEntity.setB2(BinaryEntity.Value.NUM_1);
                }else {
                    binaryEntity.setB2(BinaryEntity.Value.NUM_0);
                }

                if( carSetupTable.isRearviewMirrorDown()){
                    binaryEntity.setB0(BinaryEntity.Value.NUM_1);
                }else{
                    binaryEntity.setB0(BinaryEntity.Value.NUM_0);
                }

                String can35D5Left;
                //后舱盖高度限制 开启
                if(carSetupTable.isTrunkOpenLimit()){
                    can35D5Left = RearHatchCoverStatus.STATE_OPEN.getValue();
                }else{
                    can35D5Left = RearHatchCoverStatus.STATE_CLOSE.getValue();
                }

                String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.AUTOMATIC_CLOSING.getTypeValue() + can35D5Left + binaryEntity.getHexData() + SerialPortDataFlag.END_FLAG;

                LogUtils.printI(TAG,"updateSuitcaseOpeningLimitData--send="+send);
                SendHelperLeft.handler(send);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //后驶时后视镜下降
    private void updateRearviewMirrorDownData(boolean selected) {
        mExecutor.execute(() -> {
            try {
                CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getContext(), deviceId);
                carSetupTable.setRearviewMirrorDown(selected);
                CarSetupRepository.getInstance().saveData(getContext(), deviceId, carSetupTable);

                BinaryEntity binaryEntity = new BinaryEntity();
                if(carSetupTable.isAutomaticRarFolding()){
                    binaryEntity.setB2(BinaryEntity.Value.NUM_1);
                }else {
                    binaryEntity.setB2(BinaryEntity.Value.NUM_0);
                }

                if( carSetupTable.isRearviewMirrorDown()){
                    binaryEntity.setB0(BinaryEntity.Value.NUM_1);
                }else{
                    binaryEntity.setB0(BinaryEntity.Value.NUM_0);
                }

                String can35D5Left;
                //后舱盖高度限制 开启
                if(carSetupTable.isTrunkOpenLimit()){
                    can35D5Left = RearHatchCoverStatus.STATE_OPEN.getValue();
                }else{
                    can35D5Left = RearHatchCoverStatus.STATE_CLOSE.getValue();
                }

                String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.AUTOMATIC_CLOSING.getTypeValue() + can35D5Left + binaryEntity.getHexData() + SerialPortDataFlag.END_FLAG;

                LogUtils.printI(TAG,"updateRearviewMirrorDownData--send="+send);
                SendHelperLeft.handler(send);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //锁车时自动合拢
    private void updateAutomaticClosingData(boolean selected) {
        mExecutor.execute(() -> {
            try {
                CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getContext(), deviceId);
                carSetupTable.setAutomaticRarFolding(selected);
                CarSetupRepository.getInstance().saveData(getContext(), deviceId, carSetupTable);

                BinaryEntity binaryEntity = new BinaryEntity();
                if(carSetupTable.isAutomaticRarFolding()){
                    binaryEntity.setB2(BinaryEntity.Value.NUM_1);
                }else {
                    binaryEntity.setB2(BinaryEntity.Value.NUM_0);
                }

                if( carSetupTable.isRearviewMirrorDown()){
                    binaryEntity.setB0(BinaryEntity.Value.NUM_1);
                }else{
                    binaryEntity.setB0(BinaryEntity.Value.NUM_0);
                }

                String can35D5Left;
                //后舱盖高度限制 开启
                if(carSetupTable.isTrunkOpenLimit()){
                    can35D5Left = RearHatchCoverStatus.STATE_OPEN.getValue();
                }else{
                    can35D5Left = RearHatchCoverStatus.STATE_CLOSE.getValue();
                }


                String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.AUTOMATIC_CLOSING.getTypeValue() + can35D5Left + binaryEntity.getHexData() + SerialPortDataFlag.END_FLAG;

                LogUtils.printI(TAG,"updateAutomaticClosingData--send="+send);
                SendHelperLeft.handler(send);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 更新中央门锁反馈
     *
     * @date： 2023/10/17
     * @author: 78495
     */
    private void updateDoorLockResponseData(boolean selected) {
        mExecutor.execute(() -> {
            CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getContext(), deviceId);
            carSetupTable.setCenterDoorLock(selected);
            CarSetupRepository.getInstance().saveData(getContext(), deviceId, carSetupTable);
            String send;
            if (selected) {
                send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.DOOR_LOCK.getTypeValue() + "01" + SerialPortDataFlag.END_FLAG;
            } else {
                send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.DOOR_LOCK.getTypeValue() + "00" + SerialPortDataFlag.END_FLAG;
            }
            SendHelperLeft.handler(send);
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_common_list;
    }

    @Override
    protected void setupData() {
        super.setupData();
        deviceId = AppUtils.getDeviceId(getContext());

        mExecutor.execute(() -> {
            CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getContext(), deviceId);
            LogUtils.printI(TAG, "setupData--carSetupTable="+carSetupTable);
            if (carSetupTable != null) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            List<CommonItem> list = new ArrayList<>();
                            list.add(new CommonItem(getResources().getString(R.string.central_door_lock_feedback), carSetupTable.isCenterDoorLock()));
                            list.add(new CommonItem(getResources().getString(R.string.automatic_closing), carSetupTable.isAutomaticRarFolding()));
                            list.add(new CommonItem(getResources().getString(R.string.rearview_mirror_down), carSetupTable.isRearviewMirrorDown()));
                            list.add(new CommonItem(getResources().getString(R.string.suitcase_opening_limit), carSetupTable.isTrunkOpenLimit()));
                            list.add(new CommonItem(getResources().getString(R.string.headrest_recline), carSetupTable.isHeadrestRecline()));
                            list.add(new CommonItem(getResources().getString(R.string.seat_belt_assist), carSetupTable.isSeatBeltAssist()));
                            mAdapter.setNewData(list);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}
