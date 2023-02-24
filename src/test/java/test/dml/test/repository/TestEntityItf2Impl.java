package test.dml.test.repository;

/**
 * @author zheng chengdong
 */
public class TestEntityItf2Impl implements TestEntityItf2<TestEntityItf>{
    private String id;

    public TestEntityItf2Impl(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
