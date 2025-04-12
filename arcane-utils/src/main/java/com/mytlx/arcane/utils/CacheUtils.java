package com.mytlx.arcane.utils;

import java.io.Serial;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheUtils {

    private final static ConcurrentHashMap<Object, Map<Object, ExpireObj<?>>> map = new ConcurrentHashMap<>();

    /**
     * 根据 Provider 的类名区别缓存位置
     */
    public static <K, V> V getValue(K key, Provider<K, V> provider) {
        return getValue(provider.getClass().getName(), key, provider);
    }

    /**
     * 从命名为 cacheName 的缓存中查找 key 对应的值
     * 查询对应值添加加缓存并返回
     */
    @SuppressWarnings("unchecked")
    public static <K, V> V getValue(Object cacheName, K key, Provider<K, V> provider) {
        Map<Object, ExpireObj<?>> cacheMap = getCache(cacheName, provider.maxSize());
        ExpireObj<?> result = null;
        // FIFOLinkedHashMap 非线程安全，double check 限制单线程访问
        if ((result = cacheMap.get(key)) == null || result.isExpired()) {
            synchronized (cacheMap) {
                if ((result = cacheMap.get(key)) == null || result.isExpired()) {
                    result = new ExpireObj<V>(provider.expireIn(), provider.get());
                    cacheMap.put(key, result);
                }
            }
        }
        return (V) result.obj;
    }

    private static Map<Object, ExpireObj<?>> getCache(Object cacheName, int cacheSize) {
        return map.computeIfAbsent(cacheName, k -> new FIFOLinkedHashMap<>(cacheSize));
    }

    public static abstract class Provider<K, V> {

        public abstract V get();

        public abstract int maxSize();

        public long expireIn() {
            return 1800 * 1000;
        }
    }

    private static class ExpireObj<T> {
        final long expireTime;
        final T obj;

        public ExpireObj(long maxAvailable, T obj) {
            this.expireTime = System.currentTimeMillis() + maxAvailable;
            this.obj = obj;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    /**
     * 固定容量 FIFO 缓存，基于 LinkedHashMap 实现
     * 自动淘汰最早插入的元素
     */
    static class FIFOLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

        @Serial
        private static final long serialVersionUID = -2528998181145341846L;

        public final int MAX_ENTRIES;

        public FIFOLinkedHashMap(int maxEntries) {
            super(maxEntries + 1, 0.75f, false);
            this.MAX_ENTRIES = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K,  V> eldest) {
            return size() > MAX_ENTRIES;
        }
    }

}
