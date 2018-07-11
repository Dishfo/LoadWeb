package com.example.dishfo.loadandcache.data.manager;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

public class WorkFactory implements ThreadFactory{
    @Override
    public Thread newThread(@NonNull Runnable r) {
        return new WorkThread();
    }


    static final class WorkThread extends Thread implements Queuing{
        private Handler handler;
        private boolean inited=false;

        @Override
        public void run() {
            Looper.prepare();
            handler=new Handler(Looper.myLooper());
            inited=true;
            Looper.loop();
        }

        @Override
        public Handler getHandler() {
            while (!inited);
            return handler;
        }

    }
}
