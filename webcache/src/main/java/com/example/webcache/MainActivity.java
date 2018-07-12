package com.example.webcache;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dishfo.loadandcache.ContextConstant;
import com.example.dishfo.loadandcache.MyItemDecoration;
import com.example.dishfo.loadandcache.UiListItem;
import com.example.dishfo.loadandcache.util.Section;
import com.example.dishfo.loadandcache.util.SectionList;
import com.example.webcache.app.MyAppliacation;
import com.example.webcache.cache.LocaDataRepository;
import com.example.webcache.data.HtmlDateBase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewList;
    private List<UiListItem> listItems;
    private MyAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    MyThread thread;
    WebView webView;
    private HtmlDateBase htmlDateBase;
    private LocaDataRepository repository;

    private RequestQueue queue;

    //记录已加载的区间
    private SectionList sectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();

        repository=new LocaDataRepository();
        queue=Volley.newRequestQueue(getApplication());

        thread=new MyThread();
        thread.start();

        MyAppliacation myAppliacation= (MyAppliacation) getApplication();
        htmlDateBase=myAppliacation.getHtmlDateBase();
        webView=myAppliacation.getWebView();

        recyclerViewList = findViewById(R.id.recyclerView_list);

        linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        recyclerViewList.setLayoutManager(linearLayoutManager);
        recyclerViewList.addItemDecoration(new MyItemDecoration(this));

        recyclerViewList.setAdapter(adapter);

        recyclerViewList.setOnScrollChangeListener(
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    thread.lastTouch = System.currentTimeMillis();
                        Log.d("test", "the list scroller ");
                });
    }

    private void initData() {
        listItems = new ArrayList<>();
        sectionList = new SectionList();
        for (int i = 0; i < ContextConstant.urls.length; i++) {
            UiListItem item = new UiListItem();
            item.isLoaded = false;
            item.url = ContextConstant.urls[i];
            listItems.add(item);
        }
        adapter = new MyAdapter(this, listItems);
    }

    //todo  如何有效的搜索与已加载的区间与当前区间有重叠的的部分
    private void loadDatas() {
        int head = linearLayoutManager.findFirstVisibleItemPosition();
        int tail = linearLayoutManager.findLastVisibleItemPosition();
        Section section = sectionList.insertSecion(new Section(head, tail));
        for (int i = section.head; i <= section.tail; i++) {
            webView.loadUrl(listItems.get(i).url);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        thread.active=true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        thread.active=false;
        webView.loadUrl("about:blank");
    }

    static final class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<UiListItem> data;
        private Context context;

        MyAdapter(Context context, List<UiListItem> data) {
            this.context = context;
            this.data = data;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

            return new MyViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            UiListItem item = data.get(position);
            holder.imageView.setTag(position);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra(ContextConstant.str_url, item.url);
                context.startActivity(intent);
            });
            holder.textView.setText(position + ":");

            if (item.isLoaded) {
                holder.imageView.setVisibility(View.VISIBLE);
            } else {
                holder.imageView.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }
    }

    static final class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    private class MyThread extends Thread {

        private volatile long lastTouch = 0;

        private volatile boolean active = false;

        @Override
        public void run() {
            lastTouch = System.currentTimeMillis();

            while (!isInterrupted()) {
                if (!active) {
                    try {
                        sleep(100);
                        continue;
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                if (System.currentTimeMillis() - lastTouch > 3000) {
                    lastTouch = System.currentTimeMillis();
                    toLoadBackGround();
                }


                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

        private void toLoadBackGround() {
            new Handler(getMainLooper()).post(MainActivity.this::loadDatas);
        }
    }



}
