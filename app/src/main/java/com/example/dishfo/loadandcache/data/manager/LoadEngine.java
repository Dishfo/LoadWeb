package com.example.dishfo.loadandcache.data.manager;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.dishfo.loadandcache.data.imageloader.ImageLoader;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;

/**
 * 提供图片加载接口的引擎类
 * 将加载任务进行缓存
 * 在一个线程中统一处理
 * 对于url 相同的任务
 * 我们吧任务结果的处理者加入task 类的处理者队列中
 */

public final class LoadEngine {

    private ConcurrentLinkedDeque<Task> tasks;
    private Handler mainHandler;
    private ImageLoader realLoader;
    private ThreadFactory threadFactory;
    private Thread worker;


    public LoadEngine(){
        tasks=new ConcurrentLinkedDeque<>();
        mainHandler=new Handler(Looper.getMainLooper());
        threadFactory=new WorkFactory();
        worker=threadFactory.newThread(null);
        worker.start();
    }

    public void load(String url){
        Objects.nonNull(url);
        load(url,new DefaultRespond());
    }



    public void load(String url,Respond respond){
        Objects.nonNull(url);
        Objects.nonNull(respond);


        Task task=new Task();
        task.url=url;
        task.respond=respond;
        tasks.offer(task);

        if(worker instanceof Queuing){
            Handler handler=((Queuing) worker).getHandler();
            Message msg=Message.obtain(handler,() -> {
                Task task1=tasks.poll();
                if (task1!=null){
                    try {
                        Bitmap bitmap=realLoader.getData(task1.url);
                        task1.respond.onSucceed(bitmap);
                    }catch (Exception e){
                        task1.respond.onError(e);
                    }
                }
            });

            handler.sendMessage(msg);
        }
    }


    public void setRealLoader(ImageLoader realLoader) {
        this.realLoader = realLoader;
    }

    public interface Respond{
        void onSucceed(Bitmap bitmap);
        void onError(Throwable throwable);
    }

    public static final int WAITING=0X1;
    public static final int PREPAR=0x2;
    public static final int RUN=0x3;

    /**
     * 记录下一个加载任务的url
     * 以及等待处理任务结果的处理者
     */
    public class Task{

        /**
         * 这个任务的状态
         * 等待执行
         * 准备执行
         * 正在执行
         */


        int state=WAITING;
        String url;
        Respond respond;
    }


    static final class DefaultRespond implements Respond{

        @Override
        public void onSucceed(Bitmap bitmap) {}

        @Override
        public void onError(Throwable throwable) {}
    }

}
