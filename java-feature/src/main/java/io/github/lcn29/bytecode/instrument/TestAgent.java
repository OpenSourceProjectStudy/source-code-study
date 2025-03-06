package io.github.lcn29.bytecode.instrument;

import java.lang.instrument.Instrumentation;

/**
 * TODO
 *
 * @author canxin.li
 * @date 2025-03-06 16:18:07
 */
public class TestAgent {

    public static void agentmain(String args, Instrumentation inst) {
        //指定我们自己定义的Transformer，在其中利用Javassist做字节码替换
        inst.addTransformer(new TestTransformer(), true);
        try {
            //重定义类并载入新的字节码
            inst.retransformClasses(Base.class);
            System.out.println("Agent Load Done.");
        } catch (Exception e) {
            System.out.println("agent load failed!");
        }
    }
}
