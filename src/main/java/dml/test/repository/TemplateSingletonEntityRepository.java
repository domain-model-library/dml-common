package dml.test.repository;

public interface TemplateSingletonEntityRepository<T extends TemplateEntity> {
    T get();

    T take();

    void put(T entity);
}
