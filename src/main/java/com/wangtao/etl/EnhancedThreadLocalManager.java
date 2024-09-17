package com.wangtao.etl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * @author wangtao
 * Created at 2024-09-17
 */
public class EnhancedThreadLocalManager {

    private EnhancedThreadLocalManager() {

    }

    /**
     * 用于维护每个线程中所有的EnhancedThreadLocal
     * 使用弱引用，避免EnhancedThreadLocal无法被回收
     */
    private static final ThreadLocal<WeakHashMap<EnhancedThreadLocal<Object>, Object>> INSTANCE_HOLDER = ThreadLocal.withInitial(
        WeakHashMap::new
    );

    @SuppressWarnings("unchecked")
    public static void registerInstance(EnhancedThreadLocal<?> instance) {
        WeakHashMap<EnhancedThreadLocal<Object>, Object> weakHashMap = INSTANCE_HOLDER.get();
        if (!weakHashMap.containsKey(instance)) {
            weakHashMap.put((EnhancedThreadLocal<Object>) instance, null);
        }
    }

    public static void unRegisterInstance(EnhancedThreadLocal<?> instance) {
        WeakHashMap<EnhancedThreadLocal<Object>, Object> weakHashMap = INSTANCE_HOLDER.get();
        weakHashMap.remove(instance);
    }

    public static Map<EnhancedThreadLocal<Object>, Object> capture() {
        Map<EnhancedThreadLocal<Object>, Object> map = new HashMap<>();
        for (EnhancedThreadLocal<Object> enhancedThreadLocal : INSTANCE_HOLDER.get().keySet()) {
            if (Objects.nonNull(enhancedThreadLocal)) {
                map.put(enhancedThreadLocal, enhancedThreadLocal.get());
            }
        }
        return map;
    }

    /**
     * 设置新的值，返回老的值
     */
    public static Map<EnhancedThreadLocal<Object>, Object> apply(Map<EnhancedThreadLocal<Object>, Object> captureValueMap) {
        Map<EnhancedThreadLocal<Object>, Object> map = capture();
        for (Map.Entry<EnhancedThreadLocal<Object>, Object> entry : captureValueMap.entrySet()) {
            EnhancedThreadLocal<Object> enhancedThreadLocal = entry.getKey();
            enhancedThreadLocal.set(entry.getValue());
        }
        return map;
    }

    /**
     * 恢复老的值
     */
    public static void restore(Map<EnhancedThreadLocal<Object>, Object> backupValueMap) {
        for (Map.Entry<EnhancedThreadLocal<Object>, Object> entry : backupValueMap.entrySet()) {
            EnhancedThreadLocal<Object> enhancedThreadLocal = entry.getKey();
            enhancedThreadLocal.set(entry.getValue());
        }
    }
}
