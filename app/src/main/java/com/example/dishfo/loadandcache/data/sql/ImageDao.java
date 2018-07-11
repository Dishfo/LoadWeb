package com.example.dishfo.loadandcache.data.sql;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import com.example.dishfo.loadandcache.data.sql.imagedisk.ImageDiskCache;

import java.util.List;

@Dao
public interface ImageDao {

    @Insert
    Long insertImageDiskCache(ImageDiskCache imageDiskCache);

    @Query("select * from image_disk_cache")
    List<ImageDiskCache> getImageDiskCaches();

    @Query("select * from image_disk_cache where id=:id")
    ImageDiskCache getImageDiskCacheById(@NonNull Integer id);

    @Query("select * from image_disk_cache where image_url=:url")
    ImageDiskCache getImageDiskCacheByUrl(@NonNull String url);

    @Delete()
    int deleteImageDiskCache(ImageDiskCache imageDiskCache);
}
