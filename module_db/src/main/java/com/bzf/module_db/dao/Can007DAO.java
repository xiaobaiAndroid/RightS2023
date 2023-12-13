package com.bzf.module_db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bzf.module_db.entity.Can007Table;
import com.bzf.module_db.entity.Can20BTable;

import java.util.List;

/**
 * @date： 2023/11/3
 * @author: 78495
*/
@Dao
public interface Can007DAO {

    @Query("SELECT * FROM can007_table WHERE deviceId= :deviceId")
    List<Can007Table> queryAll(String deviceId);

    @Insert
    void insert(Can007Table table);

    @Query("DELETE FROM can007_table WHERE deviceId= :deviceId")
    void deleteAllByDeviceId(String deviceId);

    @Update
    void update(Can007Table table);
}
