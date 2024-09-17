package com.wangtao.etl;

import org.junit.jupiter.api.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2024-09-17
 */
public class EnhancedThreadLocalTest {

    @Test
    public void testRunableDecorate() {
        EnhancedThreadLocal<String> threadLocal1 = new EnhancedThreadLocal<>();
        EnhancedThreadLocal<String> threadLocal2 = new EnhancedThreadLocal<>();

        try (ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>())) {
            // 先初始化核心线程
            executor.prestartAllCoreThreads();

            threadLocal1.set("更强大的InheritableThreadLocal1");
            threadLocal2.set("更强大的InheritableThreadLocal2");

            for (int i = 0; i < 10; i++) {
                executor.execute(RunableDecorator.decorate(
                    () -> {
                        System.out.println(Thread.currentThread().getName() + ": " + threadLocal1.get());
                        System.out.println(Thread.currentThread().getName() + ": " + threadLocal2.get());
                    }
                ));
            }
        }
    }
}
