package com.jontian.specification;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by zhongjun on 3/4/17.
 */
public class BytecodeUtil {
    Map<Integer,String> lineNumberAndMethodNameMap = new HashMap<>();
    protected <T> List<String> getMethodNameInLambdaFromBytecode(String classPath, int lineNumberOfLambda) throws NotFoundException, IOException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        CtClass pt = pool.get(classPath);
        byte[] bytecode = pt.toBytecode();
        ClassReader reader = new ClassReader(bytecode);
        ClassNode classNode = new ClassNode();

        reader.accept(classNode,0);
        final List<MethodNode> methods = classNode.methods;
        //first method which has LineNumber >= lineNumberOfLambda
        methods.sort( (m1,m2) -> m1.name.compareTo(m2.name));
        List<String> methodNames = findNextOfLambdaMethodOf(lineNumberOfLambda, methods);
        if (methodNames != null) return methodNames;
        throw new IllegalStateException("Cannot find expected getter call in class: "+classPath+" line number:"+lineNumberOfLambda);
    }

    private List<String> findNextOfLambdaMethodOf(int lineNumber, List<MethodNode> methods) {
        String lastLambdaMethodName = lineNumberAndMethodNameMap.get(lineNumber);
        lastLambdaMethodName = lastLambdaMethodName != null ? lastLambdaMethodName : "";
        for(MethodNode m: methods){
            InsnList inList = m.instructions;
            AbstractInsnNode lineNumberNode = inList.get(1);
            if(m.name != null && m.name.startsWith("lambda$lambda$") && m.name.compareTo(lastLambdaMethodName) > 0) {
                if (lineNumberNode instanceof LineNumberNode
                        && ((LineNumberNode) lineNumberNode).line >= lineNumber) {
                    List<String> methodNames = new ArrayList<>();
                    for (int i = 3; i < inList.size(); i++) {
                        if (inList.get(i).getOpcode() == Opcodes.INVOKEVIRTUAL
                                && inList.get(i) instanceof MethodInsnNode) {
                            MethodInsnNode in = (MethodInsnNode) inList.get(i);
                            methodNames.add(in.name);
                        }
                    }
                    if (methodNames.isEmpty()) {
                        throw new IllegalStateException("Illegal lambda expression");
                    }
                    lineNumberAndMethodNameMap.put(lineNumber, m.name);
                    return methodNames;
                }else{
                    throw new IllegalStateException("Illegal lambda expression");
                }
            }
        }
        return null;
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
