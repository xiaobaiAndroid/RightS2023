package com.android.launcher.setting.info;

import com.android.launcher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public class AfterSaleFragment extends CarInfoFragmentBase{

    @Override
    protected List<CarInfoItem> getTitleList() {
        List<CarInfoItem> list = new ArrayList<>();
        list.add(new CarInfoItem(getResources().getString(R.string.after_sale_company), getResources().getString(R.string.company)));
        list.add(new CarInfoItem(getResources().getString(R.string.address_title), getResources().getString(R.string.address)));
        list.add(new CarInfoItem(getResources().getString(R.string.after_sale_contact1), getResources().getString(R.string.contact1)));
        list.add(new CarInfoItem(getResources().getString(R.string.after_sale_contact2), getResources().getString(R.string.contact2)));

        return list;
    }
}
