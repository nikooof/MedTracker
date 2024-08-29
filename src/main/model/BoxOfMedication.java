package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

// Represents the BoxOfMedication class, which is in other words, is a class for a List of Medication
public class BoxOfMedication {

    private final List<Medication> listOfMeds;
    private EventLog eventLog = EventLog.getInstance();

    public BoxOfMedication() {
        listOfMeds = new ArrayList<>();
    }

    public ArrayList<Medication> getListOfMeds() {
        return (ArrayList<Medication>) listOfMeds;
    }

    // MODIFIES: this
    // EFFECTS: true if medication is added to listOfMeds, else it is a duplicate
    public void addMedication(Medication medication) {
        if (!listOfMeds.contains((medication))) {
            listOfMeds.add(medication);
            Event event = new Event("Added " + medication.getName() + " to the box!");
            eventLog.logEvent(event);
        }
    }

    // MODIFIES: this
    // EFFECTS: remove Medication with given name if found
    public void removeMedication(Medication medication) {
        boolean isRemoved = listOfMeds.remove(medication);
        if (isRemoved) {
            Event event = new Event("Removed " + medication.getName() + " from the box!");
            eventLog.logEvent(event);
        }
    }

    // REQUIRES: !(listOfMeds.isEmpty())
    // EFFECTS: returns a list of names of all medications in listOfMeds
    public List<String> viewAllMedicationsInBox() {
        List<String> listOfMedicationNames = new ArrayList<>();
        for (Medication m: listOfMeds) {
            listOfMedicationNames.add(m.getName());
        }
        return listOfMedicationNames;
    }

    // REQUIRES: !(listOfMeds.isEmpty())
    // EFFECTS: returns a list of names of all medications that are almost finished in listOfMeds
    public List<String> viewAlmostFinishedMedications() {
        List<String> listOfMedicationNames = new ArrayList<>();
        for (Medication m: listOfMeds) {
            if (m.isAlmostFinished()) {
                listOfMedicationNames.add(m.getName());
            }
        }
        return listOfMedicationNames;
    }

    // REQUIRES: !(listOfMeds.isEmpty())
    // EFFECTS: returns a list of names of all medications that are almost finished in listOfMeds
    public List<String> viewFinishedMedications() {
        List<String> listOfMedicationNames = new ArrayList<>();
        for (Medication m: listOfMeds) {
            if (m.isFinished()) {
                listOfMedicationNames.add(m.getName());
            }
        }
        Event event = new Event(listOfMedicationNames.size() + " Finished Medications" + " in the box were displayed!");
        eventLog.logEvent(event);
        return listOfMedicationNames;
    }

    // REQUIRES: !(listOfMeds.isEmpty())
    // EFFECTS: returns a list of names of all medications that are expired in listOfMeds
    public List<String> viewExpiredMedications() {
        List<String> listOfMedicationNames = new ArrayList<>();
        for (Medication m: listOfMeds) {
            if (m.isExpired()) {
                listOfMedicationNames.add(m.getName());
            }
        }
        return listOfMedicationNames;
    }

    // REQUIRES: listOfMeds.contains(med)
    // MODIFIES: med
    // EFFECTS: consumes the given pills by given amount
    public void consumeMedication(Medication med, int n) {
        for (Medication m: listOfMeds) {
            if (med.equals(m)) {
                m.consumePills(n);
            }
        }
    }

    public JSONObject toJson() {
        JSONObject jsonBoxOfMedication = new JSONObject();
        JSONArray jsonListOfMeds = new JSONArray();

        for (Medication m: listOfMeds) {
            JSONObject jsonMed = m.toJson();
            jsonListOfMeds.put(jsonMed);
        }

        jsonBoxOfMedication.put("listOfMeds", jsonListOfMeds);
        return jsonBoxOfMedication;
    }
}