package com.example.dishfo.loadandcache.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class CloseUtil {
    public static void closeInputStream(InputStream inputStream){
        if(inputStream!=null){
            try {
                inputStream.close();
            }catch (Exception e){

            }
        }
    }

    public static void closeOutputStream(OutputStream outputStream){
        if(outputStream!=null){
            try {
                outputStream.close();
            } catch (Exception e) {

            }
        }
    }
}
