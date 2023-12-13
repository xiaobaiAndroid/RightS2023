package com.bzf.module_db.repository;

import android.content.Context;
import android.text.TextUtils;

import com.bzf.module_db.AppDatabase;
import com.bzf.module_db.dao.Can1E5DAO;
import com.bzf.module_db.dao.Can20BDAO;
import com.bzf.module_db.entity.Can1E5Table;
import com.bzf.module_db.entity.Can20BTable;

import java.util.List;

/**
* @description:
* @createDate: 2023/5/29
*/
public class Can1E5Repository {

    private static final String TAG = Can1E5Repository.class.getSimpleName();

    private Can1E5Repository() {}

    private static class SingletonHolder {
        private static final Can1E5Repository instance = new Can1E5Repository();
    }

    public static Can1E5Repository getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized void saveData(Context context, String deviceId, Can1E5Table table){
        try {
            if(table!=null){
                Can1E5DAO dao = AppDatabase.getDatabase(context).can1E5DAO();
                dao.deleteAllByDeviceId(deviceId);
                dao.insert(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateData(Context context, Can1E5Table table){
        try {
            if(table!=null){
                Can1E5DAO dao = AppDatabase.getDatabase(context).can1E5DAO();
                dao.update(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized Can1E5Table  getData(Context context, String deviceId){
        try {
            if(!TextUtils.isEmpty(deviceId)){
                Can1E5DAO dao = AppDatabase.getDatabase(context).can1E5DAO();
                List<Can1E5Table> tables = dao.queryAll(deviceId);
                if(tables!=null && tables.size() >0){
                    return tables.get(0);
                }else{
                    Can1E5Table table = new Can1E5Table();
                    table.setDeviceId(deviceId);
                    saveData(context, deviceId,table);
                    return table;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Can1E5Table table = new Can1E5Table();
        table.setDeviceId("");
        return table;
    }
}
