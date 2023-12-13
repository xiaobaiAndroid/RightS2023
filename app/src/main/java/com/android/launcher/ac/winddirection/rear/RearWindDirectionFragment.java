package com.android.launcher.ac.winddirection.rear;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.launcher.R;
import com.android.launcher.ac.controller.ACAdjustView;
import com.android.launcher.ac.temp.TempUtils;
import com.android.launcher.ac.wind.WindUtils;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.entity.BinaryEntity;
import module.common.utils.IconUtils;
import com.bzf.module_db.entity.CanBCTable;
import com.bzf.module_db.repository.CanBCRepository;

import java.util.ArrayList;
import java.util.List;

import module.common.MessageEvent;
import module.common.utils.AppUtils;
import module.common.utils.LogUtils;

/**
 * 后空调气流控制
 *
 * @date： 2023/10/13
 * @author: 78495
 */
public class RearWindDirectionFragment extends FragmentBase {

    private RearWindDirectionAdapter leftAirflowAdapter = new RearWindDirectionAdapter(new ArrayList<>());
    private RearWindDirectionAdapter rightAirflowAdapter = new RearWindDirectionAdapter(new ArrayList<>());
    private RecyclerView leftContentRV;
    private RecyclerView rightContentRV;

    private int leftCurrentPosition = 0;
    private int rightCurrentPosition = 0;
    private ACAdjustView leftAcAdjustV;
    private ACAdjustView rightAcAdjustV;
    private String deviceId;

    private RearAcCmdSender cmdSender;
    private ImageView autoModeIV;
    private ImageView acOffIV;

    private boolean acOff = true;
    private boolean autoMode = false;


    @Override
    public void disposeMessageEvent(MessageEvent event) {
        if (event.type == MessageEvent.Type.UPDATE_REAR_AC_DATA) {
            if (event.data instanceof CanBCTable) {
                CanBCTable table = (CanBCTable) event.data;
                updateDataToView(table);
            }
        }
    }

    private void updateDataToView(CanBCTable table) {
        if (table == null) {
            return;
        }
        if (leftAcAdjustV == null || rightAcAdjustV == null) {
            return;
        }

        String status = table.getStatus();
        if (status != null && status.length() == 2) {
            BinaryEntity statusBinary = new BinaryEntity(status);
            LogUtils.printI(TAG, "updateDataToView---statusBinary=" + statusBinary);

            autoMode = false;
            //后空调关闭
            if (BinaryEntity.Value.NUM_1.getValue().equals(statusBinary.getB5())) {
                acOff = true;
                leftAcAdjustV.setAccOff(true);
                rightAcAdjustV.setAccOff(true);
                resetAdapterData(leftAirflowAdapter);
                resetAdapterData(rightAirflowAdapter);
            } else {
                acOff = false;
                leftAcAdjustV.setAccOff(false);
                rightAcAdjustV.setAccOff(false);
                if (BinaryEntity.Value.NUM_1.getValue().equals(statusBinary.getB0())) {
                    //后空调自动模式
                    leftAcAdjustV.setAuto(true);
                    rightAcAdjustV.setAuto(true);
                    autoMode = true;
                    resetAdapterData(leftAirflowAdapter);
                    resetAdapterData(rightAirflowAdapter);
                    leftAirflowAdapter.getItem(0).setSelected(true);
                    leftAirflowAdapter.notifyItemChanged(0);

                    rightAirflowAdapter.getItem(0).setSelected(true);
                    rightAirflowAdapter.notifyItemChanged(0);

                } else if (BinaryEntity.Value.NUM_1.getValue().equals(statusBinary.getB2())) {
                    //风速自动模式
                    leftAcAdjustV.setAuto(true);
                    rightAcAdjustV.setAuto(true);
                } else if (BinaryEntity.Value.NUM_1.getValue().equals(statusBinary.getB3())) {
                    //左侧风速自动模式
                    leftAcAdjustV.setAuto(true);
                    rightAcAdjustV.setAuto(false);
                    airflowDataToView(table);
                } else if (BinaryEntity.Value.NUM_1.getValue().equals(statusBinary.getB4())) {
                    //右侧风速自动模式
                    leftAcAdjustV.setAuto(false);
                    rightAcAdjustV.setAuto(true);
                    airflowDataToView(table);
                } else if (BinaryEntity.Value.NUM_1.getValue().equals(statusBinary.getB6())) {
                    //REST(余温加热)下的自动模式
                    leftAcAdjustV.setAuto(true);
                    rightAcAdjustV.setAuto(true);
                    autoMode = true;

                    leftAirflowAdapter.getItem(0).setSelected(true);
                    leftAirflowAdapter.notifyItemChanged(0);

                    rightAirflowAdapter.getItem(0).setSelected(true);
                    rightAirflowAdapter.notifyItemChanged(0);
                } else {
                    leftAcAdjustV.setAuto(false);
                    rightAcAdjustV.setAuto(false);

                    leftAcAdjustV.setTemp(TempUtils.cmdToTemp(table.getLefttemp()));
                    leftAcAdjustV.setWind(WindUtils.cmdToNumber(table.getWind()));
                    rightAcAdjustV.setTemp(TempUtils.cmdToTemp(table.getRighttemp()));
                    rightAcAdjustV.setWind(WindUtils.cmdToNumber(table.getWind()));

                    airflowDataToView(table);
                }
            }
            setAcOffImageIsSelected(acOff);
            if(!acOff){
                setAutoImageIsSelected(autoMode);
            }
        }

    }

    private void setAcOffImageIsSelected(boolean isSelected) {
        if(isSelected){
            IconUtils.setColor(acOffIV,getContext().getResources().getColor(R.color.cl_tab_unselected));
        }else{
            IconUtils.setColor(acOffIV,getContext().getResources().getColor(R.color.cl_ffffff));
        }

    }

    private void setAutoImageIsSelected(boolean isSelected) {
        if(isSelected){
            IconUtils.setColor(autoModeIV,getContext().getResources().getColor(R.color.cl_ffffff));
        }else{
            IconUtils.setColor(autoModeIV,getContext().getResources().getColor(R.color.cl_tab_unselected));
        }
    }

    private void airflowDataToView(CanBCTable table) {
        String winddic = table.getWinddic();
        if (winddic != null && winddic.length() == 2) {
            String leftAirflow = winddic.substring(0, 1);
            String rightAirflow = winddic.substring(1, 2);

            resetAdapterData(leftAirflowAdapter);
            resetAdapterData(rightAirflowAdapter);
            leftCurrentPosition = updateAirflowItemSelected(leftAirflow, leftAirflowAdapter);
            rightCurrentPosition = updateAirflowItemSelected(rightAirflow, rightAirflowAdapter);
        }
    }

    private int updateAirflowItemSelected(String airflow, RearWindDirectionAdapter rearWindDirectionAdapter) {
        for (int i = 0; i < rearWindDirectionAdapter.getData().size(); i++) {
            RearWindDirectionItem adapterItem = rearWindDirectionAdapter.getItem(i);
            if (adapterItem != null) {
                if (adapterItem.getCmdValue().getValue().equals(airflow)) {
                    adapterItem.setSelected(true);
                    rearWindDirectionAdapter.notifyItemChanged(i);
                    return i;
                }
            }
        }
        return 0;
    }

    private void resetAdapterData(RearWindDirectionAdapter frontAirflowAdapter) {
        for (int i = 0; i < frontAirflowAdapter.getItemCount(); i++) {
            RearWindDirectionItem item = frontAirflowAdapter.getItem(i);
            if (item != null) {
                item.setSelected(false);
                frontAirflowAdapter.notifyItemChanged(i);
            }
        }
    }

    @Override
    protected void setupData() {
        super.setupData();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        cmdSender = new RearAcCmdSender(getContext());

        leftContentRV = view.findViewById(R.id.leftContentRV);
        rightContentRV = view.findViewById(R.id.rightContentRV);
        leftContentRV.setLayoutManager(new LinearLayoutManager(getContext()));
        rightContentRV.setLayoutManager(new LinearLayoutManager(getContext()));

        leftContentRV.setItemAnimator(null);
        rightContentRV.setItemAnimator(null);

        leftAcAdjustV = view.findViewById(R.id.leftAcAdjustV);
        rightAcAdjustV = view.findViewById(R.id.rightAcAdjustV);

        autoModeIV = view.findViewById(R.id.autoModeIV);
        acOffIV = view.findViewById(R.id.acOffIV);

        leftAirflowAdapter = new RearWindDirectionAdapter(getAirflowList());
        leftAirflowAdapter.getItem(leftCurrentPosition).setSelected(true);
        leftContentRV.setAdapter(leftAirflowAdapter);

        rightAirflowAdapter = new RearWindDirectionAdapter(getAirflowList());
        rightAirflowAdapter.getItem(rightCurrentPosition).setSelected(true);

        rightContentRV.setAdapter(rightAirflowAdapter);


        leftAirflowAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if(!acOff){
                changeLeftWindDirection(position);
            }

        });

        rightAirflowAdapter.setOnItemClickListener((adapter, view12, position) -> {
            if(!acOff) {
                changeRightWindDirection(position);
            }
        });

        autoModeIV.setOnClickListener(v -> {
            if(!acOff){
                autoMode = !autoMode;
                openAutoMode(autoMode);
            }
        });
        acOffIV.setOnClickListener(v -> {
            acOff = !acOff;
            closeAC(acOff);
        });


        IconUtils.setColor(acOffIV,getContext().getResources().getColor(R.color.cl_tab_unselected));
        IconUtils.setColor(autoModeIV,getContext().getResources().getColor(R.color.cl_tab_unselected));

        setAcAdjustListener();

        deviceId = AppUtils.getDeviceId(getContext());
        loadData();
    }

    private void closeAC(boolean isClose) {
        try {
            setAcOffImageIsSelected(isClose);
            setAutoImageIsSelected(!isClose);
            leftAcAdjustV.setAccOff(isClose);
            rightAcAdjustV.setAccOff(isClose);
            if(isClose){
                resetAdapterData(leftAirflowAdapter);
                resetAdapterData(rightAirflowAdapter);
            }else{
                leftAirflowAdapter.getItem(leftCurrentPosition).setSelected(true);
                leftAirflowAdapter.notifyItemChanged(leftCurrentPosition);

                rightAirflowAdapter.getItem(rightCurrentPosition).setSelected(true);
                rightAirflowAdapter.notifyItemChanged(rightCurrentPosition);
            }
            mExecutor.execute(() -> cmdSender.sendAcOffCmd(isClose));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        mExecutor.execute(() -> {
            try {
                CanBCTable table = CanBCRepository.getInstance().getData(getContext(), deviceId);
                if (table != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> updateDataToView(table));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void openAutoMode(boolean isOpen) {
        setAutoImageIsSelected(isOpen);
        leftAcAdjustV.setAccOff(false);
        rightAcAdjustV.setAccOff(false);
        if(isOpen){
            resetAdapterData(leftAirflowAdapter);

            RearWindDirectionItem leftAirflowAdapterItem = leftAirflowAdapter.getItem(0);
            if(leftAirflowAdapterItem!=null){
                leftAirflowAdapterItem.setSelected(true);
                leftAirflowAdapter.notifyItemChanged(0);
            }

            resetAdapterData(rightAirflowAdapter);
            RearWindDirectionItem rightAirflowAdapterItem = rightAirflowAdapter.getItem(0);
            if(rightAirflowAdapterItem!=null){
                rightAirflowAdapterItem.setSelected(true);
                rightAirflowAdapter.notifyItemChanged(0);
            }

            leftAcAdjustV.setAuto(true);
            rightAcAdjustV.setAuto(true);
        }else{
            leftAcAdjustV.setAuto(false);
            rightAcAdjustV.setAuto(false);
        }

        mExecutor.execute(() -> {
            if(cmdSender!=null){
                cmdSender.sendAutoModeCmd(isOpen);
            }
        });
    }

    private void changeRightWindDirection(int position) {
        if (rightCurrentPosition != position) {
            rightAirflowAdapter.getItem(rightCurrentPosition).setSelected(false);
            rightAirflowAdapter.notifyItemChanged(rightCurrentPosition + rightAirflowAdapter.getHeaderLayoutCount());

            RearWindDirectionItem airflowItem = rightAirflowAdapter.getItem(position);
            if(airflowItem!=null){
                airflowItem.setSelected(true);
                rightAirflowAdapter.notifyItemChanged(position);
                rightCurrentPosition = position;

                mExecutor.execute(() -> cmdSender.sendRightWindDirectionCmd(airflowItem.getCmdValue()));
            }
        }
    }



    private void changeLeftWindDirection(int position) {
        LogUtils.printI(TAG, "leftAirflowAdapter---position=" + position + ", leftCurrentPosition=" + leftCurrentPosition);
        if (leftCurrentPosition != position) {
            leftAirflowAdapter.getItem(leftCurrentPosition).setSelected(false);
            leftAirflowAdapter.notifyItemChanged(leftCurrentPosition);

            RearWindDirectionItem airflowAdapterItem = leftAirflowAdapter.getItem(position + leftAirflowAdapter.getHeaderLayoutCount());

            if(airflowAdapterItem!=null){
                airflowAdapterItem.setSelected(true);
                leftAirflowAdapter.notifyItemChanged(position);
                leftCurrentPosition = position;

                mExecutor.execute(() -> cmdSender.sendLeftWindDirectionCmd(airflowAdapterItem.getCmdValue()));
            }
        }
    }


    private void setAcAdjustListener() {
        leftAcAdjustV.setTouchListener(new ACAdjustView.TouchListener() {
            @Override
            public void onWindIncrease(int wind) {
                setLeftWind(wind);
            }

            @Override
            public void onWindReduce(int wind) {
                setLeftWind(wind);
            }

            @Override
            public void onTempIncrease(String temp) {
                setLeftTemp(temp);
            }

            @Override
            public void onTempReduce(String temp) {
                setLeftTemp(temp);
            }
        });

        rightAcAdjustV.setTouchListener(new ACAdjustView.TouchListener() {
            @Override
            public void onWindIncrease(int wind) {
                setRightWind(wind);
            }

            @Override
            public void onWindReduce(int wind) {
                setRightWind(wind);
            }

            @Override
            public void onTempIncrease(String temp) {
                setRightTemp(temp);
            }

            @Override
            public void onTempReduce(String temp) {
                setRightTemp(temp);
            }
        });
    }

    private void setRightTemp(String temp) {
        if (!acOff && !autoMode && mExecutor != null) {
            mExecutor.execute(() -> {
                if (cmdSender != null) {
                    cmdSender.sendSetRightTempCmd(TempUtils.tempToCmd(temp));
                }
            });
        }
    }

    private void setLeftTemp(String temp) {
        if (!acOff && !autoMode && mExecutor != null) {
            mExecutor.execute(() -> {
                if (cmdSender != null) {
                    cmdSender.sendSetLeftTempCmd(TempUtils.tempToCmd(temp));
                }
            });
        }
    }

    private void setRightWind(int wind) {
        leftAcAdjustV.setWind(wind);
        if (!acOff && !autoMode && mExecutor != null) {
            mExecutor.execute(() -> {
                if (cmdSender != null) {
                    cmdSender.sendSetWindCmd(WindUtils.numberToCmd(wind));
                }
            });
        }
    }

    private void setLeftWind(int wind) {
        rightAcAdjustV.setWind(wind);
        if (!acOff && !autoMode && mExecutor != null) {
            mExecutor.execute(() -> {
                if (cmdSender != null) {
                    cmdSender.sendSetWindCmd(WindUtils.numberToCmd(wind));
                }
            });
        }
    }

    private List<RearWindDirectionItem> getAirflowList() {
        List<RearWindDirectionItem> list = new ArrayList<>();

        list.add(new RearWindDirectionItem(false, RearWindDirectionCmdValue.AUTO, RearWindDirectionItem.Type.AUTO));
        list.add(new RearWindDirectionItem(R.drawable.car_air_leg, false, RearWindDirectionCmdValue.KNEE, RearWindDirectionItem.Type.NORMAL));
        list.add(new RearWindDirectionItem(R.drawable.car_air_back_knee, false, RearWindDirectionCmdValue.LEG, RearWindDirectionItem.Type.NORMAL));
        list.add(new RearWindDirectionItem(R.drawable.car_air_foot, false, RearWindDirectionCmdValue.FOOT, RearWindDirectionItem.Type.NORMAL));
        return list;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_rear_airflow;
    }
}
