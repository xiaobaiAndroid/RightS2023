package com.android.launcher.setting.system.display;

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
import com.android.launcher.view.LinearSpaceDecoration;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public class SystemDisplayHomeFragment extends FragmentBase {

    private CommonCarAdapter mAdapter;

    private int currentPosition = 0;
    private BasePopupView centralDisplayDialog;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        RecyclerView contentRV = view.findViewById(R.id.contentRV);
        contentRV.setLayoutManager(new LinearLayoutManager(getContext()));

        List<CommonItem> list = new ArrayList<>();
        list.add(new CommonItem(getResources().getString(R.string.central_display), false,true));
        mAdapter = new CommonCarAdapter(list);
        contentRV.setAdapter(mAdapter);

        LinearSpaceDecoration linearSpaceDecoration = new LinearSpaceDecoration(mAdapter, (int) getResources().getDimension(R.dimen.item_space));
        linearSpaceDecoration.setmDrawLastItem(false);
        contentRV.addItemDecoration(linearSpaceDecoration);

        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if(position == 0){
                showCentralDisplayDialog();
            }
        });
    }

    private void showCentralDisplayDialog() {
        centralDisplayDialog = new XPopup.Builder(getActivity())
                .isViewMode(true)
                .hasShadowBg(true)
                .dismissOnBackPressed(true)
                .dismissOnTouchOutside(true)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .asCustom(new CentralDisplaySetDialog(getActivity()))
                .show();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_common_list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(centralDisplayDialog!=null){
            centralDisplayDialog.dismiss();
            centralDisplayDialog = null;
        }
    }
}
