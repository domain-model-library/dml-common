package dml.test.repository;

public interface TemplateSingletonEntityItfRepository<T extends TemplateEntityItf> {
    T get();

    T take();

    void put(T entity);
}
