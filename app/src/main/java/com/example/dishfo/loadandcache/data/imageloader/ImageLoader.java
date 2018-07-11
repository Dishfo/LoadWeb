package com.example.dishfo.loadandcache.data.imageloader;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.dishfo.loadandcache.cache.MemoryCache;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 集合了本地与网络加载图片的包装类
 * 任然是同步加载
 */
public class ImageLoader extends  ImageRepository{

    private Context context;
    private LocalImageRepository localImageRepository;
    private RemoteImageRepository remoteImageRepository;

    private MemoryCache memoryCache;

    public ImageLoader(Context context) {
        Objects.nonNull(context);
        this.context = context;
        memoryCache=new MemoryCache();
        localImageRepository=new LocalImageRepository(context);
        remoteImageRepository=new RemoteImageRepository(context);
    }

    @Override
    public Bitmap getData(String key) {


        Bitmap bitmap=memoryCache.get(key);
        if(bitmap==null){
            bitmap=localImageRepository.getData(key);
            if(bitmap!=null){
                putIntoCache(key,bitmap);
            }
        }

        if(bitmap==null){
            bitmap=remoteImageRepository.getData(key);
            if(bitmap!=null){
                putIntoCache(key,bitmap);
                localImageRepository.putInDisk(key,bitmap);
            }
        }
        return bitmap;
    }

    private void putIntoCache(String url,Bitmap bitmap){
        memoryCache.put(url,bitmap);
    }
}
