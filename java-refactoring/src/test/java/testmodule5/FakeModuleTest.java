package testmodule5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FakeModuleTest implements Constants {

    private Map<TestDataKey, Object> providedTestData;
    private Report report;
    private FakeModule fakeModule;

    @BeforeEach
    void resetTestDataObject() {
        providedTestData = new HashMap();
        report = new Report();
        fakeModule = new FakeModule();
    }

    @Test
    void guestCheckout_Canada_English_CreditCard() {
        prepareTestData(GUEST_USER_CHECKOUT_RESOURCE_KEY, new Locale("CA", "EN", "CC"));
        report = fakeModule.invokeTestMethod(new TestData(providedTestData), report);
        assertEquals("Guest checkout for Canada in English with Credit Card", report.getTestMethodInvoked());
    }

    @Test
    void searchStyleID_US_English() {
        prepareTestData(SEARCH_STYLE_ID_RESOURCE_KEY, new Locale("US", "EN"));
        report = fakeModule.invokeTestMethod(new TestData(providedTestData), report);
        assertEquals("Search Style ID for United States in English", report.getTestMethodInvoked());
    }

    @Test
    void tryToInvokeUndefinedTestMethod() {
        prepareTestData(SEARCH_STYLE_ID_RESOURCE_KEY, new Locale("FR", "PL"));
        assertThrows(RuntimeException.class, () -> {
            fakeModule.invokeTestMethod(new TestData(providedTestData), report);
        });
    }

    private void prepareTestData(String testMethodResourceKey, Locale testLocale) {
        providedTestData.put(TestDataKey.TEST_METHOD, testMethodResourceKey);                      // Change 4
        providedTestData.put(TestDataKey.TEST_LOCALE, testLocale);
    }

}
