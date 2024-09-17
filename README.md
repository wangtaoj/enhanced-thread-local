### What
一个增强的ThreadLocal实现，由于线程池场景下，线程会复用，因此JDK自带的`InheritableThreadLocal`无法满足要求。在线程池执行任务时无法获取到父线程在ThreadLocal中设置的值。

### Use
使用javaagent，增加JVM启动参数，jar包位置按实际修改
```bash
-javaagent:enhanced-thread-local-1.0.0.jar
```
目前只修改了ThreadPoolExecutor的execute方法

示例如下
```java
@Test
public void testGetAtThreadPoolExecutor() {
    EnhancedThreadLocal<String> threadLocal = new EnhancedThreadLocal<>();

    try (ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>())) {
        // 先初始化核心线程
        executor.prestartAllCoreThreads();

        threadLocal.set("更强大的ThreadLocal");

        for (int i = 0; i < 10; i++) {
            executor.execute(
                () -> System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get())
            );
        }
    }
}
```
### 细节
由于增强的是java标准库，这些类由启动类加载器(bootstrap class loader)来加载的，是无法加载自定义的`RunableDecorator`类，因此需要把jar包放到启动加载路径上

jvm命令如下
```bash
-Xbootclasspath/a:enhanced-thread-local-1.0.0.jar
```
/a后缀含义: 将指定的jar添加到启动加载路径的最后面

由于这个已经通过MANIFEST.MF文件中的Boot-Class-Path属性指定了，因此不再需要显示指定，参见maven-jar-plugin配置