package dml.test.repository;

/**
 * @author zheng chengdong
 */
public interface TemplateEntityRepository {
    TemplateEntity take(String id);

    TemplateEntity find(String id);

    void put(TemplateEntity entity);

    TemplateEntity putIfAbsent(TemplateEntity entity);

    TemplateEntity takeOrPutIfAbsent(String id, TemplateEntity newEntity);

    TemplateEntity remove(String id);
}
