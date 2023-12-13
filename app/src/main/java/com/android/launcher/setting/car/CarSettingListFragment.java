package com.android.launcher.setting.car;

import androidx.fragment.app.Fragment;

import com.android.launcher.setting.car.activate.MatchActivateFragment;
import com.android.launcher.R;
import com.android.launcher.common.CommonItem;
import com.android.launcher.setting.SettingHomeFragmentBase;
import com.android.launcher.setting.car.assist.AssistListFragment;
import com.android.launcher.setting.car.mileage.SetCarMileageFragment;
import com.android.launcher.setting.car.onoffbus.OnOffBusAssistFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @date： 2023/10/17
 * @author: 78495
*/
public class CarSettingListFragment extends SettingHomeFragmentBase {

    @Override
    protected List<CommonItem> getTitleList() {
        List<CommonItem> titleList = new ArrayList<>();
        titleList.add(new CommonItem(getResources().getString(R.string.car_type_select),true));
        titleList.add(new CommonItem(getResources().getString(R.string.automatic_matching_activation),false));
        titleList.add(new CommonItem(getResources().getString(R.string.speed_limit),false));
        titleList.add(new CommonItem(getResources().getString(R.string.car_assist),false));
        titleList.add(new CommonItem(getResources().getString(R.string.getting_in_and_out_assistance),false));
        titleList.add(new CommonItem(getResources().getString(R.string.set_the_mileage_of_the_original_car),false));
        return titleList;
    }

    @Override
    protected List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new CarTypeSelectFragment());
        fragmentList.add(new MatchActivateFragment());
        fragmentList.add(new SpeedLimitFragment());
        fragmentList.add(new AssistListFragment());
        fragmentList.add(new OnOffBusAssistFragment());
        fragmentList.add(new SetCarMileageFragment());
        return fragmentList;
    }

}
