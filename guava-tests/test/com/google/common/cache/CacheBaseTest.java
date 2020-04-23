package com.google.common.cache;

import junit.framework.TestCase;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 我的私人测试
 */
public class CacheBaseTest extends TestCase {


    public void testCache() {
        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                .recordStats()
                .build(new CacheLoader<Integer, String>() {
                    @Override
                    public String load(Integer key) throws Exception {
                        return key + "";
                    }
                });

        System.out.println("Test 1");
        String value = cache.getUnchecked(1);
    }

    /**
     * 访问后自动失效
     * @throws InterruptedException
     */
    public void testExpireCache() throws InterruptedException {
        final AtomicInteger count = new AtomicInteger();
        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                .recordStats()
                .expireAfterAccess(Duration.ofSeconds(1))
                .build(new CacheLoader<Integer, String>() {
                    @Override
                    public String load(Integer key) throws Exception {
                        count.incrementAndGet();
                        return key + "";
                    }
                });
        cache.getUnchecked(1);
        cache.getUnchecked(1);
        Thread.sleep(1000);
        cache.getUnchecked(1);
        assertEquals(2, count.get());
    }


    public void testPostReadCleanup() {
        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                .recordStats()
                .expireAfterAccess(Duration.ofSeconds(1))
                .build(new CacheLoader<Integer, String>() {
                    @Override
                    public String load(Integer key) throws Exception {
                        return key + "";
                    }
                });
        for (int i = 0; i < 1000; i++) {
            cache.getUnchecked(i);
        }
    }

}
