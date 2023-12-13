package com.android.launcher.setting.info;

import com.android.launcher.MyApp;
import com.android.launcher.R;

import module.common.utils.APKVersionInfoUtils;
import module.common.utils.AppUtils;
import module.common.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @date： 2023/10/18
 * @author: 78495
 */
public class CarInfoFragment extends CarInfoFragmentBase {


    @Override
    protected List<CarInfoItem> getTitleList() {
        int screenWidth = DensityUtil.getScreenWidth(getActivity());
        int screenHeight = DensityUtil.getScreenHeight(getActivity());


        String versionName = APKVersionInfoUtils.getVersionName(MyApp.getGlobalContext());

        List<CarInfoItem> list = new ArrayList<>();
        list.add(new CarInfoItem(getResources().getString(R.string.system_info), android.os.Build.MODEL));
        list.add(new CarInfoItem(getResources().getString(R.string.android_version), android.os.Build.VERSION.RELEASE));
        list.add(new CarInfoItem(getResources().getString(R.string.kernel_version), System.getProperty("os.version")));
        list.add(new CarInfoItem(getResources().getString(R.string.device_id), AppUtils.getDeviceId(getContext())));
        list.add(new CarInfoItem(getResources().getString(R.string.manufacturer), android.os.Build.MANUFACTURER));
        list.add(new CarInfoItem(getResources().getString(R.string.resolution), screenWidth + "*" + screenHeight));
        list.add(new CarInfoItem(getResources().getString(R.string.system_architecture), System.getProperty("os.arch")));
        list.add(new CarInfoItem(getResources().getString(R.string.version_name), versionName));
        return list;
    }
}
