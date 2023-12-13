package com.android.launcher.music;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.launcher.MainActivity;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.common.CommonFragmentAdapter;
import com.android.launcher.music.bluetooth.BluetoothMusicFragment;
import com.android.launcher.music.usb.USBMusicFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import module.common.MessageEvent;
import module.common.entity.MyTabEntity;
import module.common.utils.LogUtils;

/**
 * @date： 2023/11/14
 * @author: 78495
*/
public class MusicHomeFragment extends FragmentBase {

    private CommonFragmentAdapter fragmentAdapter;
    private ViewPager2 viewPager;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        CommonTabLayout tabLayout = view.findViewById(R.id.tabLayout);

        viewPager = view.findViewById(R.id.viewPager);

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setUserInputEnabled(false);

        List<Fragment> list = new ArrayList<>();
        list.add(new BluetoothMusicFragment());
        list.add(new USBMusicFragment());
        fragmentAdapter = new CommonFragmentAdapter(this,list);
        viewPager.setAdapter(fragmentAdapter);

        ArrayList<CustomTabEntity> tabs = new ArrayList<>();
        tabs.add(new MyTabEntity(getResources().getString(R.string.bluetooth)));
        tabs.add(new MyTabEntity(getResources().getString(R.string.usb)));
        tabLayout.setTabData(tabs);

        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                LogUtils.printI(TAG,"onTabSelect----position="+position);
                viewPager.setCurrentItem(position,false);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_music_home;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
