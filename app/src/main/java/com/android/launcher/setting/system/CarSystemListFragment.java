package com.android.launcher.setting.system;

import androidx.fragment.app.Fragment;

import com.android.launcher.R;
import com.android.launcher.common.CommonItem;
import com.android.launcher.setting.SettingHomeFragmentBase;
import com.android.launcher.setting.system.audio.AudioHomeFragment;
import com.android.launcher.setting.system.bluetooth.BluetoothHomeFragment;
import com.android.launcher.setting.system.date.DateHomeFragment;
import com.android.launcher.setting.system.display.SystemDisplayHomeFragment;
import com.android.launcher.setting.system.language.LanguageHomeFragment;
import com.android.launcher.setting.system.reset.ResetHomeFragment;
import com.android.launcher.setting.system.unit.UnitHomeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @date： 2023/10/17
 * @author: 78495
*/
public class CarSystemListFragment extends SettingHomeFragmentBase {


    @Override
    protected List<CommonItem> getTitleList() {
        List<CommonItem> titleList = new ArrayList<>();
        titleList.add(new CommonItem(getResources().getString(R.string.display_screen),true,true));
        titleList.add(new CommonItem(getResources().getString(R.string.audio),false,true));
        titleList.add(new CommonItem(getResources().getString(R.string.calendar),false,true));
        titleList.add(new CommonItem(getResources().getString(R.string.unit),false,true));
//        titleList.add(new CommonItem(getResources().getString(R.string.modify_logo),false,true));
        titleList.add(new CommonItem(getResources().getString(R.string.restoration),false,true));
        titleList.add(new CommonItem(getResources().getString(R.string.language),false,true));
//        titleList.add(new CommonItem(getResources().getString(R.string.bluetooth),false,true));
        return titleList;
    }

    @Override
    protected List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SystemDisplayHomeFragment());

        fragmentList.add(new AudioHomeFragment());
        fragmentList.add(new DateHomeFragment());
        fragmentList.add(new UnitHomeFragment());
        fragmentList.add(new ResetHomeFragment());
        fragmentList.add(new LanguageHomeFragment());
        fragmentList.add(new BluetoothHomeFragment());
        return fragmentList;
    }

}
