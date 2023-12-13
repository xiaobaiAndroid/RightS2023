package com.android.launcher.setting.car.activate;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import module.common.MessageEvent;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.common.CommonItem;
import com.android.launcher.common.CommonCarAdapter;
import module.common.utils.DensityUtil;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;
import com.android.launcher.view.LinearSpaceDecoration;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动匹配激活
 *
 * @date： 2023/10/17
 * @author: 78495
 */
public class MatchActivateFragment extends FragmentBase {

    private CommonCarAdapter activateAdapter = new CommonCarAdapter(new ArrayList<>());

    private int currentPosition = -1;

    private List<CommonItem> modes = new ArrayList<>();

    private boolean activateLoadingRunning = false;
    private boolean activateWaitConfirm = false;

    private ActivateSelectDialog activateSelectDialog;
    private BasePopupView loadingDialog;

    private boolean isLastIndex = false;
    private RecyclerView contentRV;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

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
                startActivate(currentPosition);
                contentRV.scrollToPosition(currentPosition);
            }
        });
    }


    private void startActivate(int index) {
        showLoadingDialog();
        mExecutor.execute(() -> {
            switch (index) {
                case 0:
                    getSelectMode();
                    break;
                case 1:
                    CDActivateModeList.mode();
                    break;
                case 2:
                    CDActivateModeList.mode1();
                    break;
                case 3:
                    CDActivateModeList.mode2();
                    break;
                case 4:
                    CDActivateModeList.mode3();
                    break;
                case 5:
                    CDActivateModeList.mode4();
                    break;
                case 6:
                    CDActivateModeList.mode5();
                    break;
                case 7:
                    CDActivateModeList.mode6();
                    break;
                case 8:
                    CDActivateModeList.mode7();
                    break;
                case 9:
                    CDActivateModeList.mode8();
                    break;
                case 10:
                    CDActivateModeList.mode9();
                    break;
                case 11:
                    CDActivateModeList.mode10();
                    break;
                case 12:
                    CDActivateModeList.mode11();
                    break;
                case 13:
                    CDActivateModeList.mode12();
                    break;
                case 14:
                    CDActivateModeList.mode13();
                    break;
                case 15:
                    CDActivateModeList.mode14();
                    break;
                case 16:
                    CDActivateModeList.mode15();
                    break;
                case 17:
                    CDActivateModeList.mode16();
                    break;
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> closeLoadingDialog());
            }
        });
    }


    private void getSelectMode() {
        LogUtils.printI(TAG, "getSelectMode-----currentPosition=" + currentPosition);
        switch (currentPosition) {
            case 1:
                CDActivateModeList.mode();
                break;
            case 2:
                CDActivateModeList.mode1();
                break;
            case 3:
                CDActivateModeList.mode2();
                break;
            case 4:
                CDActivateModeList.mode3();
                break;
            case 5:
                CDActivateModeList.mode4();
                break;
            case 6:
                CDActivateModeList.mode5();
                break;
            case 7:
                CDActivateModeList.mode6();
                break;
            case 8:
                CDActivateModeList.mode7();
                break;
            case 9:
                CDActivateModeList.mode8();
                break;
            case 10:
                CDActivateModeList.mode9();
                break;
            case 11:
                CDActivateModeList.mode10();
                break;
            case 12:
                CDActivateModeList.mode11();
                break;
            case 13:
                CDActivateModeList.mode12();
                break;
            case 14:
                CDActivateModeList.mode13();
                break;
            case 15:
                CDActivateModeList.mode14();
                break;
            case 16:
                CDActivateModeList.mode15();
                break;
            case 17:
                CDActivateModeList.mode16();
                break;
            default:
                ToastUtils.showShort(getResources().getString(R.string.not_activated));
                break;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_common_list;
    }

    @Override
    protected void setupData() {
        super.setupData();

        modes.clear();

        mExecutor.execute(() -> {

            int saveSelectIndex = SPUtils.getInt(getContext(), SPUtils.SP_SELECT_MODE, -1);
            if (saveSelectIndex != -1) {
                currentPosition = saveSelectIndex;
                CommonItem commonItem = new CommonItem(getResources().getString(R.string.activated) + getSelectIndexName(saveSelectIndex), false);
                commonItem.setGoneResId(true);
                modes.add(commonItem);
            } else {
                currentPosition = -1;
                CommonItem commonItem = new CommonItem(getResources().getString(R.string.activated) + getResources().getString(R.string.not_activated), false);
                commonItem.setGoneResId(true);
                modes.add(commonItem);
            }

            LogUtils.printI(TAG, "selectMode-----------------saveSelectIndex=" + saveSelectIndex);

//            if (saveSelectIndex == -1) {
//                modes.add(new CommonItem(getResources().getString(R.string.fiber_optic_activation), true));
//            } else {
//                modes.add(new CommonItem(getResources().getString(R.string.fiber_optic_activation), false));
//            }
            modes.add(new CommonItem(getResources().getString(R.string.fiber_optic_activation), false));

            String activate = getResources().getString(R.string.activate);

            modes.add(new CommonItem("AUX" + activate + "1.0", false));
            modes.add(new CommonItem("AUX" + activate + "1.1", false));
            modes.add(new CommonItem("AUX" + activate + "1.2", false));
            modes.add(new CommonItem("AUX" + activate + "1.3", false));
            modes.add(new CommonItem("AUX" + activate + "1.4", false));
            modes.add(new CommonItem("AUX" + activate + "1.5", false));
            modes.add(new CommonItem("AUX" + activate + "1.6", false));
            modes.add(new CommonItem("AUX" + activate + "2.0", false));
            modes.add(new CommonItem("AUX" + activate + "2.1", false));
            modes.add(new CommonItem("AUX" + activate + "2.2", false));
            modes.add(new CommonItem("AUX" + activate + "2.3", false));
            modes.add(new CommonItem("AUX" + activate + "2.4", false));
            modes.add(new CommonItem("AUX" + activate + "2.5", false));
            modes.add(new CommonItem("AUX" + activate + "2.6", false));
            modes.add(new CommonItem("AUX" + activate + "2.7", false));
            modes.add(new CommonItem("AUX" + activate + "2.8", false));

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (activateAdapter != null) {
                        activateAdapter.setNewData(modes);

                        if (currentPosition > 0) {
                            activateAdapter.getItem(currentPosition).setSelected(true);
                            activateAdapter.notifyItemChanged(currentPosition);
                        }
                    }
                });
            }
        });

    }


    private String getSelectIndexName(int selectIndex) {
        String activate = getResources().getString(R.string.activate);

        String name = getResources().getString(R.string.not_activated);
        switch (selectIndex) {
            case 1:
                name = getResources().getString(R.string.fiber_optic_activation);
                break;
            case 2:
                name = "AUX" + activate + "1.0";
                break;
            case 3:
                name = "AUX" + activate + "1.1";
                break;
            case 4:
                name = "AUX" + activate + "1.2";
                break;
            case 5:
                name = "AUX" + activate + "1.3";
                break;
            case 6:
                name = "AUX" + activate + "1.4";
                break;
            case 7:
                name = "AUX" + activate + "1.5";
                break;
            case 8:
                name = "AUX" + activate + "1.6";
                break;
            case 9:
                name = "AUX" + activate + "2.0";
                break;
            case 10:
                name = "AUX" + activate + "2.1";
            case 11:
                name = "AUX" + activate + "2.2";
                break;
            case 12:
                name = "AUX" + activate + "2.3";
                break;
            case 13:
                name = "AUX" + activate + "2.4";
                break;
            case 14:
                name = "AUX" + activate + "2.5";
                break;
            case 15:
                name = "AUX" + activate + "2.6";
                break;
            case 16:
                name = "AUX" + activate + "2.7";
                break;
            case 17:
                name = "AUX" + activate + "2.8";
                break;
            default:
                break;
        }
        return name;
    }

    private void closeLoadingDialog() {
        try {
            getActivity().runOnUiThread(() -> {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                    loadingDialog = null;
                }

                if (isLastIndex) {
                    ToastUtils.showShort(getResources().getString(R.string.in_the_end_there_is_no_more));
                    selectCancel();
                    return;
                }

                activateWaitConfirm = true;

                activateSelectDialog = new ActivateSelectDialog(getActivity(), new ActivateSelectDialog.Listener() {
                    @Override
                    public void onConfirm() {
                        confirmActivate();
                    }

                    @Override
                    public void onNext() {
                        activateNext();
                    }

                    @Override
                    public void onPrevious() {
                        activatePrevious();
                    }

                    @Override
                    public void cancel() {
                        selectCancel();
                    }
                });

                loadingDialog = new XPopup.Builder(getActivity())
                        .isViewMode(true)
                        .dismissOnBackPressed(false)
                        .dismissOnTouchOutside(false)
                        .asCustom(activateSelectDialog)
                        .show();

            });
        } catch (Exception e) {
            e.printStackTrace();
            activateLoadingRunning = false;
        }
    }


    public void confirmActivate() {
        try {
            if (currentPosition > 0) {
                LogUtils.printI(TAG,"confirmActivate----position="+currentPosition);
                mExecutor.execute(() -> SPUtils.putInt(getContext(), SPUtils.SP_SELECT_MODE, currentPosition));

                CommonItem commonItem = activateAdapter.getItem(0);
                commonItem.setTitle(getResources().getString(R.string.activated) + getSelectIndexName(currentPosition));
                activateAdapter.notifyItemChanged(0);
            }
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
            activateSelectDialog = null;

            activateLoadingRunning = false;
            activateWaitConfirm = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 激活下一个
     * @createDate: 2023/8/19
     */
    private void activateNext() {
        try {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
            activateSelectDialog = null;

            activateLoadingRunning = false;
            activateWaitConfirm = false;

            if (currentPosition != -1) {
                activateAdapter.getItem(currentPosition).setSelected(false);
                activateAdapter.notifyItemChanged(currentPosition);
            }

            currentPosition++;
            if (currentPosition <= (activateAdapter.getData().size() - 1)) {
                isLastIndex = false;
                activateAdapter.getItem(currentPosition).setSelected(true);
                activateAdapter.notifyItemChanged(currentPosition);
                startActivate(currentPosition);
                contentRV.scrollToPosition(currentPosition);
            } else {
                isLastIndex = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void selectCancel() {
        try {
            activateLoadingRunning = false;
            activateWaitConfirm = false;
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
            activateSelectDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 激活上一个
     * @createDate: 2023/8/23
     */
    private void activatePrevious() {
        try {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
            activateSelectDialog = null;

            activateLoadingRunning = false;
            activateWaitConfirm = false;

            if (currentPosition != -1) {
                activateAdapter.getItem(currentPosition).setSelected(false);
                activateAdapter.notifyItemChanged(currentPosition);
            }

            currentPosition--;
            if (currentPosition >= 1) {
                isLastIndex = false;
                activateAdapter.getItem(currentPosition).setSelected(true);
                activateAdapter.notifyItemChanged(currentPosition);
                startActivate(currentPosition);
                contentRV.scrollToPosition(currentPosition);
            } else {
                isLastIndex = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoadingDialog() {
        try {
            activateWaitConfirm = false;
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
            loadingDialog = new XPopup.Builder(getActivity())
                    .isViewMode(true)
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .asLoading(getResources().getString(R.string.activate_running))
                    .show();

            activateLoadingRunning = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
