package testmodule5;

public interface TestMethod {           // Change 4
    default Report runTestCase(TestData testData, Report report) {
        throw new RuntimeException("Don't know how to run this test case!");
    }
}
