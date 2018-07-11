package com.example.dishfo.loadandcache.util;

public class Section {
    public int head;
    public int tail;

    public Section(){
        this(0,0);
    }

    public Section(int head, int tail) {
        this.head = head;
        this.tail = tail;
    }
}
