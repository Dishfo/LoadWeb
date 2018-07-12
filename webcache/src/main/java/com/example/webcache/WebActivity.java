package com.example.webcache;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.dishfo.loadandcache.ContextConstant;
import com.example.webcache.app.MyAppliacation;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        relativeLayout=findViewById(R.id.container);
        MyAppliacation appliacation= (MyAppliacation) getApplication();
        webView=appliacation.getWebView();
        LinearLayout.LayoutParams layoutParams=new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(layoutParams);
        relativeLayout.addView(webView);
        Intent intent=getIntent();
        String url=null;
        if(intent!=null){
            url=intent.getStringExtra(ContextConstant.str_url);
        }

        Log.d("test",url);

        String finalUrl = url;

        if(url!=null){
            webView.loadUrl(url);
        }
    }

    private boolean appHasInstalled(Uri uri){
        PackageManager packageManager=getPackageManager();
        String realurl=uri.toString();
        try {
            packageManager.getPackageInfo(uri.getScheme(),PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.loadUrl("about:blank");
        relativeLayout.removeView(webView);

    }
}
