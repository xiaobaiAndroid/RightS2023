package com.bzf.module_db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bzf.module_db.entity.ContactsTable;

import java.util.List;

@Dao
public interface ContactsDAO {


    @Query("SELECT * FROM contacts_table WHERE deviceId= :deviceId ORDER BY name")
    List<ContactsTable> queryAll(String deviceId);

    @Insert
    void insertAll(List<ContactsTable> users);

    @Query("DELETE FROM contacts_table WHERE deviceId= :deviceId")
    void deleteAllByDeviceId(String deviceId);

    @Update
    void update(ContactsTable table);

}
