package dml.test.repository;

public interface TemplateEntityItfRepository<T extends TemplateEntityItf, ID> {
    T take(ID id);

    T find(ID id);

    void put(T entity);

    T putIfAbsent(T entity);

    T takeOrPutIfAbsent(ID id, T newEntity);

    T remove(ID id);
}
