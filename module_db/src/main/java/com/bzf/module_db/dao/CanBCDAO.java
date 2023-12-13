package com.bzf.module_db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bzf.module_db.entity.CanBCTable;

import java.util.List;

/**
* @description:
* @createDate: 2023/6/5
*/
@Dao
public interface CanBCDAO {

    @Query("SELECT * FROM canbc_table WHERE deviceId= :deviceId")
    List<CanBCTable> queryAll(String deviceId);

    @Insert
    void insert(CanBCTable table);

    @Query("DELETE FROM canbc_table WHERE deviceId= :deviceId")
    void deleteAllByDeviceId(String deviceId);

    @Update
    void update(CanBCTable table);
}
