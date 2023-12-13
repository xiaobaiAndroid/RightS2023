package com.bzf.module_db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bzf.module_db.entity.CollectAddressTable;
import com.bzf.module_db.entity.HistorySearchTable;

import java.util.List;

/**
 * @date： 2023/12/2
 * @author: 78495
*/
@Dao
public interface HistorySearchDAO {

    @Query("SELECT * FROM history_address_table")
    List<HistorySearchTable> queryAll();

    @Insert
    void insert(HistorySearchTable table);

    @Query("DELETE FROM history_address_table WHERE id= :id")
    void deleteAllById(String id);

    @Query("DELETE FROM history_address_table")
    void deleteAll();

    @Update
    void update(HistorySearchTable table);
}
