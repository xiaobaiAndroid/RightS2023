package com.bzf.module_db.repository;

import android.content.Context;
import android.text.TextUtils;

import com.bzf.module_db.AppDatabase;
import com.bzf.module_db.dao.Can007DAO;
import com.bzf.module_db.dao.CanBCDAO;
import com.bzf.module_db.entity.Can007Table;
import com.bzf.module_db.entity.CanBCTable;

import java.util.List;

/**
* @description:
* @createDate: 2023/5/29
*/
public class Can007Repository {


    private Can007Repository() {}

    private static class SingletonHolder {
        private static final Can007Repository instance = new Can007Repository();
    }

    public static Can007Repository getInstance() {
        return SingletonHolder.instance;
    }

    private synchronized void saveData(Context context, String deviceId, Can007Table table){
        try {
            if(table!=null){
                Can007DAO dao = AppDatabase.getDatabase(context).can007DAO();
                dao.deleteAllByDeviceId(deviceId);
                dao.insert(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateData(Context context, Can007Table table){
        try {
            if(table!=null){
                Can007DAO dao = AppDatabase.getDatabase(context).can007DAO();
                dao.update(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized Can007Table getData(Context context, String deviceId){
        try {
            if(!TextUtils.isEmpty(deviceId)){
                Can007DAO dao = AppDatabase.getDatabase(context).can007DAO();
                List<Can007Table> tables = dao.queryAll(deviceId);
                if(tables!=null && tables.size() >0){
                    return tables.get(0);
                }else{
                    Can007Table table = new Can007Table();
                    table.setDeviceId(deviceId);
                    saveData(context, deviceId,table);
                    return table;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Can007Table table = new Can007Table();
        table.setDeviceId("");
        return table;
    }

}
