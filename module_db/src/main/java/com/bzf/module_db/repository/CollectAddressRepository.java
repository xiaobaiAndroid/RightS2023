package com.bzf.module_db.repository;

import android.content.Context;

import com.bzf.module_db.AppDatabase;
import com.bzf.module_db.dao.CollectAddressDAO;
import com.bzf.module_db.dao.HistorySearchDAO;
import com.bzf.module_db.entity.CollectAddressTable;
import com.bzf.module_db.entity.HistorySearchTable;

import java.util.ArrayList;
import java.util.List;

/**
* @description: 收藏的地址
* @createDate: 2023/5/29
*/
public class CollectAddressRepository {


    private CollectAddressRepository() {}

    private static class SingletonHolder {
        private static final CollectAddressRepository instance = new CollectAddressRepository();
    }

    public static CollectAddressRepository getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized void saveData(Context context, CollectAddressTable table){
        try {
            if(table!=null){
                CollectAddressDAO dao = AppDatabase.getDatabase(context).collectAddressDAO();
                dao.insert(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateData(Context context, CollectAddressTable table){
        try {
            if(table!=null){
                CollectAddressDAO dao = AppDatabase.getDatabase(context).collectAddressDAO();
                dao.update(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized List<CollectAddressTable> getData(Context context){
        try {
            CollectAddressDAO dao = AppDatabase.getDatabase(context).collectAddressDAO();
            return dao.queryAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
