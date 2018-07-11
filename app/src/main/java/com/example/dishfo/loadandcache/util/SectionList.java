package com.example.dishfo.loadandcache.util;


import java.util.LinkedList;
import java.util.ListIterator;

/**
 * 不是线程安全
 */

public class SectionList {


    private LinkedList<Section> list=new LinkedList<>();
    //插入一个section 并且返回没有重叠的区间值

    /**
     *
     * @param section 区间的长度是一个定长
     * @return 未重叠的区间
     */
    //插入一个section 并且返回没有重叠的区间值
    public Section insertSecion(Section section){
        Section res=new Section();

        ListIterator<Section> listIterator=list.listIterator();
        while (listIterator.hasNext()){
            Section var0=listIterator.next();

            if(var0.tail>=section.head){
                //可能有4种情况
                if(section.head>=var0.head){
                    if(section.tail<=var0.tail){
                        res.tail=-1;
                        res.head=0;
                        return res;
                    }else {
                        //检验是否会链接后面的区间
                        if(!listIterator.hasNext()){
                            res.head=var0.tail+1;
                            res.tail=section.tail;
                            var0.tail=res.tail;
                            return res;
                        }else {
                            Section var1=listIterator.next();
                            if(var1.head<=section.tail){
                                //发生链接
                                res.head=var0.tail+1;
                                res.tail=var1.head-1;
                                var0.tail=var1.tail;
                                listIterator.remove();
                                return res;
                            }else {
                                res.tail=section.tail;
                                res.head=var0.tail+1;
                                var0.tail=res.tail;
                                return res;
                            }
                        }
                    }
                }else {
                    res.head=section.head;
                    res.tail=var0.head-1;
                    var0.head=res.head;
                    return res;
                }
            }
        }


        list.add(section);
        res.head=section.head;
        res.tail=section.tail;

        return res;
    }

    /**
     * 用于纠正两个区间重叠的情况
     * @param iterator
     * @param cur
     */
    private Section correct(ListIterator<Section> iterator,Section cur,Section toinsert){
        Section pre= iterator.previous();
        Section res=new Section();
        if(pre.tail>=toinsert.head){
            res.head=pre.tail+1;
            res.tail=cur.head-1;
            pre.tail=cur.tail;
            iterator.next();
            iterator.remove();
        }else {
            res.head=toinsert.head;
            res.tail=cur.head-1;
            cur.head=res.head;
        }

        return res;
    }

}
