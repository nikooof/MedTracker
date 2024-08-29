package persistence;

import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;

public class JsonReaderTest extends JsonTest {

    @Test
    void testWrongFile() {
        try {
            JsonReader reader = new JsonReader("data/doesNotExist.json");
            reader.read();
            fail("This line should not have run");
        } catch (IOException e) {
            System.out.println("Correct exception was caught!");
        }
    }

    @Test
    void testEmptyFile() {
        try {
            JsonReader reader = new JsonReader("data/testBoxOfMedsEmptyReader.json");
            BoxOfMedication boxOfMedication = reader.read();
            assertEquals(0, boxOfMedication.getListOfMeds().size());
        } catch (IOException e) {
            fail("IO Exception should not have been caught");
        }
    }

    @Test
    void testMedsReader() {
        JsonReader reader = new JsonReader("data/testBoxOfMedsWithMedsReader.json");
        try {
            BoxOfMedication boxOfMedication = reader.read();
            ArrayList<Medication> tempMedList = boxOfMedication.getListOfMeds();

            assertEquals(2, tempMedList.size());

            checkMed("2025-12-20", 45, 90,
                    "Consumption information has not been specified!","Zyrtec", tempMedList.get(0));

            checkMed("2025-06-30", 95, 100,
                    "Every two days after breakfast.", "Motrin", tempMedList.get(1));
        } catch (IOException e) {
            fail("IO Exception should not have been caught");
        }
    }

}
