package testmodule2;

import java.util.Map;

/**
 * First pass at refactoring the code.
 * 1. Make names more consistent.
 * 1.1. Change "TestID" to "testMethod" so that all references to the same thing have the same name.
 * 1.2. Use consistent camel case capitalization scheme consistent with Java conventions.
 *     "Test_Category" -> "testCategory"
 */

public class FakeModule {

    private Map<String, String> testData;
    private Report report;

    public FakeModule(Map<String, String> testData, Report report) {
        this.testData = testData;
        this.report = report;
    }

    public void getPurchaseOrderForCountry() {
        String testMethod = testData.get("testMethod");                     // Change 1.1.
        report.assignCategory(testData.get("testCategory"));                // Change 1.2.
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

    void purchaseOrderForCountry_ENwithCards(Map<String, String> testData, Report report) {
        switch(testData.get("testMethod")) {
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
    void Search_styleID_US_EN(Map<String, String> testData, Report report) {}
}
