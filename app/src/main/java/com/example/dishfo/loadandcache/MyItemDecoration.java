package com.example.dishfo.loadandcache;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class MyItemDecoration extends RecyclerView.ItemDecoration {

    private static final int d_res =R.drawable.recyclerview_decoration;
    private static final int marign=4;
    private Drawable mdivider=null;

    private Context context;

    public MyItemDecoration(Context context) {
        this.context = context;
        mdivider=context.getTheme().getDrawable(d_res);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int count=parent.getChildCount();

        for (int i=0;i<count-1;i++){
            View view=parent.getChildAt(i);
            Rect rect=new Rect();
            rect.set(view.getLeft(),view.getBottom(),view.getRight(),
                    view.getBottom()+mdivider.getIntrinsicHeight());
            mdivider.setBounds(rect);
            mdivider.draw(c);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom=marign;

    }
}
