package dml.common.repository;

public interface CommonSingletonRepository<E> {
    E get();

    E take();

    void put(E entity);

    E putIfAbsent(E entity);

    E takeOrPutIfAbsent(E newEntity);
    
    E remove();
}
