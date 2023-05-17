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
                        } else {
                            throw new UnsupportedOperationException(method.getName());
                        }
                    }
                });
        return instance;
    }
}
