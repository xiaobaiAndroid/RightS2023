package com.android.launcher.common;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * @date： 2023/10/16
 * @author: 78495
*/
public class CommonFragmentAdapter extends FragmentStateAdapter {

    private List<Fragment> mList;

    public CommonFragmentAdapter(FragmentActivity fragmentActivity, List<Fragment> list) {
        super(fragmentActivity);
        this.mList = list;
    }

    public CommonFragmentAdapter(Fragment fragment, List<Fragment> list) {
        super(fragment);
        this.mList = list;
    }



    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = mList.get(position);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
