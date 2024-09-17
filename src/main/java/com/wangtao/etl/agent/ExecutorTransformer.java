package com.wangtao.etl.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * @author wangtao
 * Created at 2024-09-17
 */
public class ExecutorTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {

        if (!className.equals("java/util/concurrent/ThreadPoolExecutor")) {
            return null;
        }
        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.get("java.util.concurrent.ThreadPoolExecutor");
            // 获取你要修改的目标方法
            CtMethod method = ctClass.getDeclaredMethod("execute");

            // 使用 Javassist 修改参数, 包装runable参数
            method.insertBefore("{ $1 = com.wangtao.etl.RunableDecorator.decorate($1); }");
            return ctClass.toBytecode();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
