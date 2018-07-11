package com.example.dishfo.loadandcache;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.solver.Cache;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.dishfo.loadandcache.context.MyApplication;
import com.example.dishfo.loadandcache.data.manager.LoadEngine;

import java.util.LinkedHashMap;

public class ImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private String url;
    private LoadEngine loadEngine;
    private Bitmap bitmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imageView=findViewById(R.id.imageView2);


        MyApplication application= (MyApplication) getApplication();
        loadEngine=application.getLoadEngine();

        url=getIntent().getStringExtra(ContextConstant.str_url);
        loadEngine.load(url, new LoadEngine.Respond() {
            @Override
            public void onSucceed(Bitmap bitmap) {

                new Handler(Looper.getMainLooper()).post(() -> imageView.setImageBitmap(bitmap));
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("test","image load error\n"+throwable.toString());
            }
        });

    }
}
