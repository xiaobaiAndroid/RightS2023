package com.android.launcher.contacts;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.launcher.MainActivity;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.bluetooth.BluetoothTelephonyHelper;
import com.android.launcher.common.CommonFragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import module.common.MessageEvent;

/**
 * @date： 2023/11/14
 * @author: 78495
*/
public class CarContractsFragment extends FragmentBase {

    private CommonFragmentAdapter fragmentAdapter;

    private ViewPager2 contentVP;

    public boolean isCalling = false;

    @Override
    public void disposeMessageEvent(MessageEvent event) {
        if(event.type == MessageEvent.Type.CALL_PHONE){
            isCalling = true;
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        contentVP = view.findViewById(R.id.contentVP);

        contentVP.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ContactsFragment());
        fragmentAdapter = new CommonFragmentAdapter(this,fragmentList);
        contentVP.setAdapter(fragmentAdapter);

        final List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.contacts));
        new TabLayoutMediator(tabLayout, contentVP, (tab, position) -> tab.setText(list.get(position))).attach();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_contacts;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isCalling){
            BluetoothTelephonyHelper.handUp(getContext());
            isCalling = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
