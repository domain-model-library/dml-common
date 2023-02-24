package test.dml.test.repository;

public class TestEntityItfImpl implements TestEntityItf {
    private String id;

    public TestEntityItfImpl(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
