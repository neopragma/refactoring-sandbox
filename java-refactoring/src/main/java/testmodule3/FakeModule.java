package testmodule3;

import java.util.Map;

/**
 * Second pass at refactoring the code.
 * 2. Replace String values with explicit types.
 * 2.1. Define an enum for test data key values.
 * 2.2. Define a class for test data.
 */

public class FakeModule {

    private TestData testData;
    private Report report;

    public FakeModule(TestData testData, Report report) {                   // Change 2.2
        this.testData = testData;
        this.report = report;
    }

    public void getPurchaseOrderForCountry() {
        String testMethod = testData.get(TestDataKey.TEST_METHOD);         // Change 2.1
        report.assignCategory(testData.get(TestDataKey.TEST_CATEGORY));    // Change 2.1
        switch(testMethod) {
            case "Guest User-Checkout-CA-EN-CC":
                purchaseOrderForCountry_ENwithCards(testData, report);
                break;
            case "Guest User-Checkout-CA-EN-MC":
                purchaseOrderForCountry_ENwithCards(testData, report);
                break;
            case "Guest User-Checkout-CA-EN-AMEX":
                purchaseOrderForCountry_ENwithCards(testData, report);
            case "Search-styleID_US_EN":
                Search_styleID_US_EN(testData, report);
                break;
            default:
                throw new RuntimeException("Undefined test method");
        }
    }

    void purchaseOrderForCountry_ENwithCards(TestData testData, Report report) {       // Change 2.2
        switch(testData.get(TestDataKey.TEST_METHOD)) {                                // Change 2.1
            case "Guest User-Checkout-CA-EN-CC":
                report.setTestMethodInvoked("Guest checkout for Canada in English with Credit Card");
                break;
            case "Guest User-Checkout-CA-EN-MC":
                report.setTestMethodInvoked("Guest checkout for Canada in English with MasterCard");
                break;
            case "Guest User-Checkout-CA-EN-AMEX":
                report.setTestMethodInvoked("Guest checkout for Canada in English with American Exrpress");
            case "Search-styleID_US_EN":
                report.setTestMethodInvoked("Search styleID for US in English");
                break;
            default:
                throw new RuntimeException("Undefined test method");
        }
    }
    void Search_styleID_US_EN(TestData testData, Report report) {}            // Change 2.2
}
