package com.android.launcher.contacts;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import module.common.MessageEvent;

import com.android.launcher.view.LinearSpaceDecoration;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.bluetooth.BluetoothDeviceHelper;
import module.common.utils.BluetoothMusicHelper;
import com.android.launcher.bluetooth.BluetoothTelephonyHelper;
import com.android.launcher.music.usb.MusicPlayService;
import module.common.utils.DensityUtil;
import module.common.utils.FMHelper;

import com.bzf.module_db.entity.ContactsTable;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 通讯录
 *
 * @date： 2023/10/16
 * @author: 78495
 */
public class ContactsFragment extends FragmentBase implements ContactsIView {

    private RecyclerView contactsRV;
    private ContactsAdapter mAdapter = new ContactsAdapter();

    private ContactsPresenter mPresenter;

    private int lastSelectedPosition = 0;


    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mPresenter = new ContactsPresenter(getContext(), this);

        contactsRV = view.findViewById(R.id.titleRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        contactsRV.setLayoutManager(linearLayoutManager);


        LinearSpaceDecoration linearSpaceDecoration = new LinearSpaceDecoration(mAdapter, DensityUtil.dip2px(getContext(), 16));
        linearSpaceDecoration.setmDrawLastItem(false);
        contactsRV.addItemDecoration(linearSpaceDecoration);
        contactsRV.setAdapter(mAdapter);

        setupListener();

        mPresenter.loadData();
    }

    private void setupListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            try {
                resetAllItemStatus();
                ContactsTable mAdapterItem = mAdapter.getItem(position);
                mAdapterItem.setSelect(true);
                mAdapter.notifyItemChanged(position);

                lastSelectedPosition = position;

                if (!BluetoothDeviceHelper.hasDevice(getContext())) {
                    try {
                        Toast toast = Toast.makeText(getContext(),
                                "请链接蓝牙", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        LinearLayout toastView = (LinearLayout) toast.getView();
                        ImageView imageCodeProject = new ImageView(getContext());
                        imageCodeProject.setImageResource(R.drawable.right_car_set_list_bluetooth_icon);
                        toastView.addView(imageCodeProject, 0);
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (lastSelectedPosition >= 0 && lastSelectedPosition < mAdapter.getData().size()) {
                        FMHelper.finishFM(getActivity());
                        MusicPlayService.stopMusicService(getContext());
                        BluetoothMusicHelper.pause(getContext());
                        ContactsTable contactsTable = mAdapter.getItem(lastSelectedPosition);
                        String phone = contactsTable.getPhone1();
                        BluetoothTelephonyHelper.call(getContext(), phone);

                        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.CALL_PHONE));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    private void resetAllItemStatus() {
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            ContactsTable item = mAdapter.getItem(i);
            if (item.isSelect()) {
                item.setSelect(false);
                mAdapter.notifyItemChanged(i);
            }
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    public void getContactsResult(List<ContactsTable> list) {
        if (mAdapter != null) {
            if (list != null && !list.isEmpty()) {
                ContactsTable contactsTable = list.get(lastSelectedPosition);
                contactsTable.setSelect(true);
            }
            mAdapter.setNewData(list);
        }
    }

    @Override
    public void getContactsFail() {
        try {
            View view = getLayoutInflater().inflate(R.layout.layout_empty_data, null);
            TextView messageTV = view.findViewById(R.id.messageTV);
            messageTV.setText(getResources().getString(R.string.not_contacts));
            mAdapter.setEmptyView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
