package dml.test.repository;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public abstract class TestSingletonRepository<T> {

    private T entity;

    public T get() {
        return entity;
    }

    public T take() {
        return entity;
    }

    public void put(T entity) {
        this.entity = entity;
    }

    public static <I> I instance(Class<I> itfType) {
        TypeVariable<Class<I>>[] typeVariables = itfType.getTypeParameters();
        TypeVariable<Class<I>> entityTypeVariable = typeVariables[0];
        Type[] entityTypeBounds = entityTypeVariable.getBounds();
        Type entityType = entityTypeBounds[0];
        String entityTypeDesc;
        if (entityType instanceof ParameterizedType){
            entityTypeDesc= "L" +  ( (ParameterizedType)entityType).getRawType().getTypeName().replace('.', '/') + ";";
        }else{
            entityTypeDesc = "L" + entityType.getTypeName().replace('.', '/') + ";";
        }

        String templateEntityTypeDesc = "Ldml/test/repository/TemplateEntityItf;";

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("dml/test/repository/TemplateSingletonEntityItfRepositoryImpl.class");
        byte[] bytes = new byte[0];
        try {
            bytes = new byte[is.available()];
            is.read(bytes);
        } catch (IOException e) {
            throw new RuntimeException("read TemplateSingletonEntityItfRepositoryImpl.class error", e);
        }

        String newTypeClsName = "dml.test.repository.generated." + itfType.getName();
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cr.accept(new ClassVisitor(Opcodes.ASM5, cw) {
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                interfaces[0] = itfType.getName().replace('.', '/');
                name = newTypeClsName.replace('.', '/');
                signature = signature.replaceAll(templateEntityTypeDesc, entityTypeDesc);
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public MethodVisitor visitMethod(int access, String mthName, String mthDesc, String signature, String[] exceptions) {
                mthDesc = mthDesc.replaceAll(templateEntityTypeDesc, entityTypeDesc);
                return new AdviceAdapter(Opcodes.ASM5, super.visitMethod(access, mthName, mthDesc, signature, exceptions), access, mthName, mthDesc) {
                    @Override
                    public void visitTypeInsn(final int opcode, final String type) {
                        String realType = type;
                        if (Opcodes.CHECKCAST == opcode) {
                            realType = entityType.getTypeName().replace('.', '/');
                        }
                        super.visitTypeInsn(opcode, realType);
                    }
                };
            }
        }, ClassReader.EXPAND_FRAMES);

        byte[] enhancedBytes = cw.toByteArray();
        Object[] argArray = new Object[]{newTypeClsName, enhancedBytes,
                new Integer(0), new Integer(enhancedBytes.length)};
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class cls = null;
        try {
            cls = Class.forName("java.lang.ClassLoader");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("get class for java.lang.ClassLoader error", e);
        }
        java.lang.reflect.Method method = null;
        try {
            method = cls.getDeclaredMethod(
                    "defineClass",
                    new Class[]{String.class, byte[].class, int.class, int.class});
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("get getDeclaredMethod for defineClass error", e);
        }
        method.setAccessible(true);
        try {
            method.invoke(cl, argArray);
        } catch (Exception e) {
            throw new RuntimeException("invoke defineClass error", e);
        }
        Constructor constructor = null;
        try {
            constructor = Class.forName(newTypeClsName).getDeclaredConstructor();
        } catch (Exception e) {
            throw new RuntimeException("getDeclaredConstructor for " + newTypeClsName + " error", e);
        }
        constructor.setAccessible(true);
        try {
            return (I) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("newInstance for " + newTypeClsName + " error", e);
        }
    }
}
