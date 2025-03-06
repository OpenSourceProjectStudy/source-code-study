package io.github.lcn29.bytecode.java.ssist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;

/**
 * TODO
 *
 * @author canxin.li
 * @date 2025-03-06 15:47:46
 */
public class JavassistTest {

    public static void main(String[] args) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, IOException {

        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("io.github.lcn29.java.ssist.Base");
        CtMethod m = cc.getDeclaredMethod("process");
        m.insertBefore("{ System.out.println(\"start\"); }");
        m.insertAfter("{ System.out.println(\"end\"); }");
        Class<?> c = cc.toClass();
        cc.writeFile("/Users/lcn/Projects/Java/Git/source-code-study/java-feature/target/classes");

        Base h = (Base) c.newInstance();
        h.process();
    }
}
