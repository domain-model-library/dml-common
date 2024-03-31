package dml.common.repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public abstract class TestCommonRepository<E, ID> implements CommonRepository<E, ID> {

    protected Map<Object, E> data = new HashMap<>();

    @Override
    public E find(ID id) {
        return data.get(id);
    }

    @Override
    public E take(ID id) {
        return data.get(id);
    }

    @Override
    public void put(E entity) {
        data.put(getId(entity), entity);
    }

    @Override
    public E putIfAbsent(E entity) {
        return data.putIfAbsent(getId(entity), entity);
    }

    @Override
    public E takeOrPutIfAbsent(ID id, E newEntity) {
        E entity = take(id);
        if (entity != null) {
            return entity;
        }
        E exists = putIfAbsent(newEntity);
        if (exists != null) {
            return exists;
        }
        return newEntity;
    }

    @Override
    public E remove(ID id) {
        return data.remove(id);
    }

    private ID getId(E entity) {
        //取名称为“id”的field作为id field，如果不存在 “id” field，那么取第一个field作为id field
        Field idField = null;
        Class entityClass = entity.getClass();
        Field[] fields = getAllFields(entityClass);
        for (Field field : fields) {
            if (field.getName().equals("id")) {
                idField = field;
                break;
            }
        }
        if (idField == null) {
            if (fields.length > 0) {
                idField = fields[0];
            } else {
                throw new RuntimeException("can not find id field in entity class " + entityClass.getName());
            }
        }
        idField.setAccessible(true);

        try {
            return (ID) idField.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("get value of idField error", e);
        }
    }

    private Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            // 获取当前类的所有声明字段，并添加到列表中
            Field[] declaredFields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(declaredFields));
            // 获取当前类的父类，准备继续获取父类的字段
            clazz = clazz.getSuperclass();
        }
        // 将列表转换为数组并返回
        return fieldList.toArray(new Field[0]);
    }

    public static <I> I instance(Class<I> itfType) {
        TestCommonRepository testRepositoryInstance = new TestCommonRepository() {
        };
        I instance = (I) Proxy.newProxyInstance(testRepositoryInstance.getClass().getClassLoader(), new Class[]{itfType},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("find".equals(method.getName())) {
                            return testRepositoryInstance.find(args[0]);
                        } else if ("take".equals(method.getName())) {
                            return testRepositoryInstance.take(args[0]);
                        } else if ("put".equals(method.getName())) {
                            testRepositoryInstance.put(args[0]);
                            return null;
                        } else if ("putIfAbsent".equals(method.getName())) {
                            return testRepositoryInstance.putIfAbsent(args[0]);
                        } else if ("takeOrPutIfAbsent".equals(method.getName())) {
                            return testRepositoryInstance.takeOrPutIfAbsent(args[0], args[1]);
                        } else if ("remove".equals(method.getName())) {
                            return testRepositoryInstance.remove(args[0]);
                        } else {
                            throw new UnsupportedOperationException(method.getName());
                        }
                    }
                });
        return instance;
    }

}
