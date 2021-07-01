package me.wlchao.debug.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author wanglichao
 * @date 2021/7/1 12:53
 */
@Slf4j
public class UnsafeTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println("Transforming " + className);
        if ("java/nio/DirectByteBuffer".equals(className)) {
            return null;
        }
        if ("java/nio/DirectByteBuffer$Deallocator".equals(className)) {
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get("java/nio/DirectByteBuffer$Deallocator".replaceAll("/", "."));
                CtConstructor[] ctConstructors = cc.getConstructors();
                for (CtConstructor ctConstructor : ctConstructors) {
                    ctConstructor
                            .insertBefore("{ System.out.println(\"capacity=\" + capacity + \" size=\" + size + \" address=\" + address); }");
                    ctConstructor.insertAfter("{ " +
                            " System.out.println(\"capacity=\" + capacity + \" size=\" + size + \" address=\" + address);" +
                            " StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();\n" +
                            " for(int i=0;i< stackTraceElements.length;i++){" +
                            " StackTraceElement element = stackTraceElements[i];" +
                            "   System.out.println(element.getClassName() + \".\" + element.getMethodName()+ \":\" + element.getLineNumber()); " +
                            " }" +
                            " System.out.println(\"DirectByteBuffer----end----\\n\\n\\n\");" +
                            "}");
                }
                // CtMethod m = cc.getDeclaredMethod("allocateMemory");
                // m.insertBefore("{ System.out.println(\"start\"); }");
                // m.insertAfter("{ System.out.println(\"end\"); }");
                return cc.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        // if (className.equals("DirectByteBuffer"))

        return null;
    }
}
