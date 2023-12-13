package com.bzf.module_db.repository;

import android.content.Context;
import android.text.TextUtils;

import com.bzf.module_db.AppDatabase;
import com.bzf.module_db.dao.ContactsDAO;
import com.bzf.module_db.entity.ContactsTable;

import java.util.ArrayList;
import java.util.List;

/**
* @description:
* @createDate: 2023/5/29
*/
public class ContactsRepository {

    private ContactsRepository() {}


    private static class SingletonHolder {
        private static final ContactsRepository instance = new ContactsRepository();
    }

    public static ContactsRepository getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized void saveData(Context context,String deviceId, List<ContactsTable> list){
        try {
            if(list!=null && !list.isEmpty()){
                ContactsDAO contactsDAO = AppDatabase.getDatabase(context).contactsDAO();
                contactsDAO.deleteAllByDeviceId(deviceId);
                contactsDAO.insertAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized List<ContactsTable>  getData(Context context, String deviceId){
        try {
            if(!TextUtils.isEmpty(deviceId)){
                ContactsDAO contactsDAO = AppDatabase.getDatabase(context).contactsDAO();
               return contactsDAO.queryAll(deviceId);
            }else{
                return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public synchronized void removeAll(Context context,String deviceId) {
        try {
            ContactsDAO contactsDAO = AppDatabase.getDatabase(context).contactsDAO();
            contactsDAO.deleteAllByDeviceId(deviceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
