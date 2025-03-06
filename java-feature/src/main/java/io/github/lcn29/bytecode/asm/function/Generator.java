package io.github.lcn29.bytecode.asm.function;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * TODO
 *
 * @author canxin.li
 * @date 2025-03-06 15:04:19
 */
public class Generator {

    public static void main(String[] args) throws IOException {
        //读取
        ClassReader classReader = new ClassReader("io/github/lcn29/bytecode/asm/Base");
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //处理
        ClassVisitor classVisitor = new MyClassVisitor(classWriter);
        classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
        byte[] data = classWriter.toByteArray();
        //输出
        File f = new File("/Users/lcn/Projects/Java/Git/source-code-study/java-feature/target/classes/io/github/lcn29/asm/Base.class");
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(data);
        fout.close();
        System.out.println("now generator cc success!!!!!");
    }
}
