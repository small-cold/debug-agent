package me.wlchao.debug.agent;

import sun.misc.Unsafe;

import java.lang.instrument.Instrumentation;

/**
 * @author wanglichao
 * @date 2021/7/1 13:27
 */
public class AgentMainAgent {

    public static void agentmain(String args, Instrumentation inst) {
        System.err.println("agentmain");
        //指定我们自己定义的Transformer，在其中利用Javassist做字节码替换
        inst.addTransformer(new UnsafeTransformer(), true);
        try {
            //重定义类并载入新的字节码
            inst.retransformClasses(Unsafe.class);
            System.out.println("Agent Load Done.");
        } catch (Exception e) {
            System.out.println("agent load failed!");
        }
    }
}
