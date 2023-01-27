package test.dml.test.repository;

public interface TestSingletonEntityRepository<T extends TestEntity> {
    T get();

    T take();

    void put(T entity);
}
