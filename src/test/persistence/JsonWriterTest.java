package persistence;

import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class JsonWriterTest extends JsonTest {

    @Test
    void testIllegalFileName() {
        try {
            JsonWriter writer = new JsonWriter("data/illegal\0file/.json");
            writer.open();
            fail("This line should not have run");
        } catch (IOException e) {
            System.out.println("Correct exception was caught!");
        }
    }

    @Test
    void testEmpty() {
        try {
            JsonWriter writer = new JsonWriter("data/testBoxOfMedsEmptyWriter.json");
            writer.open();

            BoxOfMedication emptyBoxOfMedication = new BoxOfMedication();
            writer.write(emptyBoxOfMedication);
            writer.close();

            JsonReader reader = new JsonReader("data/testBoxOfMedsEmptyWriter.json");
            BoxOfMedication boxOfMedication = reader.read();
            assertEquals(0, boxOfMedication.getListOfMeds().size());
        }   catch (IOException e) {
            fail("IO Exception should not have been caught");
        }
    }

    @Test
    void testWithMeds() {
        try {
            JsonWriter writer = new JsonWriter("data/testBoxOfMedsWithMedsWriter.json");
            writer.open();

            BoxOfMedication boxOfMedication = new BoxOfMedication();

            LocalDate date1 = LocalDate.parse("2025-12-20");
            Medication med1 = new Medication("Zyrtec", 90, date1);
            med1.setConsumedNumberOfPills(45);

            LocalDate date2 = LocalDate.parse("2025-06-30");
            Medication med2 = new Medication("Motrin", 100, date2);
            med2.setConsumedNumberOfPills(95);
            med2.setConsumptionInformation("Every two days after breakfast.");

            boxOfMedication.addMedication(med1);
            boxOfMedication.addMedication(med2);

            writer.write(boxOfMedication);
            writer.close();

            JsonReader reader = new JsonReader("data/testBoxOfMedsWithMedsWriter.json");
            BoxOfMedication boxOfMedicationRead = reader.read();
            ArrayList<Medication> tempList = boxOfMedicationRead.getListOfMeds();
            assertEquals(2, tempList.size());

            checkMed("2025-12-20", 45, 90,
                    "Consumption information has not been specified!","Zyrtec", tempList.get(0));

            checkMed("2025-06-30", 95, 100,
                    "Every two days after breakfast.", "Motrin", tempList.get(1));

        } catch (IOException e) {
            fail("IO Exception should not have been caught");
        }
    }
}
