package test.dml.test.repository;

public interface TestEntityItf2Repository<T extends TestEntityItf2<?>, ID> {
    T take(ID id);

    T find(ID id);

    void put(T entity);

    T putIfAbsent(T entity);

    T takeOrPutIfAbsent(ID id, T newEntity);

    T remove(ID id);
}
