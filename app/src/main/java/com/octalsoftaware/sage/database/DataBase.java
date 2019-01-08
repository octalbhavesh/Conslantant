package com.octalsoftaware.sage.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by shachindrap on 4/27/2018.
 */
@Database(entities = {MessageDetail.class}, version = 1)

public abstract class DataBase extends RoomDatabase {
    private static DataBase INSTANCE;
    public abstract Emply employDao();
    public static DataBase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                    DataBase.class)
                            // To simplify the codelab, allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }
}
