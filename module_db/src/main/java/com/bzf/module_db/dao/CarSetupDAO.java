package com.bzf.module_db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bzf.module_db.entity.CarSetupTable;

import java.util.List;

/**
* @description:
* @createDate: 2023/6/3
*/
@Dao
public interface CarSetupDAO {

    @Query("SELECT * FROM carsetup_table WHERE deviceId= :deviceId")
    List<CarSetupTable> queryAll(String deviceId);

    @Insert
    void insert(CarSetupTable table);

    @Query("DELETE FROM carsetup_table WHERE deviceId= :deviceId")
    void deleteAllByDeviceId(String deviceId);

    @Update
    void update(CarSetupTable table);

}
