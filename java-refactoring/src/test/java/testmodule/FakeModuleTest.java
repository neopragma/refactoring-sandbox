package testmodule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FakeModuleTest {

    private Map<String, String> testData;
    private Report report;
    private FakeModule fakeModule;

    @BeforeEach
    void resetTestDataObject() {
        testData = new HashMap();
        report = new Report();
    }

    @Test
    void guestCheckout_Canada_English_CreditCard() {
        testData.put("TestID", "Guest User-Checkout-CA-EN-CC");
        fakeModule = new FakeModule(testData, report);
        fakeModule.getPurchaseOrderForCountry();
        assertEquals("Guest checkout for Canada in English with Credit Card", report.getTestMethodInvoked());
    }

}
