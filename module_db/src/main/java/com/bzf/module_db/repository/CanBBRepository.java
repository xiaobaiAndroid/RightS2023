package com.bzf.module_db.repository;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bzf.module_db.AppDatabase;
import com.bzf.module_db.dao.CanBBDAO;
import com.bzf.module_db.dao.CanBCDAO;
import com.bzf.module_db.entity.CanBBTable;
import com.bzf.module_db.entity.CanBCTable;

import java.util.List;

/**
* @description:
* @createDate: 2023/5/29
*/
public class CanBBRepository {

    private static final String TAG = CanBBRepository.class.getSimpleName();

    private CanBBRepository() {}

    private static class SingletonHolder {
        private static final CanBBRepository instance = new CanBBRepository();
    }

    public static CanBBRepository getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized void saveData(Context context, String deviceId, CanBBTable table){
        try {
            if(table!=null){
                CanBBDAO dao = AppDatabase.getDatabase(context).canBBDAO();
                dao.deleteAllByDeviceId(deviceId);
                dao.insert(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateData(Context context, CanBBTable table){
        try {
            if(table!=null){
                CanBBDAO dao = AppDatabase.getDatabase(context).canBBDAO();
                dao.update(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized CanBBTable  getData(Context context, String deviceId){
        try {
            if(!TextUtils.isEmpty(deviceId)){
                CanBBDAO dao = AppDatabase.getDatabase(context).canBBDAO();
                List<CanBBTable> tables = dao.queryAll(deviceId);
                if(tables!=null && tables.size() >0){
                    return tables.get(0);
                }else{
                    CanBBTable table = new CanBBTable();
                    table.setDeviceId(deviceId);
                    saveData(context, deviceId,table);
                    return table;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CanBBTable table = new CanBBTable();
        table.setDeviceId("");
        return table;
    }

}
