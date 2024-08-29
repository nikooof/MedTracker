package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.time.LocalDate;

// The application which allows the user to add/remove/view/update information about Medication in their boxOfMedication
public class MedsTracker {

    private BoxOfMedication boxOfMeds;
    private Scanner input;
    private static final String destination = "./data/boxOfMed.json";
    private final JsonReader jsonReader;
    private final JsonWriter jsonWriter;

    // EFFECTS: runs the medication tracker application
    public MedsTracker() throws FileNotFoundException {
        jsonReader = new JsonReader(destination);
        jsonWriter = new JsonWriter(destination);
        runMedsTracker();
    }

    // MODIFIES: this
    // EFFECTS: Processes the user's input
    public void runMedsTracker() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            menu();

            command = input.next().toLowerCase();

            if (command.equals("q")) {
                System.out.println("See you next time!");
                keepGoing = false;
            }
            processInput(command);
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes a BoxOfMedication, scanner and creates two example instances of Medication
    public void init() {
        boxOfMeds = new BoxOfMedication();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: Displays all the options, based on the user stories, that the user can pick
    public void menu() {
        System.out.println("\n Select from one of the listed options!");
        System.out.println("\ta -> add a Medication to your box");
        System.out.println("\tr -> remove a Medication from your box");
        System.out.println("\tv -> view all the Medications in your box");
        System.out.println("\tl -> view all the Medications that are almost finished in your box");
        System.out.println("\tf -> view all the Medications that are finished in your box");
        System.out.println("\te -> view all the expired Medications in your box");
        System.out.println("\tc -> consume a Medication in your box");
        System.out.println("\tu -> update the consumption information and the expiry date of a Medication in your box");
        System.out.println("\ts -> save your box of medication into a file");
        System.out.println("\tz -> load your box of medication from a file");
        System.out.println("\tq -> quit the program");
    }

    // EFFECTS: Processes user input that correspond to the options from menu()
    public void processInput(String command) {
        if (command.equals("a")) {
            addMed();
        } else if (command.equals("r")) {
            removeMed();
        } else if (command.equals("v")) {
            viewMeds();
        } else if (command.equals("l")) {
            viewAlmostFinishedMeds();
        } else if (command.equals("f")) {
            viewFinishedMeds();
        } else if (command.equals("e")) {
            viewExpiredMeds();
        } else if (command.equals("c")) {
            consumeMed();
        } else if (command.equals("u")) {
            updateMed();
        } else if (command.equals("s")) {
            saveBoxOfMedication();
        } else if (command.equals("z")) {
            loadBoxOfMedication();
        } else if (!command.equals("q")) {
            System.out.println("The given input is invalid. Please try again!");
        }
    }


    // MODIFIES: this
    // EFFECTS: adds a Medication to the BoxOfMedication if it is not already in the box, else it does not add it
    public void addMed() {
        System.out.println("Please enter the name of the medication you wish to add");
        String name = input.next();

        if (boxOfMeds.viewAllMedicationsInBox().contains(name)) {
            System.out.println("Medication is already in the box!");
        } else {
            System.out.println("Please enter the number of pills that come in the box of the medication");
            int orgNumberOfPills = input.nextInt();
            LocalDate expiryDate = makeDate();

            Medication med = new Medication(name, orgNumberOfPills, expiryDate);
            boxOfMeds.addMedication(med);

            System.out.println("Medication with name " + name + " with initially " + orgNumberOfPills
                    + " pills has been added to your Box!"
                    + " It expires on " + expiryDate.toString() + ".");
        }
    }

    // EFFECTS: creates an instance of LocalDate.of
    public LocalDate makeDate() {
        System.out.println("Please enter the day of expiry of the medication:");
        int day = input.nextInt();

        System.out.println("Please enter the month of expiry of the medication:");
        int month = input.nextInt();

        System.out.println("Please enter the year of expiry of the medication:");
        int year = input.nextInt();

        return LocalDate.of(year, month, day);
    }

    // MODIFIES: this
    // EFFECTS:  removes the given medicine from boxOfMeds if found,
    //           else print not found or empty box depending on the scenario

    public void removeMed() {
        System.out.println("Please enter the name of the medication you wish to remove");
        String name = input.next();

        if (!boxOfMeds.viewAllMedicationsInBox().contains(name)) {
            System.out.println("The name of the medication that you provided is not in the box!");
        } else {
            Medication med = null;
            for (Medication m: boxOfMeds.getListOfMeds()) {
                if (name.equals(m.getName())) {
                    med = m;
                    break;
                }
            }
            boxOfMeds.removeMedication(med);
            System.out.println("The medication with the given name has been removed from the box!");
        }
    }

    // EFFECTS: prints the list of all Medication in boxOfMeds to the console
    public void viewMeds() {
        StringBuilder allNames = new StringBuilder();

        if (boxOfMeds.getListOfMeds().isEmpty()) {
            System.out.println("The box is empty!");
        } else if (boxOfMeds.getListOfMeds().size() == 1) {
            allNames.append(boxOfMeds.viewAllMedicationsInBox().get(0));
            System.out.println("The box consists of the following medicine: " + allNames + ".");
        } else {
            int lastIndex = boxOfMeds.viewAllMedicationsInBox().size() - 1;
            for (int i = 0; i < lastIndex; i++) {
                allNames.append(boxOfMeds.viewAllMedicationsInBox().get(i)).append(", ");
            }
            allNames.append("and ").append(boxOfMeds.viewAllMedicationsInBox().get(lastIndex));
            System.out.println("The box consists of the following medicines: " + allNames + ".");
        }
    }

    // EFFECTS: prints the list of all Medication that is almost finished in boxOfMeds to the console
    public void viewAlmostFinishedMeds() {
        StringBuilder allNames = new StringBuilder();

        if (boxOfMeds.viewAlmostFinishedMedications().isEmpty()) {
            System.out.println("The box consists of no medicines that are almost finished!");
        } else if (boxOfMeds.viewAlmostFinishedMedications().size() == 1) {
            allNames.append(boxOfMeds.viewAlmostFinishedMedications().get(0));
            System.out.println("The box consists of one medicine that is almost finished: " + allNames + ".");
        } else {
            int lastIndex = boxOfMeds.viewAlmostFinishedMedications().size() - 1;
            for (int i = 0; i < lastIndex; i++) {
                allNames.append(boxOfMeds.viewAlmostFinishedMedications().get(i)).append(", ");
            }
            allNames.append("and ").append(boxOfMeds.viewAlmostFinishedMedications().get(lastIndex));
            System.out.println("The box consists of the following almost finished medicines: " + allNames + ".");
        }
    }

    // EFFECTS: prints the list of all Medication in boxOfMeds that are finished to the console
    public void viewFinishedMeds() {
        StringBuilder allNames = new StringBuilder();

        if (boxOfMeds.viewFinishedMedications().isEmpty()) {
            System.out.println("The box consists of no medicines that are finished!");
        } else if (boxOfMeds.viewFinishedMedications().size() == 1) {
            allNames.append(boxOfMeds.viewFinishedMedications().get(0));
            System.out.println("The box consists of one medicine that is finished: " + allNames + ".");
        } else {
            int lastIndex = boxOfMeds.viewFinishedMedications().size() - 1;
            for (int i = 0; i < lastIndex; i++) {
                allNames.append(boxOfMeds.viewFinishedMedications().get(i)).append(", ");
            }
            allNames.append("and ").append(boxOfMeds.viewFinishedMedications().get(lastIndex));
            System.out.println("The box consists of the following finished medicines: " + allNames + ".");
        }
    }

    // EFFECTS: prints the list of all Medication in boxOfMeds that are expired to the console
    public void viewExpiredMeds() {
        StringBuilder allNames = new StringBuilder();

        if (boxOfMeds.viewExpiredMedications().isEmpty()) {
            System.out.println("The box consists of no medicines that are expired!");
        } else if (boxOfMeds.viewExpiredMedications().size() == 1) {
            allNames.append(boxOfMeds.viewExpiredMedications().get(0));
            System.out.println("The box consists of one medicine that is expired: " + allNames + ".");
        } else {
            int lastIndex = boxOfMeds.viewExpiredMedications().size() - 1;
            for (int i = 0; i < lastIndex; i++) {
                allNames.append(boxOfMeds.viewExpiredMedications().get(i)).append(", ");
            }
            allNames.append("and ").append(boxOfMeds.viewExpiredMedications().get(lastIndex));
            System.out.println("The box consists of the following expired medicines: " + allNames + ".");
        }
    }

    // MODIFIES: this
    // EFFECTS: If the given medication is found, a given number of pills is consumed
    public void consumeMed() {
        System.out.println("Please enter the name of the medication you wish to consume");
        String name = input.next();

        if (boxOfMeds.getListOfMeds().isEmpty()) {
            System.out.println("The box is empty!");
        } else {
            if (!boxOfMeds.viewAllMedicationsInBox().contains(name)) {
                System.out.println("The box does not consist of a Medicine with the given name!");
            } else {
                System.out.println("Please enter how many pills you have consumed");
                int con = input.nextInt();

                Medication med = null;

                for (Medication m: boxOfMeds.getListOfMeds()) {
                    if (name.equals(m.getName())) {
                        med = m;
                        break;
                    }
                }
                boxOfMeds.consumeMedication(med, con);
                System.out.println(con + " pills of " + name + " have been consumed!");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates the consumptionInformation and the expiryDate field of a given med
    public void updateMed() {
        if (boxOfMeds.getListOfMeds().isEmpty()) {
            System.out.println("The box is empty!");
        } else {
            System.out.println("What is the name of the medicine whose information you wish to update?");
            String name = input.next();
            if (!boxOfMeds.viewAllMedicationsInBox().contains(name)) {
                System.out.println("The box does not consist of a Medicine with the given name!");
            } else {
                for (Medication med : boxOfMeds.getListOfMeds()) {
                    if (name.equals(med.getName())) {
                        System.out.println("Please enter the new consumption information");
                        med.setConsumptionInformation(input.next());
                        System.out.println("Please enter the new expiry date");
                        med.setExpiryDate(makeDate());
                        System.out.println("The consumption information and the expiry date"
                                + " of given medicine have been updated.");
                        break;
                    }
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: saves the BoxOfMedication in the JSON format in destination
    private void saveBoxOfMedication() {
        try {
            jsonWriter.open();
            jsonWriter.write(boxOfMeds);
            jsonWriter.close();
            System.out.println("Saved all the medications to file: " + destination);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + destination);
        }
    }

    // MODIFIES: this
    // EFFECTS: load the BoxOfMedication from the JSON format in destination
    private void loadBoxOfMedication() {
        try {
            boxOfMeds = jsonReader.read();
            System.out.println("Loaded the box of medication from the file: " + destination);
        } catch (IOException e) {
            System.out.println("Unable to read to file: " + destination);
        }

    }
}