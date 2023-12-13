package com.bzf.module_db.repository;

import android.content.Context;
import android.text.TextUtils;

import com.bzf.module_db.AppDatabase;
import com.bzf.module_db.dao.Can1E5DAO;
import com.bzf.module_db.dao.HistorySearchDAO;
import com.bzf.module_db.entity.Can1E5Table;
import com.bzf.module_db.entity.HistorySearchTable;

import java.util.ArrayList;
import java.util.List;

/**
* @description:
* @createDate: 2023/5/29
*/
public class HistorySearchRepository {


    private HistorySearchRepository() {}



    private static class SingletonHolder {
        private static final HistorySearchRepository instance = new HistorySearchRepository();
    }

    public static HistorySearchRepository getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized void saveData(Context context,HistorySearchTable table){
        try {
            if(table!=null){
                HistorySearchDAO historySearchDAO = AppDatabase.getDatabase(context).historySearchDAO();
                historySearchDAO.insert(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateData(Context context, HistorySearchTable table){
        try {
            if(table!=null){
                HistorySearchDAO dao = AppDatabase.getDatabase(context).historySearchDAO();
                dao.update(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized List<HistorySearchTable> getData(Context context){
        try {
            HistorySearchDAO dao = AppDatabase.getDatabase(context).historySearchDAO();
            return dao.queryAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void removeAll(Context context) {
        try {
            HistorySearchDAO dao = AppDatabase.getDatabase(context).historySearchDAO();
            dao.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
