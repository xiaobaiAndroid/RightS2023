package com.android.launcher.main;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import module.common.MessageEvent;

import com.android.launcher.MainActivity;
import com.android.launcher.MyApp;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.view.GridSpaceDecoration;

import org.greenrobot.eventbus.EventBus;

import module.common.type.AppType;
import module.common.utils.DensityUtil;
import module.common.utils.FMHelper;
import module.common.utils.GaodeCarMapHelper;

import java.util.ArrayList;

/**
 * @date： 2023/10/12
 * @author: 78495
*/
public class HomeItemFragment extends FragmentBase {

    private RecyclerView contentRV;
    private HomeItemAdapter homeItemAdapter = new HomeItemAdapter(new ArrayList<>());

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {


        contentRV = view.findViewById(R.id.titleRV);
        contentRV.setLayoutManager(new GridLayoutManager(getContext(),3));

        Bundle arguments = getArguments();
        ArrayList<HomeItem> homeItem = arguments.getParcelableArrayList("homeitems");
        homeItemAdapter.setNewData(homeItem);
        contentRV.setAdapter(homeItemAdapter);

        homeItemAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if(position < 0 ||  position > homeItemAdapter.getData().size()-1){
                return;
            }
            HomeItem item = homeItemAdapter.getItem(position);
            if(item!=null){
                openFragment(item);
            }
        });
    }

    private void openFragment(HomeItem item) {
        if(item.getType() == HomeItem.Type.MAP){
//            GaodeCarMapHelper.start(getContext());
            if(MainActivity.isNaving){
                MainActivity activity = (MainActivity) getActivity();
                activity.toMapNavFragment();
            }else{
                MainActivity activity = (MainActivity) getActivity();
                activity.toMapFragment();
            }

        }else if(item.getType() == HomeItem.Type.FM){
            FMHelper.startFM();
            MyApp.currentActivityStr = AppType.FM.name();
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_STATUS_BAR_VIEW));
        }else if(item.getType() == HomeItem.Type.APPS){
            MainActivity activity = (MainActivity) getActivity();
            activity.toAppsListFragment();
        }else if(item.getType() == HomeItem.Type.PHONE){
            MainActivity activity = (MainActivity) getActivity();
            activity.toPhoneFragment();
        }else if(item.getType() == HomeItem.Type.MEDIA){
            MainActivity activity = (MainActivity) getActivity();
            activity.toMediaFragment();
        }else if(item.getType() == HomeItem.Type.SETTING){
            MainActivity activity = (MainActivity) getActivity();
            activity.toSettingFragment();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_home_item;
    }


}
