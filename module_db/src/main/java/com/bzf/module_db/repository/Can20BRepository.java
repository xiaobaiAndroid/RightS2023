package com.bzf.module_db.repository;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bzf.module_db.AppDatabase;
import com.bzf.module_db.dao.Can20BDAO;
import com.bzf.module_db.dao.CanBBDAO;
import com.bzf.module_db.entity.Can20BTable;
import com.bzf.module_db.entity.CanBBTable;

import java.util.List;

/**
* @description:
* @createDate: 2023/5/29
*/
public class Can20BRepository {

    private static final String TAG = Can20BRepository.class.getSimpleName();

    private Can20BRepository() {}

    private static class SingletonHolder {
        private static final Can20BRepository instance = new Can20BRepository();
    }

    public static Can20BRepository getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized void saveData(Context context, String deviceId, Can20BTable table){
        try {
            if(table!=null){
                Can20BDAO dao = AppDatabase.getDatabase(context).can20BDAO();
                dao.deleteAllByDeviceId(deviceId);
                dao.insert(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateData(Context context, Can20BTable table){
        try {
            if(table!=null){
                Can20BDAO dao = AppDatabase.getDatabase(context).can20BDAO();
                dao.update(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized Can20BTable  getData(Context context, String deviceId){
        try {
            if(!TextUtils.isEmpty(deviceId)){
                Can20BDAO dao = AppDatabase.getDatabase(context).can20BDAO();
                List<Can20BTable> tables = dao.queryAll(deviceId);
                if(tables!=null && tables.size() >0){
                    return tables.get(0);
                }else{
                    Can20BTable table = new Can20BTable();
                    table.setDeviceId(deviceId);
                    saveData(context, deviceId,table);
                    return table;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Can20BTable table = new Can20BTable();
        table.setDeviceId("");
        return table;
    }
}
