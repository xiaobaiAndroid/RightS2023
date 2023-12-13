package com.bzf.module_db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bzf.module_db.dao.Can007DAO;
import com.bzf.module_db.dao.Can1E5DAO;
import com.bzf.module_db.dao.Can20BDAO;
import com.bzf.module_db.dao.CanBBDAO;
import com.bzf.module_db.dao.CanBCDAO;
import com.bzf.module_db.dao.CarSetupDAO;
import com.bzf.module_db.dao.CollectAddressDAO;
import com.bzf.module_db.dao.ContactsDAO;
import com.bzf.module_db.dao.HistorySearchDAO;
import com.bzf.module_db.dao.MusicDAO;
import com.bzf.module_db.entity.Can007Table;
import com.bzf.module_db.entity.Can1E5Table;
import com.bzf.module_db.entity.Can20BTable;
import com.bzf.module_db.entity.CanBBTable;
import com.bzf.module_db.entity.CanBCTable;
import com.bzf.module_db.entity.CarSetupTable;
import com.bzf.module_db.entity.CollectAddressTable;
import com.bzf.module_db.entity.ContactsTable;
import com.bzf.module_db.entity.HistorySearchTable;
import com.bzf.module_db.entity.MusicTable;

@Database(entities = {ContactsTable.class, CarSetupTable.class, CanBBTable.class
        , Can20BTable.class, CanBCTable.class, Can007Table.class
        , Can1E5Table.class, MusicTable.class, HistorySearchTable.class
        , CollectAddressTable.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactsDAO contactsDAO();

    public abstract CarSetupDAO carSetupDAO();

    public abstract CanBCDAO canBCDAO();

    public abstract CanBBDAO canBBDAO();

    public abstract Can20BDAO can20BDAO();

    public abstract Can007DAO can007DAO();

    public abstract Can1E5DAO can1E5DAO();
    public abstract MusicDAO musicDAO();

    public abstract HistorySearchDAO historySearchDAO();
    public abstract CollectAddressDAO collectAddressDAO();

    private static AppDatabase INSTANCE = null;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "db_car"
                ).build();
            }
        }
        return INSTANCE;
    }

}
