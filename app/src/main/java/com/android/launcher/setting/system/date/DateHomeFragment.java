package com.android.launcher.setting.system.date;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import module.common.MessageEvent;

import com.android.launcher.base.FragmentBase;
import com.android.launcher.R;
import com.android.launcher.util.CarDateUtils;

import module.common.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @date： 2023/10/19
 * @author: 78495
*/
public class DateHomeFragment extends FragmentBase {

    private TextView timeTV;
    private ScheduledExecutorService timeService;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        TextView dateTV = view.findViewById(R.id.dateTV);
        TextView weekTV = view.findViewById(R.id.weekTV);
        timeTV = view.findViewById(R.id.timeTV);

        String date = CarDateUtils.getDate(getContext(), "GMT+8:00");
        dateTV.setText(date);
        weekTV.setText(CarDateUtils.getWeek(getContext(),"GMT+8:00"));

        timeService = Executors.newSingleThreadScheduledExecutor();
        timeService.scheduleAtFixedRate(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String currentTime =  sdf.format(new Date()) ;

            if(getActivity()!=null){
                getActivity().runOnUiThread(() -> {
                    if(timeTV!=null){
                        timeTV.setText(StringUtils.removeNull(currentTime));
                    }
                });
            }

        },0, 1,TimeUnit.SECONDS);

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_date_home;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(timeService!=null){
            timeService.shutdown();
        }
    }
}
