package com.example.dishfo.loadandcache.data.imageloader;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.dishfo.loadandcache.ContextConstant;
import com.example.dishfo.loadandcache.data.sql.ImageDataBase;
import com.example.dishfo.loadandcache.data.sql.imagedisk.ImageDiskCache;
import com.example.dishfo.loadandcache.util.CloseUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 同步的本地图片缓存的读取
 */
public class LocalImageRepository extends ImageRepository{

    private Context context;
    ImageDataBase imageDataBase;

    public LocalImageRepository(Context context) {
        Objects.nonNull(context);
        this.context = context;
        imageDataBase= Room.
                databaseBuilder(context,ImageDataBase.class, ContextConstant.db_name).build();

    }

    @Override
    public Bitmap getData(String key) {
        ImageDiskCache imageDiskCache=
                imageDataBase.imageDao().getImageDiskCacheByUrl(key);
        if(imageDiskCache==null){
            return null;
        }else {
            InputStream inputStream=null;
            try {
                inputStream=context.openFileInput(imageDiskCache.getFileName());
                return BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException e) {
                Log.d(ContextConstant.error_file_tag,"未找到缓存文件");
                imageDataBase.imageDao().deleteImageDiskCache(imageDiskCache);
                return null;
            }finally {
                if(inputStream!=null){try{inputStream.close();}catch (Exception e){}}
            }
        }
    }

    public void putInDisk(String url,Bitmap bitmap){
        String filename=System.currentTimeMillis()+".PNG";
        OutputStream outputStream=null;
        File file=new File(context.getFilesDir(),filename);

        if(!file.exists()){
            try {
                boolean has=file.createNewFile();
                if(!has){
                    Log.d("test","创建文件失败");
                    return;
                }
            } catch (IOException e) {
                Log.d("test","io 异常");
                return;
            }
        }

        try {
            outputStream=context.openFileOutput(filename,Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();

            ImageDiskCache imageDiskCache=new ImageDiskCache(url);
            imageDiskCache.setFileName(filename);

            imageDataBase.imageDao().insertImageDiskCache(imageDiskCache);
        } catch (FileNotFoundException e) {
            Log.d("test","没有这个文件");
        } catch (IOException e) {
            file.delete();
            Log.d("test","io 异常");
        } finally {
            CloseUtil.closeOutputStream(outputStream);
        }
    }

}
