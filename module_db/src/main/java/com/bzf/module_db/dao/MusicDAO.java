package com.bzf.module_db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bzf.module_db.entity.Can20BTable;
import com.bzf.module_db.entity.MusicTable;

import java.util.List;

/**
 * @date： 2023/11/16
 * @author: 78495
*/
@Dao
public interface MusicDAO {


    @Query("SELECT * FROM music_table WHERE deviceId= :deviceId")
    List<MusicTable> queryAll(String deviceId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MusicTable> tables);

    @Query("DELETE FROM music_table")
    void deleteAll();

    @Update
    void update(MusicTable table);

}
