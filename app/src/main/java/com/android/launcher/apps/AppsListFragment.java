package com.android.launcher.apps;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.launcher.MainActivity;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.type.ThirdAppType;
import com.android.launcher.util.FuncUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import module.common.MessageEvent;
import module.common.type.AppPackageType;
import module.common.utils.AppUtils;

public class AppsListFragment extends FragmentBase {

    private AppAdapter appAdapter = new AppAdapter(new ArrayList<>());

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        RecyclerView contentRV = view.findViewById(R.id.titleRV);
        contentRV.setLayoutManager(new GridLayoutManager(getContext(), 4));
        contentRV.setAdapter(appAdapter);
        appAdapter.setOnItemClickListener((adapter, itemView, position) -> {
            ApplicationInfo item = appAdapter.getItem(position);
            try {

                if("com.android.settings".equals(item.packageName)){
                    Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_STATUS_BAR_VIEW));
                } else if(AppPackageType.CAR_METER.getTypeValue().equals(item.packageName)){
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_METER_TOUCH_FLOATING));
                } else if(AppPackageType.DSP.getTypeValue().equals(item.packageName)){
                    FuncUtil.sendShellCommand(" am start --user 0 -n com.xyauto.dspSetting/.ui.MainActivity");
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_STATUS_BAR_VIEW));
                    MainActivity.mCurrentThirdAppType = ThirdAppType.DSP;
                }else {
                    Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(item.packageName);
                    if (intent != null) {
                        startActivity(intent);
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_STATUS_BAR_VIEW));
                    } else {
                        // 第三方应用未安装
                        Toast.makeText(getActivity(), "第三方应用未安装", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (ActivityNotFoundException e) {
                // 发生异常，可能是未找到对应的Activity
                Toast.makeText(getContext(), "无法打开第三方应用", Toast.LENGTH_SHORT).show();
            }
        });

        mExecutor.execute(() -> {
            try {
                List<ApplicationInfo> applicationInfos = AppUtils.loadAllApp(getContext());
                getActivity().runOnUiThread(() -> {
                    if (appAdapter != null) {
                        appAdapter.setNewData(applicationInfos);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_apps_list;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
