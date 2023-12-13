package com.android.launcher.contacts;

import android.text.TextUtils;

import com.android.launcher.R;
import module.common.utils.StringUtils;
import com.bzf.module_db.entity.ContactsTable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

public class ContactsAdapter extends BaseQuickAdapter<ContactsTable, BaseViewHolder> {


    public ContactsAdapter() {
        super(R.layout.layout_item_contacts);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactsTable item) {
        if (item != null) {

            String phone = StringUtils.removeNull(item.getPhone1());
            String phone2 = StringUtils.removeNull(item.getPhone2());

            if (TextUtils.isEmpty(phone2)) {
                helper.setText(R.id.nameTV, item.getName());
            } else {
                helper.setText(R.id.nameTV, item.getName());
            }

        }
    }
}
