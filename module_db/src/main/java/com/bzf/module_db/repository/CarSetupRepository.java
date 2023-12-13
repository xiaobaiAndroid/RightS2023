package com.bzf.module_db.repository;

import android.content.Context;
import android.text.TextUtils;

import com.bzf.module_db.AppDatabase;
import com.bzf.module_db.dao.CarSetupDAO;
import com.bzf.module_db.entity.CarSetupTable;

import java.util.List;

/**
* @description:
* @createDate: 2023/5/29
*/
public class CarSetupRepository {
    private static final String TAG = CarSetupRepository.class.getSimpleName();

    private CarSetupRepository() {}

    private static class SingletonHolder {
        private static final CarSetupRepository instance = new CarSetupRepository();
    }

    public static CarSetupRepository getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized void saveData(Context context,String deviceId, CarSetupTable carSetupTable){
        try {
            if(carSetupTable!=null){
                CarSetupDAO dao = AppDatabase.getDatabase(context).carSetupDAO();
                dao.deleteAllByDeviceId(deviceId);
                dao.insert(carSetupTable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized void updateData(Context context, CarSetupTable table){
        try {
            if(table!=null){
                CarSetupDAO dao = AppDatabase.getDatabase(context).carSetupDAO();
                dao.update(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized CarSetupTable getData(Context context, String deviceId){
        try {
            if(!TextUtils.isEmpty(deviceId)){
                CarSetupDAO dao = AppDatabase.getDatabase(context).carSetupDAO();
                List<CarSetupTable> carSetupTables = dao.queryAll(deviceId);
                if(carSetupTables!=null && carSetupTables.size() >0){
                    return carSetupTables.get(0);
                }else{
                    CarSetupTable carSetupTable = new CarSetupTable();
                    carSetupTable.setDeviceId(deviceId);
                    saveData(context, deviceId,carSetupTable);
                    return carSetupTable;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CarSetupTable carSetupTable = new CarSetupTable();
        carSetupTable.setDeviceId("");
        return carSetupTable;
    }

}
