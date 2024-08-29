package persistence;

import model.*;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Writing a JSON file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter printWriter;
    private final String destination;

    // EFFECTS: constructs a JsonWriter with given destination
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // EFFECTS: opens the given file if found,
    //          else throws FileNotFoundException
    public void open() throws FileNotFoundException {
        printWriter = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: updates the file as necessary with the updates information
    public void write(BoxOfMedication boxOfMedication) {
        JSONObject jsonBoxOfMedication = boxOfMedication.toJson();
        saveToFile(jsonBoxOfMedication.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: saves the data to the PrintWriter
    private void saveToFile(String string) {
        printWriter.print(string);
    }

    // MODIFIES: this
    // EFFECTS: closes the PrintWriter
    public void close() {
        printWriter.close();
    }

    // Referenced from the JsonSerialization Demo
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
}
