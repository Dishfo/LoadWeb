package com.example.dishfo.loadandcache.context;

import android.app.Application;

import com.example.dishfo.loadandcache.data.imageloader.ImageLoader;
import com.example.dishfo.loadandcache.data.manager.LoadEngine;

public class MyApplication extends Application{

    private ImageLoader imageLoader;
    private LoadEngine loadEngine;

    @Override
    public void onCreate() {
        super.onCreate();
        imageLoader=new ImageLoader(this);
        loadEngine=new LoadEngine();
        loadEngine.setRealLoader(imageLoader);
    }

    public LoadEngine getLoadEngine() {
        return loadEngine;
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
