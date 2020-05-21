package testmodule3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FakeModuleTest {

    private TestData testData;
    private Map<TestDataKey, String> providedTestData;
    private Report report;
    private FakeModule fakeModule;

    @BeforeEach
    void resetTestDataObject() {
        providedTestData = new HashMap();
        report = new Report();
    }

    @Test
    void guestCheckout_Canada_English_CreditCard() {
        providedTestData.put(TestDataKey.TEST_METHOD, "Guest User-Checkout-CA-EN-CC");      // Change 2.1
        fakeModule = new FakeModule(new TestData(providedTestData), report);                // Change 2.2
        fakeModule.getPurchaseOrderForCountry();
        assertEquals("Guest checkout for Canada in English with Credit Card", report.getTestMethodInvoked());
    }

}
