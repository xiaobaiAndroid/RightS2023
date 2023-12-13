package com.bzf.module_db.repository;

import android.content.Context;
import android.text.TextUtils;

import com.bzf.module_db.AppDatabase;
import com.bzf.module_db.dao.Can20BDAO;
import com.bzf.module_db.dao.MusicDAO;
import com.bzf.module_db.entity.Can20BTable;
import com.bzf.module_db.entity.MusicTable;

import java.util.ArrayList;
import java.util.List;

/**
* @description:
* @createDate: 2023/5/29
*/
public class MusicRepository {

    private static final String TAG = MusicRepository.class.getSimpleName();

    private MusicRepository() {}

    private static class SingletonHolder {
        private static final MusicRepository instance = new MusicRepository();
    }

    public static MusicRepository getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized void saveData(Context context, List<MusicTable> table){
        try {
            if(table!=null){
                MusicDAO dao = AppDatabase.getDatabase(context).musicDAO();
                dao.insertAll(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateData(Context context, MusicTable table){
        try {
            if(table!=null){
                MusicDAO dao = AppDatabase.getDatabase(context).musicDAO();
                dao.update(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized List<MusicTable>  getData(Context context, String deviceId){
        try {
            if(!TextUtils.isEmpty(deviceId)){
                MusicDAO dao = AppDatabase.getDatabase(context).musicDAO();
                List<MusicTable> tables = dao.queryAll(deviceId);
               return tables;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public synchronized void deleteAll(Context context){
        try {
            MusicDAO dao = AppDatabase.getDatabase(context).musicDAO();
            dao.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
