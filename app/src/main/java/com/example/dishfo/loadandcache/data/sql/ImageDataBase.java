package com.example.dishfo.loadandcache.data.sql;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.dishfo.loadandcache.data.sql.imagedisk.ImageDiskCache;

@Database(entities = {ImageDiskCache.class},version = 1)
public abstract class ImageDataBase extends RoomDatabase{
    public abstract ImageDao imageDao();
}
