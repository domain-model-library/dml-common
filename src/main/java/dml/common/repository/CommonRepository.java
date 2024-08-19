package dml.common.repository;

public interface CommonRepository<E, ID> {
    E take(ID id);

    E find(ID id);

    void put(E entity);

    E putIfAbsent(E entity);

    E takeOrPutIfAbsent(ID id, E entity);

    E remove(ID id);
}
