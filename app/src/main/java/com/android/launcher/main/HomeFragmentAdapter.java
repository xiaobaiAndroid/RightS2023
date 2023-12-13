package com.android.launcher.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.launcher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @date： 2023/10/12
 * @author: 78495
 */
public class HomeFragmentAdapter extends FragmentStateAdapter {

    private List<HomeItemFragment> fragmentList;

    private Context context;

    public HomeFragmentAdapter(Fragment fragment, List<HomeItemFragment> fragmentList) {
        super(fragment);
        this.context = fragment.getContext();
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment createFragment(int position) {
        HomeItemFragment homeItemFragment = fragmentList.get(position);

        Bundle bundle = new Bundle();
        ArrayList<HomeItem> homeItems = new ArrayList<>();
        if(position == 0){
            homeItems.add(new HomeItem(HomeItem.Type.MAP,R.drawable.right_nav,context.getResources().getString(R.string.map),false));
            homeItems.add(new HomeItem(HomeItem.Type.MEDIA,R.drawable.right_music,context.getResources().getString(R.string.music),false));
            homeItems.add(new HomeItem(HomeItem.Type.FM,R.drawable.right_fm,context.getResources().getString(R.string.fm),false));
        }else if(position == 1){
            homeItems.add(new HomeItem(HomeItem.Type.SETTING,R.drawable.right_setting,context.getResources().getString(R.string.setting),false));
            homeItems.add(new HomeItem(HomeItem.Type.PHONE,R.drawable.right_phone,context.getResources().getString(R.string.phone),false));
            homeItems.add(new HomeItem(HomeItem.Type.APPS,R.drawable.ic_apps,context.getResources().getString(R.string.apps),false));
        }
//        else if(position == 2){
//            homeItems.add(new HomeItem(HomeItem.Type.INFO,R.drawable.right_info,context.getResources().getString(R.string.info),false));
//            homeItems.add(new HomeItem(HomeItem.Type.COMFORT,R.drawable.ic_phone,context.getResources().getString(R.string.mobile_phone),false));
//        }
        bundle.putParcelableArrayList("homeitems",homeItems);

        homeItemFragment.setArguments(bundle);
        return homeItemFragment;
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
