/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;


/**
 *@author Jon (Zhongjun Tian)
 */
public class BytecodeParser {
    private static Logger logger = LoggerFactory.getLogger(BytecodeParser.class);
    public static final int LINE_NUMBER_NODE_INDEX = 1;
    public static final int FIRST_INVOKEVITUAL_INDEX = 3;
    public static final String BYTECODE_LAMBDA_METHOD_PREFIX = "lambda$";
    Map<Integer,String> lineNumberAndMethodNameMap = new HashMap<>();

    public String getPathFromCallStack() {
        StackTraceElement elementWhichCallsThisMethod = Thread.currentThread().getStackTrace()[3];
        int lineNumberInUpperLevelClass = elementWhichCallsThisMethod.getLineNumber();
        List<String> getterNames = Collections.emptyList();
        try {
            getterNames = getMethodNameInLambdaFromBytecode(elementWhichCallsThisMethod.getClassName(),
                    elementWhichCallsThisMethod.getMethodName(),lineNumberInUpperLevelClass);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        logger.info("Found getter in lambda expression: "+getterNames.toString());
        return toAttributePath(getterNames);
    }

    protected <T> List<String> getMethodNameInLambdaFromBytecode(String classPath, String methodName, int lineNumberOfLambda) throws NotFoundException, IOException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        CtClass pt = pool.get(classPath);
        byte[] bytecode = pt.toBytecode();
        ClassReader reader = new ClassReader(bytecode);
        ClassNode classNode = new ClassNode();

        reader.accept(classNode,0);
        final List<MethodNode> methods = classNode.methods;
        //first method which has LineNumber >= lineNumberOfLambda
        methods.sort( (m1,m2) -> m1.name.compareTo(m2.name));
        List<String> methodNames = findNextOfLambdaMethodOf(lineNumberOfLambda, methodName, methods);
        if (methodNames != null) return methodNames;
        throw new IllegalStateException("Cannot find expected getter call in class: "+classPath+" line number:"+lineNumberOfLambda);
    }
    /*
        Bytecode Example


  private static synthetic lambda$lambda$1(Lcom/jontian/demo/db/Person;)Ljava/lang/Object;
   L0
    LINENUMBER 46 L0
    ALOAD 0
    INVOKEVIRTUAL com/jontian/demo/db/Person.getAddress ()Lcom/jontian/demo/db/Address;
    INVOKEVIRTUAL com/jontian/demo/db/Address.getCity ()Ljava/lang/String;
    ARETURN
   L1
    LOCALVARIABLE p Lcom/jontian/demo/db/Person; L0 L1 0
    MAXSTACK = 1
    MAXLOCALS = 1

     */
    private List<String> findNextOfLambdaMethodOf(int lineNumber, String methodName, List<MethodNode> methods) {
        String lastLambdaMethodName = lineNumberAndMethodNameMap.get(lineNumber);
        lastLambdaMethodName = lastLambdaMethodName != null ? lastLambdaMethodName : "";
        for(MethodNode m: methods){
            InsnList inList = m.instructions;
            AbstractInsnNode lineNumberNode = inList.get(LINE_NUMBER_NODE_INDEX);
            if(m.name != null && m.name.startsWith(BYTECODE_LAMBDA_METHOD_PREFIX +methodName)
                    && m.name.compareTo(lastLambdaMethodName) > 0) {
                if (lineNumberNode instanceof LineNumberNode
                        && ((LineNumberNode) lineNumberNode).line >= lineNumber) {
                    List<String> methodNames = new ArrayList<>();
                    for (int i = FIRST_INVOKEVITUAL_INDEX; i < inList.size(); i++) {
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
                }
            }
        }
        return null;
    }

    private String toAttributePath(List<String> getterNames) {
        StringBuilder sb = new StringBuilder();
        for(String getterName : getterNames) {
            if (!getterName.startsWith("get")) {
                throw new IllegalStateException();
            }
            String attributeName = getterName.substring(3);
            attributeName = Character.toLowerCase(attributeName.charAt(0)) + attributeName.substring(1);
            sb.append(Filter.PATH_DELIMITER+attributeName);
        }
        return sb.toString().substring(Filter.PATH_DELIMITER.length());
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
