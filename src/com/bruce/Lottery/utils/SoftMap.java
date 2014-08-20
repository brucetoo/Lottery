package com.bruce.Lottery.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by Bruce
 * Data 2014/8/20
 * Time 18:37.
 */
public class SoftMap<K,V> extends HashMap<K,V> {

    private HashMap<K,SoftReference<V>> temp;
    public SoftMap() {
           temp = new HashMap<K, SoftReference<V>>();
    }

    @Override
    public V put(K key, V value) {
//        return super.put(key, value);
        SoftReference sr = new SoftReference(value);
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
        SoftReference<V> sr = temp.get(key);
        /**
         * 从软引用中get 时， 需要先判断是否为空，因为可能被回收了~
         */
        if(sr != null){
            //从 SoftReference 中取得原始数据
          return sr.get();
        }
        return null;
    }
}
