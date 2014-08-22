package com.bruce.Lottery.utils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by Bruce
 * Data 2014/8/20
 * Time 18:37.
 */
public class SoftMap<K, V> extends HashMap<K, V> {

    private HashMap<K, SoftValue<V>> temp;
    private ReferenceQueue queue;

    public SoftMap() {
        temp = new HashMap<K, SoftValue<V>>();
        queue = new ReferenceQueue(); //存放被回收UI后的空软引用
    }

    @Override
    public V put(K key, V value) {
//        return super.put(key, value);
//        SoftReference sr = new SoftReference(value);
        SoftValue sr = new SoftValue(key,value, queue);
        temp.put(key, sr);
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
//        return super.containsKey(key);

        return get(key) != null;
    }

    @Override
    public V get(Object key) {
//        return super.get(key);
        //获取之前先清除被GC的袋子和V
        clearSr();
        SoftValue<V> sr = temp.get(key);
        /**
         * 从软引用中get 时， 需要先判断是否为空，因为可能被回收了~
         */
        if (sr != null) {
            //从 SoftReference 中取得原始数据
            return sr.get();
        }
        return null;
    }

    /**
     * 清空 被GC后的空袋子（软引用）
     */
    public void clearSr() {

        //拿出一个被GC后的袋子，并删除空袋子，可循环全部移除
        SoftValue poll = (SoftValue) queue.poll();
        while(poll != null){
            poll = (SoftValue) queue.poll();
        }
    }

    /**
     * 增强版的袋子，增加了key，方便清理操作
     */
    private class SoftValue<V> extends SoftReference<V>{

        private Object key;
        public SoftValue(K key,V r, ReferenceQueue q) {
            super(r, q);
            this.key = key;
        }
    }
}
