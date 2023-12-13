package com.android.launcher.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.common.CommonFragmentAdapter;
import com.android.launcher.setting.car.CarSettingListFragment;
import com.android.launcher.setting.info.CarInfoHomeFragment;
import com.android.launcher.setting.lighting.CarLightingListFragment;
import com.android.launcher.setting.offlinemap.OfflineMapHomeFragment;
import com.android.launcher.setting.system.CarSystemListFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.util.ArrayList;
import java.util.List;

import module.common.MessageEvent;

/**
 * @date： 2023/11/14
 * @author: 78495
*/
public class SettingHomeFragment extends FragmentBase {

    private ViewPager2 contentVP;
    private Banner imageBanner;
    private TabLayout tabLayout;

    private CommonFragmentAdapter fragmentAdapter;
    private SettingImageAdapter imageAdapter;

    private ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            imageBanner.setCurrentItem(position,false);
        }
    };


    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        imageBanner = view.findViewById(R.id.imageBanner);
        contentVP = view.findViewById(R.id.contentVP);
        tabLayout = view.findViewById(R.id.tabLayout);
        contentVP.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        List<Fragment> list = new ArrayList<>();
        list.add(new CarSettingListFragment());
        list.add(new CarLightingListFragment());
        list.add(new CarSystemListFragment());
        list.add(new OfflineMapHomeFragment());
        list.add(new CarInfoHomeFragment());
        fragmentAdapter = new CommonFragmentAdapter(this,list);
        contentVP.setAdapter(fragmentAdapter);
        contentVP.setUserInputEnabled(false);

        List<String> titles = new ArrayList<>();
        titles.add(getResources().getString(R.string.car));
        titles.add(getResources().getString(R.string.lighting));
        titles.add(getResources().getString(R.string.system));
        titles.add(getResources().getString(R.string.offlinemap));
        titles.add(getResources().getString(R.string.info));
        new TabLayoutMediator(tabLayout, contentVP, (tab, position) -> tab.setText(titles.get(position))).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                contentVP.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.ic_car_setting);
        imageList.add(R.drawable.ic_car_lighting);
        imageList.add(R.drawable.ic_car_system);
//        imageList.add(R.drawable.ic_car_system);
        imageList.add(R.drawable.ic_car_info);
        imageAdapter = new SettingImageAdapter(imageList);
//        imageVP.setAdapter(imageAdapter);

        imageBanner.addBannerLifecycleObserver(this)
                .setUserInputEnabled(false)
//                .setPageTransformer(new AlphaPageTransformer())
//                .setPageTransformer(new ZoomOutPageTransformer())
                .setAdapter(new BannerImageAdapter<Integer>(imageList) {
                    @Override
                    public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        holder.imageView.setImageResource(data);
                    }
                },false);

        contentVP.registerOnPageChangeCallback(pageChangeCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            contentVP.unregisterOnPageChangeCallback(pageChangeCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            imageBanner.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_setting_home;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
