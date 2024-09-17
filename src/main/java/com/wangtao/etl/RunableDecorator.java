package com.wangtao.etl;

import java.util.Map;

/**
 * @author wangtao
 * Created at 2024-09-17
 */
public class RunableDecorator implements Runnable {

    private final Map<EnhancedThreadLocal<Object>, Object> captureMap;

    private final Runnable delegate;

    private RunableDecorator(Runnable delegate) {
        this.captureMap = EnhancedThreadLocalManager.capture();
        this.delegate = delegate;
    }

    public static Runnable decorate(Runnable runnable) {
        if (runnable == null) {
            return null;
        }
        if (runnable instanceof RunableDecorator) {
            return runnable;
        }
        return new RunableDecorator(runnable);
    }

    @Override
    public void run() {
        Map<EnhancedThreadLocal<Object>, Object> backupValueMap = EnhancedThreadLocalManager.apply(captureMap);
        try {
            delegate.run();
        } finally {
            EnhancedThreadLocalManager.restore(backupValueMap);
        }
    }
}
