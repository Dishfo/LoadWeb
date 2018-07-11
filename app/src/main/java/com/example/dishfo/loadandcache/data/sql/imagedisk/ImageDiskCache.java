package com.example.dishfo.loadandcache.data.sql.imagedisk;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * 用于描述图片缓存记录的实体类
 */

@Entity(tableName = "image_disk_cache")
public class ImageDiskCache {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "id",typeAffinity = ColumnInfo.INTEGER)
    private Integer id;

    @NonNull

    @ColumnInfo(name = "image_url",typeAffinity = ColumnInfo.TEXT)
    private String url;


    @ColumnInfo(name = "image_name")
    private String fileName;


    public ImageDiskCache( @NonNull String url) {

        this.url = url;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        Objects.nonNull(id);
        this.id = id;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        Objects.nonNull(url);
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
