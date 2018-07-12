package com.example.webcache.app;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.URLUtil;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.dishfo.loadandcache.ContextConstant;
import com.example.dishfo.loadandcache.context.MyApplication;
import com.example.webcache.data.HtmlDateBase;

import java.io.File;

public class MyAppliacation extends MyApplication {

    private WebView webView;
    private HtmlDateBase htmlDateBase;


    @Override
    public void onCreate() {
        super.onCreate();
        htmlDateBase= Room.databaseBuilder(this, HtmlDateBase.class,ContextConstant.db_name)
        .build();



        webView=new WebView(this);

        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);


        String path=getFilesDir().getAbsolutePath()+"/Webcache/";
        Log.d("test","cache in "+path);

        File file=new File(path);
        if(!file.exists()){
            file.mkdir();
        }

        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(path);


        settings.setSupportZoom(true);


        webView.setWebViewClient(new MyWebViewClient(this));
    }

    public WebView getWebView() {
         return webView;
    }

    private static boolean appHasInstalled(Uri uri, Context context) {
        PackageManager packageManager = context.getPackageManager();
        String realurl = uri.toString();
        try {
            packageManager.getPackageInfo(uri.getScheme(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    public HtmlDateBase getHtmlDateBase(){
        return htmlDateBase;
    }

    public static class MyWebViewClient extends WebViewClient {

        private Context context;

        public MyWebViewClient(Context context) {
            this.context = context;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url=request.getUrl().toString();
            if (URLUtil.isNetworkUrl(url)){
                //尝试从本地过去
                return false;
            }


            if (appHasInstalled(request.getUrl(),context)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                context.startActivity(intent);
            } else {
                view.stopLoading();
            }


            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("test", "real " + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("test","load "+url+" finished");
        }
    }
}
