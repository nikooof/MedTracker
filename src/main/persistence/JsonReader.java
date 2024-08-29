package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Stream;

// Class which enables BoxOfMedication to be read as JSON
public class JsonReader {
    private final String source;
    private final BoxOfMedication boxOfMedication;

    // EFFECTS: constructor to read the source and initializes a new BoxOfMedication
    public JsonReader(String source) {
        this.source = source;
        this.boxOfMedication = new BoxOfMedication();
    }

    // EFFECTS: returns the data from the source as the initialized boxOfMedication
    public BoxOfMedication read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        parseBoxOfMedication(jsonObject);

        return boxOfMedication;
    }

    // EFFECTS: returns the data from the source as a String
    public String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    // MODIFIES: this
    // EFFECTS: Iterates through the boxOfMedication's listOfMeds and casts each as JSONObject
    public void parseBoxOfMedication(JSONObject jsonObject) {
        JSONArray listOfMeds = jsonObject.getJSONArray("listOfMeds");

        for (Object med: listOfMeds) {
            addJsonItem((JSONObject) med);
        }
    }

    // MODIFIES: this
    // EFFECTS: stores JSON data as fields and makes a new Medication. Adds Medication to BoxOfMedication
    public void addJsonItem(JSONObject jsonMed) {
        String name = jsonMed.getString("name");
        String consumptionInfo = jsonMed.getString("consumption information");
        int originalNumberOfPills = jsonMed.getInt("original pills");
        int consumedNumberOfPills = jsonMed.getInt("consumed pills");
        LocalDate expiryDate = LocalDate.parse(jsonMed.getString("expiry date"));

        Medication medTemp = new Medication(name, originalNumberOfPills, expiryDate);
        medTemp.setConsumedNumberOfPills(consumedNumberOfPills);
        medTemp.setConsumptionInformation(consumptionInfo);
        boxOfMedication.addMedication(medTemp);
    }


    // Referenced from the JsonSerialization Demo
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

}
