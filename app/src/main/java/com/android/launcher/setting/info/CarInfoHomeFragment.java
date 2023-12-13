package com.android.launcher.setting.info;

import androidx.fragment.app.Fragment;

import com.android.launcher.R;
import com.android.launcher.common.CommonItem;
import com.android.launcher.setting.SettingHomeFragmentBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @date： 2023/10/17
 * @author: 78495
*/
public class CarInfoHomeFragment extends SettingHomeFragmentBase {


    @Override
    protected List<CommonItem> getTitleList() {
        List<CommonItem> titleList = new ArrayList<>();
        titleList.add(new CommonItem(getResources().getString(R.string.system_info),true));
        titleList.add(new CommonItem(getResources().getString(R.string.after_sale),false));
        return titleList;
    }

    @Override
    protected List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new CarInfoFragment());
        fragmentList.add(new AfterSaleFragment());
        return fragmentList;
    }

}
