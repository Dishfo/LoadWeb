package com.example.dishfo.loadandcache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dishfo.loadandcache.context.MyApplication;
import com.example.dishfo.loadandcache.data.manager.LoadEngine;
import com.example.dishfo.loadandcache.util.Section;
import com.example.dishfo.loadandcache.util.SectionList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewList;
    private MyThread thread;
    private List<UiListItem> listItems;
    private MyAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private LoadEngine loadEngine;


    //记录已加载的区间
    private SectionList sectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thread = new MyThread();
        initData();

        MyApplication application = (MyApplication) getApplication();
        loadEngine = application.getLoadEngine();

        recyclerViewList = findViewById(R.id.recyclerView_list);

        linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        recyclerViewList.setLayoutManager(linearLayoutManager);
        recyclerViewList.addItemDecoration(new MyItemDecoration(this));
        recyclerViewList.setOnScrollChangeListener(
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    thread.lastTouch = System.currentTimeMillis();
                //    Log.d("test", "the list scroller ");
                });

        recyclerViewList.setAdapter(adapter);
        thread.start();
    }


    private void initData() {
        listItems = new ArrayList<>();
        sectionList = new SectionList();
        for (int i = 0; i < ContextConstant.iamge_urls.length; i++) {
            UiListItem item = new UiListItem();
            item.isLoaded = false;
            item.url = ContextConstant.iamge_urls[i];
            listItems.add(item);

        }
        adapter = new MyAdapter(this, listItems);
    }

    @Override
    protected void onResume() {
        super.onResume();
        thread.active = true;
        thread.lastTouch = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        thread.active = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }

    //todo  如何有效的搜索与已加载的区间与当前区间有重叠的的部分
    private void loadDatas() {
        int head = linearLayoutManager.findFirstVisibleItemPosition();
        int tail = linearLayoutManager.findLastVisibleItemPosition();
        Section section = sectionList.insertSecion(new Section(head, tail));
        for (int i = section.head; i <= section.tail; i++) {
            View view = recyclerViewList.getChildAt(i);
            final int index = i;
            Log.d("test", "load image " + i);
            loadEngine.load(listItems.get(i).url, new LoadEngine.Respond() {
                @Override
                public void onSucceed(Bitmap bitmap) {
                    if (bitmap != null) {
                        listItems.get(index).isLoaded = true;
                        new Handler(Looper.getMainLooper()).post(() -> {
                            adapter.notifyItemChanged(index);
                        });
                    }
                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
        }
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UiListItem item = data.get(position);
        holder.imageView.setTag(position);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageActivity.class);
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
