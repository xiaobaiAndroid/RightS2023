package com.bzf.module_db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bzf.module_db.entity.Can20BTable;

import java.util.List;

/**
* @description:
* @createDate: 2023/6/5
*/
@Dao
public interface Can20BDAO {

    @Query("SELECT * FROM can20b_table WHERE deviceId= :deviceId")
    List<Can20BTable> queryAll(String deviceId);

    @Insert
    void insert(Can20BTable table);

    @Query("DELETE FROM can20b_table WHERE deviceId= :deviceId")
    void deleteAllByDeviceId(String deviceId);

    @Update
    void update(Can20BTable table);
}
