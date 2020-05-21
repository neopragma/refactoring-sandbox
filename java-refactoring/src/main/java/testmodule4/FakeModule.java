package testmodule4;

/**
 * Third pass at refactoring the code.
 * 3. Prepare to break dependencies by passing and returning testData and report references
 *    instead of referencing instance members in multiple methods.
 */
public class FakeModule {

    public Report getPurchaseOrderForCountry(TestData testData, Report report) {    // Change 3
        report.assignCategory(testData.get(TestDataKey.TEST_CATEGORY));
        switch(testData.get(TestDataKey.TEST_METHOD)) {
            case "Guest User-Checkout-CA-EN-CC":
                return purchaseOrderForCountry_ENwithCards(testData, report);
            case "Guest User-Checkout-CA-EN-MC":
                return purchaseOrderForCountry_ENwithCards(testData, report);
            case "Guest User-Checkout-CA-EN-AMEX":
                return purchaseOrderForCountry_ENwithCards(testData, report);
            case "Search-styleID_US_EN":
                return Search_styleID_US_EN(testData, report);
            default:
                throw new RuntimeException("Undefined test method");
        }
    }

    Report purchaseOrderForCountry_ENwithCards(TestData testData, Report report) {
        switch(testData.get(TestDataKey.TEST_METHOD)) {
            case "Guest User-Checkout-CA-EN-CC":
                report.setTestMethodInvoked("Guest checkout for Canada in English with Credit Card");
                return report;
            case "Guest User-Checkout-CA-EN-MC":
                report.setTestMethodInvoked("Guest checkout for Canada in English with MasterCard");
                return report;
            case "Guest User-Checkout-CA-EN-AMEX":
                report.setTestMethodInvoked("Guest checkout for Canada in English with American Exrpress");
                return report;
            case "Search-styleID_US_EN":
                report.setTestMethodInvoked("Search styleID for US in English");
                return report;
            default:
                throw new RuntimeException("Undefined test method");
        }
    }
    Report Search_styleID_US_EN(TestData testData, Report report) {       // Change 3
        return report;
    }
}
