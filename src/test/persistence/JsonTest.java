package persistence;

import model.*;
import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {
    protected void checkMed(String expDate, int consumedPills, int originalPills,
                            String consumptionInfo, String name, Medication med) {

        assertEquals(expDate, med.getExpiryDate().toString());
        assertEquals(consumedPills, med.getConsumedNumberOfPills());
        assertEquals(originalPills, med.getOriginalNumberOfPills());
        assertEquals(consumptionInfo, med.getConsumptionInformation());
        assertEquals(name, med.getName());
    }
}
