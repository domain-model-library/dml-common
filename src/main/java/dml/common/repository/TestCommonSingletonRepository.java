package dml.common.repository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class TestCommonSingletonRepository<T> implements CommonSingletonRepository<T> {

    protected T entity;

    @Override
    public T get() {
        return entity;
    }

    @Override
    public T take() {
        return entity;
    }

    @Override
    public void put(T entity) {
        this.entity = entity;
    }

    @Override
    public T remove() {
        T temp = this.entity;
        this.entity = null;
        return temp;
    }

    @Override
    public T putIfAbsent(T entity) {
        if (this.entity == null) {
            this.entity = entity;
            return null;
        } else {
            return this.entity;
        }
    }

    @Override
    public T takeOrPutIfAbsent(T newEntity) {
        if (this.entity == null) {
            this.entity = newEntity;
            return newEntity;
        } else {
            return this.entity;
        }
    }

    public static <I> I instance(Class<I> itfType) {
        return instanceProxy(itfType, new TestCommonSingletonRepository() {
        });
    }

    public static <I> I instance(Class<I> itfType, Object entity) {
        TestCommonSingletonRepository testSingletonRepository = new TestCommonSingletonRepository() {
        };
        testSingletonRepository.put(entity);
        return instanceProxy(itfType, testSingletonRepository);
    }

    private static <I> I instanceProxy(Class<I> itfType, TestCommonSingletonRepository testSingletonRepository) {
        I instance = (I) Proxy.newProxyInstance(testSingletonRepository.getClass().getClassLoader(), new Class[]{itfType},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("get".equals(method.getName())) {
                            return testSingletonRepository.get();
                        } else if ("take".equals(method.getName())) {
                            return testSingletonRepository.take();
                        } else if ("put".equals(method.getName())) {
                            testSingletonRepository.put(args[0]);
                            return null;
                        } else if ("remove".equals(method.getName())) {
                            return testSingletonRepository.remove();
                        } else if ("putIfAbsent".equals(method.getName())) {
                            return testSingletonRepository.putIfAbsent(args[0]);
                        } else if ("takeOrPutIfAbsent".equals(method.getName())) {
                            return testSingletonRepository.takeOrPutIfAbsent(args[0]);
                        } else {
                            throw new UnsupportedOperationException(method.getName());
                        }
                    }
                });
        return instance;
    }
}
