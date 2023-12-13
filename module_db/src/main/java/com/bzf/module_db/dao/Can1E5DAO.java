package com.bzf.module_db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bzf.module_db.entity.Can1E5Table;
import com.bzf.module_db.entity.Can20BTable;

import java.util.List;

/**
* @description:
* @createDate: 2023/6/5
*/
@Dao
public interface Can1E5DAO {

    @Query("SELECT * FROM can1e5_table WHERE deviceId= :deviceId")
    List<Can1E5Table> queryAll(String deviceId);

    @Insert
    void insert(Can1E5Table table);

    @Query("DELETE FROM can1e5_table WHERE deviceId= :deviceId")
    void deleteAllByDeviceId(String deviceId);

    @Update
    void update(Can1E5Table table);
}
