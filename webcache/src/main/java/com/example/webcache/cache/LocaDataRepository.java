package com.example.webcache.cache;

import android.util.Log;

import com.example.dishfo.loadandcache.data.DataRepository;

/**
 *获取数据在本地的路径
 */

public class LocaDataRepository implements DataRepository<String,String>{

    public void addData(String key,String data){
        Log.d("test"," key is :"+key+"\n data :"+data);
    }

    @Override
    public String getData(String key) {

        return null;
    }
}
