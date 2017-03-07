package com.jontian.specification;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import jdk.internal.org.objectweb.asm.util.Printer;
import jdk.internal.org.objectweb.asm.util.Textifier;
import jdk.internal.org.objectweb.asm.util.TraceMethodVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongjun on 3/4/17.
 */
public class BytecodeUtil {
    protected static List<String> getMethodNameInLambdaFromBytecode(String classPath, int lineNumberInUpperLevelClass) throws NotFoundException, IOException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        CtClass pt = pool.get(classPath);
        byte[] bytecode = pt.toBytecode();
        ClassReader reader = new ClassReader(bytecode);
        ClassNode classNode = new ClassNode();

        reader.accept(classNode,0);
        @SuppressWarnings("unchecked")
        final List<MethodNode> methods = classNode.methods;
        //first method which has LineNumber >= lineNumberInUpperLevelClass
        for(MethodNode m: methods){
            InsnList inList = m.instructions;
//            System.out.println(m.name);
            if(m.name.startsWith("lambda")) {
                if(inList.size() >=6
                        && inList.get(1) instanceof LineNumberNode
                        && ((LineNumberNode) inList.get(1)).line >= lineNumberInUpperLevelClass){
                    List<String> methodNames = new ArrayList<>();
                    for(int i = 3; i< inList.size(); i++){
                        if(inList.get(i).getOpcode() == Opcodes.INVOKEVIRTUAL
                                && inList.get(i) instanceof MethodInsnNode) {
                            MethodInsnNode in = (MethodInsnNode) inList.get(i);
                            methodNames.add(in.name);
                        }
                    }
                    if(methodNames.isEmpty()){
                        throw new IllegalStateException("Illegal lambda expression");
                    }
                    return methodNames;
                }
            }
        }
        throw new IllegalStateException("Cannot find expected getter call in class: "+classPath+" line number:"+lineNumberInUpperLevelClass);
    }

    private static Printer printer = new Textifier();
    private static TraceMethodVisitor mp = new TraceMethodVisitor(printer);
    protected static String insnToString(AbstractInsnNode insn){
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }

}
