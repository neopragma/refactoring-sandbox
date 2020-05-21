package testmodule2;

public class Report {

    private String testCategory;
    private String testMethodInvoked;

    void assignCategory(String testCategory) {}

    void setTestMethodInvoked(String testMethodInvoked) {
        this.testMethodInvoked = testMethodInvoked;
    }

    String getTestMethodInvoked() {
        return testMethodInvoked;
    }
}
