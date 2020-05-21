package testmodule4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FakeModuleTest {

    private Map<TestDataKey, String> providedTestData;
    private Report report;
    private FakeModule fakeModule;              // Change 3

    @BeforeEach
    void resetTestDataObject() {
        providedTestData = new HashMap();
        report = new Report();
        fakeModule = new FakeModule();
    }

    @Test
    void guestCheckout_Canada_English_CreditCard() {
        providedTestData.put(TestDataKey.TEST_METHOD, "Guest User-Checkout-CA-EN-CC");
        report = fakeModule.getPurchaseOrderForCountry(new TestData(providedTestData), report);   // Change 3
        assertEquals("Guest checkout for Canada in English with Credit Card", report.getTestMethodInvoked());
    }

}
