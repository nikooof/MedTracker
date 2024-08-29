package model;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicationTest {

    private Medication med1;
    private LocalDate expDate;

    @BeforeEach
    void setUp() {
        expDate = LocalDate.of(2015, 1, 1);
        med1 = new Medication("Tylenol", 100, expDate);
    }

    @Test
    // Testing the constructor from the information in setUp()
    void testConstructor() {
        assertEquals("Tylenol", med1.getName());
        assertEquals("Consumption information has not been specified!", med1.getConsumptionInformation());
        assertEquals(100, med1.getOriginalNumberOfPills());
        assertEquals(0, med1.getConsumedNumberOfPills());
        assertEquals(expDate, med1.getExpiryDate());
    }

    @Test
    // Testing the setter for consumptionInformation -- the consumptionInformation is modified
    void testSetConsumptionInformation() {
        med1.setConsumptionInformation("This medicine is taken after breakfast, every Monday");
        assertEquals("This medicine is taken after breakfast, every Monday", med1.getConsumptionInformation());
    }

    @Test
    // Testing consumePills() where the given n is less than getOriginalNumberOfPills
    void testConsumeNPillsNotExceed() {
        assertEquals(0, med1.getConsumedNumberOfPills());
        med1.consumePills(10);
        assertEquals(10, med1.getConsumedNumberOfPills());
        med1.consumePills(90);
        assertEquals(100, med1.getConsumedNumberOfPills());
    }

    @Test
    // Testing consumePills() where the given n is greater than getOriginalNumberOfPills
    void testConsumeNPillsExceed() {
        assertEquals(0, med1.getConsumedNumberOfPills());
        med1.consumePills(10);
        assertEquals(10, med1.getConsumedNumberOfPills());
        med1.consumePills(91);
        assertEquals(100, med1.getConsumedNumberOfPills());
    }

    @Test
    // Testing isExpired when the med is expired and then modified s.t. it is not expired
    void testIsExpired() {
        assertTrue(med1.isExpired());

        med1.setExpiryDate(LocalDate.of(2025, 1, 1));
        assertEquals(med1.getExpiryDate(), LocalDate.of(2025, 1, 1));
        assertFalse(med1.isExpired());
    }

    @Test
    // testing when the medicine is not almost finished, then almost finished and then finished
    void testIsAlmostFinished() {
        assertFalse(med1.isAlmostFinished());

        med1.consumePills(50);
        assertFalse(med1.isAlmostFinished());

        med1.consumePills(39);
        assertFalse(med1.isAlmostFinished());

        med1.consumePills(1);
        assertTrue(med1.isAlmostFinished());

        med1.consumePills(9);
        assertTrue(med1.isAlmostFinished());

        med1.consumePills(1);
        assertFalse(med1.isAlmostFinished());
    }

    @Test
    // testing when the medicine is not finished and then finished
    void testIsFinished() {
        med1.consumePills(50);
        assertFalse(med1.isFinished());
        med1.consumePills(49);
        assertFalse(med1.isFinished());
        med1.consumePills(1);
        assertTrue(med1.isFinished());
    }

    @Test
    // testing toString
    void testToString() {
        assertEquals("Tylenol", med1.toString());
    }
}