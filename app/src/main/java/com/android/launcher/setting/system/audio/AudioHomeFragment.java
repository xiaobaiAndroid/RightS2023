package com.android.launcher.setting.system.audio;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import module.common.MessageEvent;

import com.android.launcher.common.CommonCarAdapter;
import com.android.launcher.common.CommonItem;
import com.android.launcher.view.LinearSpaceDecoration;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;

import module.common.utils.DensityUtil;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * 音频
 * @date： 2023/10/18
 * @author: 78495
*/
public class AudioHomeFragment extends FragmentBase {

    private CommonCarAdapter mAdapter;

    private BasePopupView volumeDialog;
    private BasePopupView alarmSetDialog;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        RecyclerView contentRV = view.findViewById(R.id.contentRV);
        contentRV.setLayoutManager(new LinearLayoutManager(getContext()));

        List<CommonItem> list = new ArrayList<>();
        list.add(new CommonItem(getResources().getString(R.string.volume_setting), false,true));
        list.add(new CommonItem(getResources().getString(R.string.alarm_volume_setting), false,true));
        mAdapter = new CommonCarAdapter(list);
        contentRV.setAdapter(mAdapter);

        LinearSpaceDecoration linearSpaceDecoration = new LinearSpaceDecoration(mAdapter, (int) getResources().getDimension(R.dimen.item_space));
        linearSpaceDecoration.setmDrawLastItem(false);
        contentRV.addItemDecoration(linearSpaceDecoration);

        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if(position == 0){
                showVolumeDialog();
            }else if(position == 1){
                showAlarmSetDialog();
            }
        });
    }

    private void showAlarmSetDialog() {
        alarmSetDialog = new XPopup.Builder(getActivity())
                .isViewMode(true)
                .hasShadowBg(true)
                .dismissOnBackPressed(true)
                .dismissOnTouchOutside(true)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .asCustom(new AlarmSetDialog(getActivity()))
                .show();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_common_list;
    }


    private void showVolumeDialog() {
        volumeDialog = new XPopup.Builder(getActivity())
                .isViewMode(true)
                .hasShadowBg(true)
                .dismissOnBackPressed(true)
                .dismissOnTouchOutside(true)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .asCustom(new VolumeSetDialog(getActivity()))
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(volumeDialog!=null){
            volumeDialog.dismiss();
            volumeDialog = null;
        }
        if(alarmSetDialog!=null){
            alarmSetDialog.dismiss();
            alarmSetDialog = null;
        }
    }
}
