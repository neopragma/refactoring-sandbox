package testmodule;

import java.util.HashMap;
import java.util.Map;

/**
 * Stripped down class for refactoring demonstration.
 * First step: Add missing unit test cases.
 */

public class FakeModule {

    private Map<String, String> testData;
    private Report report;

    public FakeModule(Map<String, String> testData, Report report) {
        this.testData = testData;
        this.report = report;
    }

    public void getPurchaseOrderForCountry() {
        String testMethod = testData.get("TestID");
        report.assignCategory(testData.get("Test_Category"));
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
        switch(testData.get("TestID")) {
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
