package com.bzf.module_db.repository;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bzf.module_db.AppDatabase;
import com.bzf.module_db.dao.CanBCDAO;
import com.bzf.module_db.dao.CarSetupDAO;
import com.bzf.module_db.entity.CanBCTable;
import com.bzf.module_db.entity.CarSetupTable;

import java.util.List;

/**
* @description:
* @createDate: 2023/5/29
*/
public class CanBCRepository {

    private static final String TAG = CanBCRepository.class.getSimpleName();

    private CanBCRepository() {}

    private static class SingletonHolder {
        private static final CanBCRepository instance = new CanBCRepository();
    }

    public static CanBCRepository getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized void saveData(Context context, String deviceId, CanBCTable canBCTable){
        try {
            if(canBCTable!=null){
                CanBCDAO dao = AppDatabase.getDatabase(context).canBCDAO();
                dao.deleteAllByDeviceId(deviceId);
                dao.insert(canBCTable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateData(Context context, CanBCTable table){
        try {
            if(table!=null){
                CanBCDAO dao = AppDatabase.getDatabase(context).canBCDAO();
                dao.update(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized CanBCTable  getData(Context context, String deviceId){
        try {
            if(!TextUtils.isEmpty(deviceId)){
                CanBCDAO dao = AppDatabase.getDatabase(context).canBCDAO();
                List<CanBCTable> tables = dao.queryAll(deviceId);
                if(tables!=null && tables.size() >0){
                    return tables.get(0);
                }else{
                    CanBCTable table = new CanBCTable();
                    table.setDeviceId(deviceId);
                    saveData(context, deviceId,table);
                    return table;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CanBCTable table = new CanBCTable();
        table.setDeviceId("");
        return table;
    }

}
