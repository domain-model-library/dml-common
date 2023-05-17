package dml.common.repository;

public interface CommonSingletonRepository<E> {
    E get();

    E take();

    void put(E var1);
}
