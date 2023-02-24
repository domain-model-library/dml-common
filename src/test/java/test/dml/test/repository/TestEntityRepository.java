package test.dml.test.repository;

public interface TestEntityRepository {
    TestEntity take(String id);

    TestEntity find(String id);

    void put(TestEntity entity);

    TestEntity putIfAbsent(TestEntity entity);

    TestEntity takeOrPutIfAbsent(String id, TestEntity newEntity);

    TestEntity remove(String id);
}
