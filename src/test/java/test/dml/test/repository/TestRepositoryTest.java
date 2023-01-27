package test.dml.test.repository;

import dml.test.repository.TestRepository;
import dml.test.repository.TestSingletonRepository;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRepositoryTest {

    @Test
    public void test() {
        TestEntityRepository<TestEntity, Object> testEntityRepository = TestRepository.instance(TestEntityRepository.class);
        TestEntity testEntity = new TestEntityImpl("1");
        testEntityRepository.put(testEntity);
        TestEntity testEntity1 = testEntityRepository.find(testEntity.getId());
        assertEquals(testEntity1.getId(), testEntity.getId());

        TestSingletonEntityRepository<TestEntity> testSingletonEntityRepository = TestSingletonRepository.instance(TestSingletonEntityRepository.class);
        TestEntity testEntity2 = new TestEntityImpl("2");
        testSingletonEntityRepository.put(testEntity2);
        TestEntity testEntity3 = testSingletonEntityRepository.get();
        assertEquals(testEntity3.getId(), testEntity2.getId());
    }

}
