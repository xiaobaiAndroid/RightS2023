package com.bzf.module_db.dao;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bzf.module_db.DBTableNames;
import com.bzf.module_db.entity.Can20BTable;
import com.bzf.module_db.entity.CollectAddressTable;

import java.util.List;

/**
 * @date： 2023/12/2
 * @author: 78495
*/
@Dao
public interface CollectAddressDAO {

    @Query("SELECT * FROM collect_address_table")
    List<CollectAddressTable> queryAll();

    @Insert
    void insert(CollectAddressTable table);

    @Query("DELETE FROM collect_address_table WHERE id= :id")
    void deleteAllById(String id);

    @Query("DELETE FROM collect_address_table")
    void deleteAll();

    @Update
    void update(CollectAddressTable table);
}
