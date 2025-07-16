package com.springleaf.mybatis.cache.decorators;

import com.springleaf.mybatis.cache.Cache;

import java.util.Deque;
import java.util.LinkedList;

/**
 * FIFO (first in, first out) cache decorator
 */
public class FifoCache implements Cache {

    // 被装饰的底层Cache对象
    private final Cache delegate;
    // 用于记录Key进入缓存的先后顺序
    private Deque<Object> keyList;
    // 记录的是缓存项的上限，超过该值，则需要清理最老的缓存
    private int size;

    public FifoCache(Cache delegate) {
        this.delegate = delegate;
        this.keyList = new LinkedList<>();
        this.size = 1024;
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public int getSize() {
        return delegate.getSize();
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void putObject(Object key, Object value) {
        cycleKeyList(key);  // 检测并清理缓存
        delegate.putObject(key, value);     // 添加缓存项
    }

    @Override
    public Object getObject(Object key) {
        return delegate.getObject(key);
    }

    @Override
    public Object removeObject(Object key) {
        return delegate.removeObject(key);
    }

    @Override
    public void clear() {
        delegate.clear();
        keyList.clear();
    }

    private void cycleKeyList(Object key) {
        keyList.addLast(key);
        if (keyList.size() > size) {    // 如果达到缓存上限，则清理最老的缓存项
            Object oldestKey = keyList.removeFirst();
            delegate.removeObject(oldestKey);
        }
    }

}
