package com.octalsoftaware.sage.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by shachindrap on 4/27/2018.
 */
@Entity
public class MessageDetail {
    @PrimaryKey(autoGenerate = true)
    public long employId;

    @ColumnInfo(name = "sender_id")
    public String sender_id;
    @ColumnInfo(name = "sender_name")
    public String sender_name;
    @ColumnInfo(name = "receiver_id")
    public String receiver_id;
    @ColumnInfo(name = "receiver_name")
    public String receiver_name;
    @ColumnInfo(name = "text_body")
    public String text_body;
    @ColumnInfo(name = "device_type")
    public String device_type;
    @ColumnInfo(name = "type")
    public String type;
    public String sender_time;
    @ColumnInfo(name = "receiver_time")
    public String receiver_time;
    @ColumnInfo(name = "img")
    public String img;

}
