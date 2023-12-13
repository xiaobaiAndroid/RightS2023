package com.android.launcher.contacts;

import com.android.launcher.base.IView;
import com.bzf.module_db.entity.ContactsTable;

import java.util.List;

public interface ContactsIView extends IView {

    void getContactsResult(List<ContactsTable> list);

    void getContactsFail();
}
