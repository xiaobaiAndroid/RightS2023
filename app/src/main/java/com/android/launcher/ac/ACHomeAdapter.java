package com.android.launcher.ac;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @date： 2023/10/13
 * @author: 78495
*/
public class ACHomeAdapter extends FragmentStateAdapter {

    private List<Fragment>  list;

    public ACHomeAdapter(FragmentActivity activity, List<Fragment> list) {
        super(activity);
        this.list = list;
    }

    @Override
    public Fragment createFragment(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
