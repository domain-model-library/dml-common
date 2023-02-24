package test.dml.test.repository;

import dml.test.repository.TestRepository;
import dml.test.repository.TestSingletonRepository;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRepositoryTest {

    @Test
    public void test() {
        TestEntityItfRepository<TestEntityItf, Object> testEntityItfRepository = TestRepository.instance(TestEntityItfRepository.class);
        TestEntityItf testEntityItf = new TestEntityItfImpl("1");
        testEntityItfRepository.put(testEntityItf);
        TestEntityItf testEntityItf1 = testEntityItfRepository.find(testEntityItf.getId());
        assertEquals(testEntityItf1.getId(), testEntityItf.getId());

        TestEntityItf2Repository<TestEntityItf2<?>, Object> testEntityItf2Repository = TestRepository.instance(TestEntityItf2Repository.class);
        TestEntityItf2 testEntityItf21 = new TestEntityItf2Impl("1");
        testEntityItf2Repository.put(testEntityItf21);
        TestEntityItf testEntityItf22 = testEntityItfRepository.find(testEntityItf21.getId());
        assertEquals(testEntityItf22.getId(), testEntityItf21.getId());

        TestSingletonItfEntityRepository<TestEntityItf> testSingletonItfEntityRepository = TestSingletonRepository.instance(TestSingletonItfEntityRepository.class);
        TestEntityItf testEntityItf2 = new TestEntityItfImpl("2");
        testSingletonItfEntityRepository.put(testEntityItf2);
        TestEntityItf testEntityItf3 = testSingletonItfEntityRepository.get();
        assertEquals(testEntityItf3.getId(), testEntityItf2.getId());
    }

}
