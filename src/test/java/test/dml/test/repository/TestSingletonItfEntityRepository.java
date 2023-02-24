package test.dml.test.repository;

public interface TestSingletonItfEntityRepository<T extends TestEntityItf> {
    T get();

    T take();

    void put(T entity);
}
