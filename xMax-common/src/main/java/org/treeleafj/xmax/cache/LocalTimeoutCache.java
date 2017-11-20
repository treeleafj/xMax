package org.treeleafj.xmax.cache;

import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 用于本地缓存数据一定时间,超过一定时间后去获取,则直接删除,便于重新去数据查询
 * <p>
 * Created by leaf on 2017/11/20.
 */
public class LocalTimeoutCache<K, V> {

    /**
     * 多少秒后失效
     */
    private long timeout;

    private Map<K, CacheItem> cache = new ConcurrentHashMap<>();

    public LocalTimeoutCache(long timeout) {
        this.timeout = timeout;
    }

    public LocalTimeoutCache(long timeout, TimeUnit timeUnit) {
        this.timeout = timeUnit.toSeconds(timeout);
    }

    /**
     * 往缓存中存放数据
     *
     * @param key
     * @param val
     * @return
     */
    public V put(K key, V val) {
        CacheItem<V> item = new CacheItem();
        item.setPutTime(new Date());
        item.setVal(val);
        CacheItem<V> oldItem = this.cache.put(key, item);
        if (oldItem != null) {
            return oldItem.getVal();
        }
        return null;
    }

    /**
     * 从缓存中获取数据,如果数据存放时间超过设置的timeout,则会返回null,并删除该缓存数据
     *
     * @param key
     * @return
     */
    public V get(K key) {
        CacheItem<V> item = cache.get(key);
        if (item == null) {
            return null;
        }

        long now = System.currentTimeMillis() / 1000;
        if (now - item.getPutTime().getTime() > timeout) {
            //超时
            cache.remove(key);
            return null;
        }
        return item.getVal();
    }

    @Data
    private static class CacheItem<V> {

        /**
         * 放进去本地缓存中的时间
         */
        private Date putTime;

        /**
         * 实际放进去的值
         */
        private V val;
    }
}
