package testmodule4;

import java.util.HashMap;
import java.util.Map;

public class TestData {                          // Change 2.2

    private Map<TestDataKey, String> testData;

    public TestData() {
        testData = new HashMap<TestDataKey, String>();
    }

    public TestData(Map<TestDataKey, String> testData) {  // Change 2.2 (for dependency injection)
        this.testData = testData;
    }

    public String get(TestDataKey key) {
        return testData.get(key);
    }
}
