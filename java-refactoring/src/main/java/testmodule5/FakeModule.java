package testmodule5;

import java.util.ResourceBundle;

/**
 * Fourth pass at refactoring the code.
 * At this point we have multiple directions to choose from.
 * This version is based on the apparent pattern in the names of the test methods:
 * Action_Country_Language_PaymentMethod.
 * We can use Java's internationalization/localization support to identify the name of a
 * class where the functionality is encapsulated for each combination of action, country,
 * language, and payment method. Then testModule.getPurchaseOrderForCountry() and
 * testModule.Search_styleID_US_EN can be replaced by a factory method that hands off to
 * the appropriate implementation to run the test case.
 * Other approaches are also possible to replace conditional logic with polymorphism.
 *
 * 4. Break out functionality currently embedded in procedural conditional logic into
 * an object-oriented class hierarchy.
 */
public class FakeModule implements Constants {

    public Report invokeTestMethod(TestData testData, Report report) {
        report.assignCategory(testData.getString(TestDataKey.TEST_CATEGORY));
        return getTestMethodInstance(testData).runTestCase(testData, report);
    }

    private TestMethod getTestMethodInstance(TestData testData) {
        try {
            ResourceBundle testMethodsResourceBundle =
                    ResourceBundle.getBundle(
                            TEST_METHODS_RESOURCE_NAME,
                            testData.getLocale(TestDataKey.TEST_LOCALE));
            String testMethodClassName = testMethodsResourceBundle.getString(testData.getString(TestDataKey.TEST_METHOD));
            return (TestMethod) Class.forName(testMethodClassName).getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
