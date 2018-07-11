package com.example.dishfo.loadandcache.cache;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import java.nio.ByteBuffer;

public final class MemoryCache extends LruCache<String,Bitmap>{


    public static final int KB=1024;
    public static final int MB=1024*1024;
    private final static int DEFAULT_MAX_SIZE=30*MB;

    public MemoryCache() {
        this(DEFAULT_MAX_SIZE);
    }

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public MemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount();
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        oldValue.recycle();

        Log.d("test","remove a birmap");
    }
}
