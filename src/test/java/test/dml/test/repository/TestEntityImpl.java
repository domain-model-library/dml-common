package test.dml.test.repository;

public class TestEntityImpl implements TestEntity {
    private String id;

    public TestEntityImpl(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}