package com.android.launcher.ac.winddirection;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.android.launcher.R;
import com.android.launcher.ac.airflow.AirflowPatternView;
import com.android.launcher.ac.winddirection.front.FrontWindDirectionView;
import com.android.launcher.ac.winddirection.rear.RearWindDirectionView;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import module.common.entity.MyTabEntity;

/**
 * @date： 2023/10/15
 * @author: 78495
*/
public class WindDirectionHomeView extends FrameLayout {


    private CommonTabLayout tabLayout;
    private RearWindDirectionView rearWindDirectionView;
    private FrontWindDirectionView frontWindDirectionView;
    private AirflowPatternView airflowPatternView;

    public WindDirectionHomeView(Context context) {
        this(context,null);

    }

    public WindDirectionHomeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WindDirectionHomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public WindDirectionHomeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setupInit(context);
    }

    private void setupInit(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_ac_home, this, true);

        tabLayout = view.findViewById(R.id.tabLayout);
        rearWindDirectionView = view.findViewById(R.id.rearWindDirectionView);
        frontWindDirectionView = view.findViewById(R.id.frontWindDirectionView);
        airflowPatternView = view.findViewById(R.id.airflowPatternView);

        ArrayList<CustomTabEntity> list = new ArrayList<>();
        list.add(new MyTabEntity(getResources().getString(R.string.ac_tab1)));
        list.add(new MyTabEntity(getResources().getString(R.string.ac_tab2)));
        list.add(new MyTabEntity(getResources().getString(R.string.ac_tab3)));
        tabLayout.setTabData(list);

        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if(position == 0){
                    rearWindDirectionView.setVisibility(View.GONE);
                    frontWindDirectionView.setVisibility(View.VISIBLE);
                    airflowPatternView.setVisibility(View.GONE);
                }else if(position == 1){
                    rearWindDirectionView.setVisibility(View.VISIBLE);
                    frontWindDirectionView.setVisibility(View.GONE);
                    airflowPatternView.setVisibility(View.GONE);
                }else {
                    airflowPatternView.setVisibility(View.VISIBLE);
                    rearWindDirectionView.setVisibility(View.GONE);
                    frontWindDirectionView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }



}
