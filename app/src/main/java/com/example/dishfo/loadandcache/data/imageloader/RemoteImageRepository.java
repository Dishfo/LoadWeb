package com.example.dishfo.loadandcache.data.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.dishfo.loadandcache.ContextConstant;
import com.example.dishfo.loadandcache.util.CloseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class RemoteImageRepository extends ImageRepository {

    private Context context;

    public RemoteImageRepository(Context context) {
        this.context = context;
    }


    /**
     *
     * @param key 图片的网址
     * @return
     */
    @Override
    public Bitmap getData(String key) {
        Objects.nonNull(key);
        InputStream inputStream=null;
        try {
            URL url=new URL(key);
            URLConnection urlConnection=url.openConnection();
            inputStream =urlConnection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            Log.d(ContextConstant.error_url_tag,"url format error");
            return null;
        } catch (IOException e) {
            Log.d(ContextConstant.error_net_tag,"network error");
            return null;
        }finally {
            CloseUtil.closeInputStream(inputStream);
        }


    }
}
