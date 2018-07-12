package com.example.webcache.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "web_html")
public class DataRecord {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "id",typeAffinity = ColumnInfo.INTEGER)
    private Integer id;

    @NonNull

    @ColumnInfo(name = "url",typeAffinity = ColumnInfo.TEXT)
    private String url;


    @ColumnInfo(name = "filename")
    private String fileName;


    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
