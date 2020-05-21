package testmodule5;

public class SearchStyleID_US_EN implements SearchStyleID {

    @Override
    public Report runTestCase(TestData testData, Report report) {
        report.setTestMethodInvoked("Search Style ID for United States in English");
        return report;
    }
}
