package com.example.webcache.data;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface HtmlDao {
    @Insert
    Long insert(DataRecord dataRecord);

    @Query("select * from web_html where url=:url")
    DataRecord query(String url);

    @Delete
    int delete(DataRecord dataRecord);

}
