package com.bzf.module_db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bzf.module_db.entity.CanBBTable;

import java.util.List;

/**
* @description:
* @createDate: 2023/6/5
*/
@Dao
public interface CanBBDAO {

    @Query("SELECT * FROM canbb_table WHERE deviceId= :deviceId")
    List<CanBBTable> queryAll(String deviceId);

    @Insert
    void insert(CanBBTable table);

    @Query("DELETE FROM canbb_table WHERE deviceId= :deviceId")
    void deleteAllByDeviceId(String deviceId);

    @Update
    void update(CanBBTable table);
}
