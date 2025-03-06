package io.github.lcn29.bytecode.asm;

/**
 * TODO
 *
 * @author canxin.li
 * @date 2025-03-06 15:29:57
 */
public class TestBase {

    public static void main(String[] args) {
        // 先跑一遍 io.github.lcn29.asm.function.Generator.main 方法，生成增强后的 Base 类
        // 然后再跑 io.github.lcn29.asm.Base.main 方法，看效果
        Base base = new Base();
        base.process();
    }

}
