package com.wangtao.etl;

/**
 * @author wangtao
 * Created at 2024-09-17
 */
public class EnhancedThreadLocal<T> extends ThreadLocal<T> {

    @Override
    public void set(T value) {
        super.set(value);
        EnhancedThreadLocalManager.registerInstance(this);
    }

    @Override
    public T get() {
        return super.get();
    }

    @Override
    public void remove() {
        super.remove();
        EnhancedThreadLocalManager.unRegisterInstance(this);
    }
}
