package me.wlchao.debug.agent;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.instrument.Instrumentation;

/**
 * @author wanglichao
 * @date 2021/7/1 12:49
 */
@Slf4j
public class PreMainAgent {


    public static void premain(String agentArgs, Instrumentation inst) {
        System.err.println("PreMainAgent 我在main启动之前启动");
        //指定我们自己定义的Transformer，在其中利用Javassist做字节码替换
        inst.addTransformer(new UnsafeTransformer());
    }

}
