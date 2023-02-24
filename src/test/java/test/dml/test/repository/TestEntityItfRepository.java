package test.dml.test.repository;

public interface TestEntityItfRepository<T extends TestEntityItf, ID> {
    T take(ID id);

    T find(ID id);

    void put(T entity);

    T putIfAbsent(T entity);

    T takeOrPutIfAbsent(ID id, T newEntity);

    T remove(ID id);
}
