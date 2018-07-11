package com.example.dishfo.loadandcache.data;

/**
 * 使用url 为键取出数据
 */
public interface DataRepository<T,K> {
    T getData(K key);
}
