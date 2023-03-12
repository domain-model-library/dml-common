package dml.test.repository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class TestSingletonRepository<T> {

    protected T entity;

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
        TestSingletonRepository testSingletonRepository = new TestSingletonRepository() {
        };
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
