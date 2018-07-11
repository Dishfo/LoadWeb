package com.example.dishfo.loadandcache.data.imageloader;

import android.graphics.Bitmap;

import com.example.dishfo.loadandcache.data.DataRepository;

/**
 * 获取bitmap的抽象类
 */
public abstract class ImageRepository implements DataRepository<Bitmap,String>{

    public Bitmap getImage(String url){
        return getData(url);
    }
}
