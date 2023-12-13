package com.android.launcher.setting.system.appserve;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import module.common.MessageEvent;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;

import module.common.type.AppPackageType;
import module.common.utils.BluetoothMusicHelper;
import com.android.launcher.music.usb.MusicPlayService;

import org.greenrobot.eventbus.EventBus;

import module.common.utils.AppUtils;
import module.common.utils.FMHelper;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public class AppServeFragment extends FragmentBase {

    private TextView versionTV;
    private TextView nameTV;
    private ImageView iconIV;
    private String packageName;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        nameTV = view.findViewById(R.id.nameTV);
        versionTV = view.findViewById(R.id.versionTV);
        iconIV = view.findViewById(R.id.appIV);
       TextView openTV = view.findViewById(R.id.openTV);

        Bundle arguments = getArguments();
        if(arguments!=null){
            packageName = arguments.getString("packageName");
        }

        PackageInfo packageInfo =  AppUtils.getPackageInfo(getContext(),packageName);
        ApplicationInfo applicationInfo = AppUtils.getApplicationInfo(getContext(), packageName);

        if(packageInfo!=null){
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            versionTV.setText(versionName);
        }

        if(applicationInfo!=null){
            Drawable icon = applicationInfo.loadIcon(getContext().getPackageManager());
            CharSequence appName = applicationInfo.loadLabel(getContext().getPackageManager());


            iconIV.setImageDrawable(icon);
            nameTV.setText(appName);
        }else{
            nameTV.setText(getResources().getString(R.string.not_find_carplay_app));
        }

        openTV.setOnClickListener(v -> {
            if(packageName!=null){
                toApp();
            }
        });
    }

    private void toApp() {
        try {
            FMHelper.finishFM(getContext());
            MusicPlayService.stopMusicService(getContext());
            BluetoothMusicHelper.pause(getContext());

            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                startActivity(intent);
            }

            if(AppPackageType.CAR_PLAY.getTypeValue().equals(packageName)){
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_STATUS_BAR_VIEW));
            }else if(AppPackageType.CAR_METER.getTypeValue().equals(packageName)){
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_METER_TOUCH_FLOATING));
            }else if(AppPackageType.DSP.getTypeValue().equals(packageName)){
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_STATUS_BAR_VIEW));
            }else if(AppPackageType.PANORAMA.getTypeValue().equals(packageName)){
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_STATUS_BAR_VIEW));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_carplay;
    }
}
