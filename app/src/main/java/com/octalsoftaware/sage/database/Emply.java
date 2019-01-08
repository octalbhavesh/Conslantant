package com.octalsoftaware.sage.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by shachindrap on 4/27/2018.
 */
@Dao
public interface Emply {
    @Insert(onConflict = REPLACE)
    void insertEmploy(MessageDetail employee);

    @Query("DELETE FROM MessageDetail")
    void deleteAll();

    @Query("SELECT * FROM MessageDetail")
    public List<MessageDetail> fetchAllMessage();
}
