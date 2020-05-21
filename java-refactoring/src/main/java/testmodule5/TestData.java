package testmodule5;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TestData {

    private Map<TestDataKey, Object> testData;

    public TestData() {
        testData = new HashMap<TestDataKey, Object>();
    }

    public TestData(Map<TestDataKey, Object> testData) {                       // Change 4
        this.testData = testData;
    }

    public Object get(TestDataKey key) {
        return testData.get(key);
    }
    public String getString(TestDataKey key) {                                  // Change 4
        return (String) testData.get(key);
    }
    public Locale getLocale(TestDataKey key) {                                  // Change 4
        return (Locale) testData.get(key);
    }
}
