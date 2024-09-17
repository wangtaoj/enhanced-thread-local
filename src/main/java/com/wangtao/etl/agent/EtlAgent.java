package com.wangtao.etl.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author wangtao
 * Created at 2024-09-17
 */
public class EtlAgent {

    /**
     * agent 入口函数
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        // 向 Instrumentation 添加一个 ClassFileTransformer
        inst.addTransformer(new ExecutorTransformer());
    }
}
