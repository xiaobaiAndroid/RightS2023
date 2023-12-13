package com.android.launcher.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.viewpager2.widget.ViewPager2;

import module.common.MessageEvent;

import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * @date： 2023/10/12
 * @author: 78495
 */
public class HomeFragment extends FragmentBase {

    private ViewPager2 contentVP;

    private HomeFragmentAdapter mHomeAdapter;
    private PageIndicatorView pageIndicatorView;
    private ImageView rightArrowsIV;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        pageIndicatorView = view.findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(2);
        rightArrowsIV = view.findViewById(R.id.rightArrowsIV);
        contentVP = view.findViewById(R.id.contentVP);

        List<HomeItemFragment> fragments = new ArrayList<>();
        fragments.add(new HomeItemFragment());
        fragments.add(new HomeItemFragment());

        mHomeAdapter = new HomeFragmentAdapter(this, fragments);

        contentVP.setOffscreenPageLimit(2);
        contentVP.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        contentVP.setAdapter(mHomeAdapter);

        pageIndicatorView = view.findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(fragments.size());
        rightArrowsIV = view.findViewById(R.id.rightArrowsIV);


        contentVP.registerOnPageChangeCallback(pageChangeCallback);

        rightArrowsIV.setOnClickListener(v -> contentVP.setCurrentItem(1, true));


    }

    @Override
    protected void setupData() {
        super.setupData();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            contentVP.unregisterOnPageChangeCallback(pageChangeCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_right_home;
    }

    private ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            pageIndicatorView.setSelection(position);
            if (position == mHomeAdapter.getItemCount() - 1) {
                rightArrowsIV.setVisibility(View.INVISIBLE);
            } else {
                rightArrowsIV.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };
}
