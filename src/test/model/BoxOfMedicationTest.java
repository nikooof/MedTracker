package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;

public class BoxOfMedicationTest {
    private Medication med1;
    private Medication med2;
    private Medication med3;
    private BoxOfMedication box1;
    private List<String> list1;
    private List<String> list2;
    private List<String> list3;

    @BeforeEach
    void setUp() {
        med1 = new Medication("Tylenol", 100, LocalDate.of(2015, 1, 1));
        med2 = new Medication("Zyrtec", 25, LocalDate.of(2020, 1, 1));
        med3 = new Medication("Advil", 500, LocalDate.of(2024, 2, 14));

        box1 = new BoxOfMedication();

        list1 = new ArrayList<>();
        list1.add(med1.getName());

        list2 = new ArrayList<>();
        list2.add(med1.getName());
        list2.add(med2.getName());

        list3 = new ArrayList<>();
        list3.add(med1.getName());
        list3.add(med2.getName());
        list3.add(med3.getName());
    }

    @Test
    // testing adding Medication to BoxOfMedication, including duplicate case
    void testAddMedication() {
        assertTrue(box1.getListOfMeds().isEmpty());
        box1.addMedication(med1);
        assertEquals(1, box1.getListOfMeds().size());
        assertEquals(med1, box1.getListOfMeds().get(0));

        box1.addMedication(med1);
        assertEquals(1, box1.getListOfMeds().size());
        assertEquals(med1, box1.getListOfMeds().get(0));

        box1.addMedication(med2);
        assertEquals(2, box1.getListOfMeds().size());
        assertEquals(med1, box1.getListOfMeds().get(0));
        assertEquals(med2, box1.getListOfMeds().get(1));
    }

    @Test
    // testing removing Medication from BoxOfMedication
    void testRemoveMedication() {
        assertTrue(box1.getListOfMeds().isEmpty());
        box1.addMedication(med1);
        box1.addMedication(med2);

        box1.removeMedication(med3);
        assertEquals(2, box1.getListOfMeds().size());

        box1.addMedication(med3);
        assertEquals(3, box1.getListOfMeds().size());
        assertEquals(med3, box1.getListOfMeds().get(2));
        box1.removeMedication(med3);
        assertEquals(2, box1.getListOfMeds().size());

        box1.removeMedication(med1);
        box1.removeMedication(med2);

        assertTrue(box1.getListOfMeds().isEmpty());
    }

    @Test
    // Testing the correct listOfMedication is returned
    void testViewAllMedicationsInBox() {
        assertTrue(box1.getListOfMeds().isEmpty());

        box1.addMedication(med1);
        assertEquals(list1, box1.viewAllMedicationsInBox());

        box1.addMedication(med2);
        assertEquals(list2, box1.viewAllMedicationsInBox());
    }

    @Test
    // Testing consumeMedicine for when the medicine is not finished and finished
    void testConsumeMedicine() {
        box1.addMedication(med1);
        assertEquals(0, box1.getListOfMeds().get(0).getConsumedNumberOfPills());
        box1.consumeMedication(med1, 10);
        assertEquals(10, box1.getListOfMeds().get(0).getConsumedNumberOfPills());
        box1.consumeMedication(med1, 100);
        assertEquals(100, box1.getListOfMeds().get(0).getConsumedNumberOfPills());
    }

    @Test
    // Testing when no medicines are almost finished, all are almost finished and at least one is almost finished
    void testViewAlmostFinishedMedications() {
        assertTrue(box1.getListOfMeds().isEmpty());

        box1.addMedication(med1);
        box1.consumeMedication(med1, 50);
        assertTrue(box1.viewAlmostFinishedMedications().isEmpty());

        box1.addMedication(med2);
        assertTrue(box1.viewAlmostFinishedMedications().isEmpty());

        box1.consumeMedication(med1, 39);
        box1.consumeMedication(med2, 20);
        assertTrue(box1.viewAlmostFinishedMedications().isEmpty());

        box1.consumeMedication(med1, 3);
        box1.consumeMedication(med2, 4);
        assertEquals(list2, box1.viewAlmostFinishedMedications());

        box1.consumeMedication(med2, 100);
        assertEquals(list1, box1.viewAlmostFinishedMedications());
    }

    @Test
    // Testing when no medicines are finished, all are finished and at least one is finished
    void testViewFinishedMedications() {
        assertTrue(box1.getListOfMeds().isEmpty());

        box1.addMedication(med1);
        box1.consumeMedication(med1, 50);
        assertTrue(box1.viewFinishedMedications().isEmpty());

        box1.addMedication(med2);
        assertTrue(box1.viewFinishedMedications().isEmpty());

        box1.consumeMedication(med1, 39);
        box1.consumeMedication(med2, 20);
        assertTrue(box1.viewFinishedMedications().isEmpty());

        box1.consumeMedication(med1, 3);
        box1.consumeMedication(med2, 4);
        assertTrue(box1.viewFinishedMedications().isEmpty());

        box1.consumeMedication(med1, 8);
        assertEquals(list1, box1.viewFinishedMedications());
    }

    @Test

    void testViewExpiredMedications() {
    // Testing when no medicines are expired, all are expired and at least one is expired
        assertTrue(box1.viewExpiredMedications().isEmpty());
        box1.addMedication(med1);
        assertEquals(list1, box1.viewExpiredMedications());
        box1.addMedication(med2);
        assertEquals(list2, box1.viewExpiredMedications());
        box1.addMedication(med3);
        assertEquals(list3, box1.viewExpiredMedications());

        box1.getListOfMeds().get(2).setExpiryDate(LocalDate.of(2025, 2, 14));
        assertEquals(list2, box1.viewExpiredMedications());
    }
}