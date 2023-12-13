package com.android.launcher.setting.lighting;

import androidx.fragment.app.Fragment;

import com.android.launcher.common.CommonItem;
import com.android.launcher.R;
import com.android.launcher.setting.SettingHomeFragmentBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @date： 2023/10/17
 * @author: 78495
*/
public class CarLightingListFragment extends SettingHomeFragmentBase {

    @Override
    protected List<CommonItem> getTitleList() {
        List<CommonItem> titleList = new ArrayList<>();
        titleList.add(new CommonItem(getResources().getString(R.string.ambient_lighting_set),true));
        titleList.add(new CommonItem(getResources().getString(R.string.interior_lighting_delay_set),false));
        titleList.add(new CommonItem(getResources().getString(R.string.exterior_lighting_delay_set),false));
        return titleList;
    }

    @Override
    protected List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new AmbientLightingFragment());
        fragmentList.add(new InteriorLightingFragment());
        fragmentList.add(new ExteriorLightingFragment());
        return fragmentList;
    }

}
