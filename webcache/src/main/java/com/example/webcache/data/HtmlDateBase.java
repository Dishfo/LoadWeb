package com.example.webcache.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.dishfo.loadandcache.data.sql.ImageDao;

@Database(entities = {DataRecord.class},version = 1)
public abstract class HtmlDateBase extends RoomDatabase{
    public abstract HtmlDao htmlDao();

}
