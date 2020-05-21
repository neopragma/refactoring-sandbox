package testmodule5;

public class GuestUserCheckout_CA_EN_AMEX implements GuestUserCheckout {

    @Override
    public Report runTestCase(TestData testData, Report report) {
        report.setTestMethodInvoked("Guest checkout for Canada in English with American Express");
        return report;
    }

}
