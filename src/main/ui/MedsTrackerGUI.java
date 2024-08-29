package ui;

import model.*;
import model.Event;
import persistence.*;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

// GUI form of the application which allows users to add/remove/view Medications in their boxOfMedication
public class MedsTrackerGUI extends JFrame implements ActionListener, WindowListener {

    private BoxOfMedication boxOfMeds;
    private static final String destination = "./data/boxOfMed.json";
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    private static final String bgImgLocation = "./data/background_img.jpeg";

    private static final int WIDTH = 1500;
    private static final int HEIGHT = 600;
    private static final int BORDER = 15;

    private JPanel optionsPanel;
    private JPanel contentPanel;
    private JPanel medsPanel;
    private JPanel addMedPanel;
    private DefaultListModel<Medication> listOfMedsModel;
    private JList<Medication> listMeds;
    private JScrollPane scrollPane;
    private JButton removeMedicationButton;

    private JTextField name;
    private JTextField consumptionInformation;
    private JTextField originalNumberOfPills;
    private JTextField consumedNumberOfPills;
    private JTextField expDate;
    private JTextField findExpDateJText;
    private JTextField consumeMedJText;

    private EventLog eventLog = EventLog.getInstance();

    // EFFECTS: runs the medication tracker GUI
    public MedsTrackerGUI() throws FileNotFoundException {
        super("My Box of Medication");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(this);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new GridLayout(2, 1));
        contentPanel = (JPanel) getContentPane();
        contentPanel.setBorder(new EmptyBorder(BORDER, BORDER, BORDER, BORDER));

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(7, 2));

        medsPanel = new JPanel();
        JLabel img;
        img = new JLabel(new ImageIcon(bgImgLocation));

        jsonReader = new JsonReader(destination);
        jsonWriter = new JsonWriter(destination);

        initializeMeds();
        initializeMedsTrackerGUI();
        add(img);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: creates a new boxOfMeds and adds two Medication as examples
    public void initializeMeds() {
        boxOfMeds = new BoxOfMedication();
    }

    // EFFECTS: initializes the two panels and adds the buttons
    public void initializeMedsTrackerGUI() {
        initializeMedsPanel();
        addMedsButton();
        removeMedsButton();
        findExpDateButton();
        displayFinishedMedsButton();
        consumeMedButton();
        saveButton();
        loadButton();
        add(optionsPanel);
    }

    // EFFECTS: initializes medPanel and adds JList and ListModel to panel; sets up scrolling
    private void initializeMedsPanel() {
        listOfMedsModel = new DefaultListModel<>();
        listMeds = new JList<>(listOfMedsModel);

        medsPanel.add(listMeds);
        listMeds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listMeds.setSelectedIndex(0);
        listMeds.setVisibleRowCount(5);

        scrollPane = new JScrollPane(listMeds);
        add(scrollPane);
    }

    // MODIFIES: this
    // EFFECTS: creates button to allow users to add Medication to BoxOfMeds
    private void addMedsButton() {
        JButton addMedicationButton = new JButton("Add Medication");
        addMedicationButton.setActionCommand("add");
        addMedicationButton.addActionListener(this);
        optionsPanel.add(addMedicationButton);
    }

    // MODIFIES: this
    // EFFECTS: creates button to allow users to remove Medication to BoxOfMeds
    private void removeMedsButton() {
        removeMedicationButton = new JButton("Remove Medication");
        removeMedicationButton.setActionCommand("remove");
        removeMedicationButton.addActionListener(this);
        optionsPanel.add(removeMedicationButton);
    }

    // MODIFIES: this, findExpDateJText
    // EFFECTS: creates button to allow users to find a Medication wih findExpDateJText
    private void findExpDateButton() {
        JLabel expDateLabel = new JLabel("Find the expiry date of: ", JLabel.TRAILING);
        findExpDateJText = new JTextField(10);
        findExpDateJText.addActionListener(this);
        expDateLabel.setLabelFor(findExpDateJText);
        optionsPanel.add(expDateLabel);
        optionsPanel.add(findExpDateJText);

        JButton findExpDateButton = new JButton("Find");
        findExpDateButton.setActionCommand("find");
        findExpDateButton.addActionListener(this);
        optionsPanel.add(findExpDateButton);
    }

    // MODIFIES: this
    // EFFECTS: creates button to allow users to see all Meds that are finished
    private void displayFinishedMedsButton() {
        JButton displayFinishedMedsButton = new JButton("Display Finished Medication");
        displayFinishedMedsButton.setActionCommand("finished");
        displayFinishedMedsButton.addActionListener(this);
        optionsPanel.add(displayFinishedMedsButton);
    }

    // MODIFIES: this
    // EFFECTS: creates button to allow users to consume a particular quantity of a specific med
    private void consumeMedButton() {
        JLabel consumeLabel = new JLabel("Consume how many pills of this medication?", JLabel.TRAILING);
        consumeMedJText = new JTextField(10);
        consumeMedJText.addActionListener(this);
        consumeLabel.setLabelFor(consumeMedJText);
        optionsPanel.add(consumeLabel);
        optionsPanel.add(consumeMedJText);

        JButton consumeMedButton = new JButton("Consume");
        consumeMedButton.setActionCommand("consume");
        consumeMedButton.addActionListener(this);
        optionsPanel.add(consumeMedButton);
    }

    // MODIFIES: this
    // EFFECTS: creates button to allow users to save the BoxOfMeds as JSON
    private void saveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);
        optionsPanel.add(saveButton);
    }

    // MODIFIES: this
    // EFFECTS: creates button to allow users to load the BoxOfMeds from JSON
    private void loadButton() {
        JButton loadButton = new JButton("Load");
        loadButton.setActionCommand("load");
        loadButton.addActionListener(this);
        optionsPanel.add(loadButton);
    }

    // MODIFIES: this
    // EFFECTS: based on the buttons above, an action is performed after pressing a button
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("add")) {
            addMed();
        } else if (actionCommand.equals("remove")) {
            removeMed();
        } else if (actionCommand.equals("find")) {
            findExpiryDate();
        } else if (actionCommand.equals("finished")) {
            showFinishedMeds();
        } else if (actionCommand.equals("consume")) {
            consumeMed();
        } else if (actionCommand.equals("save")) {
            saveMeds();
        } else if (actionCommand.equals("load")) {
            loadMeds();
        } else if (actionCommand.equals("addMed")) {
            addNewMed();
        }
    }

    // MODIFIES: this, addMedPanel
    // EFFECTS: creates a JFrame and JPanel so users can add a new med
    private void addMed() {
        JFrame addMedFrame = new JFrame("Add Medication");
        addMedFrame.setLayout(new GridLayout(6, 1));
        addMedPanel = (JPanel) addMedFrame.getContentPane();
        initializeMedFields();

        addMedFrame.pack();
        addMedFrame.setPreferredSize(new Dimension(WIDTH / 2, HEIGHT / 2));
        addMedFrame.setLocationRelativeTo(null);
        addMedFrame.setResizable(true);
        addMedFrame.setVisible(true);

    }

    // MODIFIES: this, addMedPanel
    // EFFECTS: creates JLabels and JTextFields for each Medication field
    private void initializeMedFields() {
        JLabel nameLabel = new JLabel("Medication name: ", JLabel.TRAILING);
        addMedPanel.add(nameLabel);
        name = new JTextField(10);
        nameLabel.setLabelFor(name);
        addMedPanel.add(name);

        JLabel consumptionInformationLabel = new JLabel("Medication Information: ", JLabel.TRAILING);
        addMedPanel.add(consumptionInformationLabel);
        consumptionInformation = new JTextField(10);
        consumptionInformationLabel.setLabelFor(consumptionInformation);
        addMedPanel.add(consumptionInformation);

        JLabel originalLabel = new JLabel("Original number of pills: ", JLabel.TRAILING);
        addMedPanel.add(originalLabel);
        originalNumberOfPills = new JTextField(3);
        originalLabel.setLabelFor(originalNumberOfPills);
        addMedPanel.add(originalNumberOfPills);

        JLabel consumedLabel = new JLabel("Consumed number of pills: ", JLabel.TRAILING);
        addMedPanel.add(consumedLabel);
        consumedNumberOfPills = new JTextField(3);
        consumedLabel.setLabelFor(consumedNumberOfPills);
        addMedPanel.add(consumedNumberOfPills);

        initializeMedFieldsHelper();
    }

    // MODIFIES: this, addMedPanel
    // EFFECTS: creates JLabels and JTextFields for each Medication field
    private void initializeMedFieldsHelper() {
        JLabel expiryDateLabel = new JLabel("Expiry Date (yyyy-mm-dd): ", JLabel.TRAILING);
        addMedPanel.add(expiryDateLabel);
        expDate = new JTextField(6);
        expiryDateLabel.setLabelFor(expDate);
        addMedPanel.add(expDate);

        JButton addMedButton = new JButton("Add Medication");
        addMedButton.addActionListener(this);
        addMedButton.setActionCommand("addMed");
        addMedPanel.add(addMedButton);
    }

    // MODIFIES: this
    // EFFECTS: removes a Medication from JList
    private void removeMed() {
        int i = listMeds.getSelectedIndex();
        Medication tempMed = listOfMedsModel.get(i);
        boxOfMeds.removeMedication(tempMed);
        listOfMedsModel.remove(i);

        if (listOfMedsModel.isEmpty()) {
            removeMedicationButton.setEnabled(false);
        } else {
            listMeds.setSelectedIndex(0);
        }
    }

    // EFFECTS: returns the expiry date of a mediation if med with given name exists,
    //          return "A medication with given name was not found!"
    private void findExpiryDate() {
        String tempMedName = findExpDateJText.getText();
        LocalDate tempExpDate = getLocateDateHelper(tempMedName);

        String tempString;
        JFrame tempFrame = new JFrame("The expiry date of the given medication is:");
        if (tempExpDate != null) {
            tempString = tempExpDate.toString();
            JLabel tempLabel = new JLabel(tempString);
            tempFrame.add(tempLabel);
        } else {
            tempString = "A medication with given name was not found!";
            JLabel tempLabel = new JLabel(tempString);
            tempFrame.add(tempLabel);
        }
        tempFrame.pack();
        tempFrame.setLocationRelativeTo(null);
        tempFrame.setVisible(true);
    }

    // EFFECTS: returns the LocalDate of a Medication with given name
    private LocalDate getLocateDateHelper(String name) {
        LocalDate tempExpDate = null;
        ArrayList<Medication> tempMedList = boxOfMeds.getListOfMeds();

        for (Medication m : tempMedList) {
            if (m.getName().equals(name)) {
                tempExpDate = m.getExpiryDate();
            }
        }
        return tempExpDate;
    }

  // EFFECTS: returns a JFrame with a list of all finished items
    private void showFinishedMeds() {
        JFrame jframe = new JFrame("All Finished Medications");
        JPanel meds = new JPanel();
        meds.setLayout(new FlowLayout());

        List<String> tempMedsList = boxOfMeds.viewFinishedMedications();
        DefaultListModel<String> tempListModel = new DefaultListModel<>();
        JList<String> tempJList = new JList<>(tempListModel);
        for (String s : tempMedsList) {
            tempListModel.addElement(s);
        }

        JScrollPane scrollPanel = new JScrollPane(tempJList);
        JLabel tempLabel;

        if (tempMedsList.isEmpty()) {
            tempLabel = new JLabel("No finished meds found!");
        } else {
            tempLabel = new JLabel(tempMedsList.size() + " finished meds found");
        }

        meds.add(scrollPanel);
        meds.add(tempLabel);

        jframe.add(meds);
        jframe.pack();
        jframe.setVisible(true);
        jframe.setLocationRelativeTo(null);
    }

    // MODIFIES: tempMed
    // EFFECTS: consumes a med with the given number
    private void consumeMed() {
        String tempString =  consumeMedJText.getText();
        JFrame tempFrame = new JFrame("The number of consumed pills were:");
        if (-1 != listMeds.getMaxSelectionIndex()) {
            try {
                int i = listMeds.getSelectedIndex();
                Medication tempMed = listOfMedsModel.get(i);
                int tempConsumed = Integer.parseInt(tempString);
                tempMed.consumePills(tempConsumed);
                JLabel tempLabel = new JLabel(tempString);
                tempFrame.add(tempLabel);
            } catch (NumberFormatException e) {
                tempString = "The number of pills is invalid";
                JLabel tempLabel = new JLabel(tempString);
                tempFrame.add(tempLabel);
            }
        } else {
            tempString = "There are no meds in the box!";
            JLabel tempLabel = new JLabel(tempString);
            tempFrame.add(tempLabel);
        }
        tempFrame.pack();
        tempFrame.setLocationRelativeTo(null);
        tempFrame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: saves BoxOfMeds as JSON
    private void saveMeds() {
        try {
            jsonWriter.open();
            jsonWriter.write(boxOfMeds);
            jsonWriter.close();

            JFrame tempFrame = new JFrame("Saved!");
            JLabel tempLabel = new JLabel("The Box of Meds was saved.");
            tempFrame.add(tempLabel);
            tempFrame.pack();
            tempFrame.setLocationRelativeTo(null);
            tempFrame.setVisible(true);

        } catch (IOException e) {
            JFrame tempFrame = new JFrame("Error occurred");
            JLabel tempLabel = new JLabel("An error occurred while saving the Box of Medication.");
            tempFrame.add(tempLabel);
            tempFrame.pack();
            tempFrame.setLocationRelativeTo(null);
            tempFrame.setVisible(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads JSON as BoxOfMeds
    private void loadMeds() {
        try {
            this.boxOfMeds = jsonReader.read();
            listOfMedsModel = new DefaultListModel<>();

            for (Medication m: boxOfMeds.getListOfMeds()) {
                listOfMedsModel.addElement(m);
            }

            listMeds.setModel(listOfMedsModel);
        } catch (IOException e) {
            JFrame tempFrame = new JFrame("Error occurred");
            JLabel tempLabel = new JLabel("An error occurred while loading the Box of Medication.");
            tempFrame.add(tempLabel);
            tempFrame.pack();
            tempFrame.setLocationRelativeTo(null);
            tempFrame.setVisible(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds new med by creating fields using JText entries
    private void addNewMed() {
        String nameMed = name.getText();
        String consumptionInfo = consumptionInformation.getText();
        int originalPills = Integer.parseInt(originalNumberOfPills.getText());
        int consumedPills = Integer.parseInt(consumedNumberOfPills.getText());
        LocalDate tempDate = LocalDate.parse(expDate.getText());

        try {
            Medication tempMed = new Medication(nameMed, originalPills, tempDate);
            tempMed.setConsumptionInformation(consumptionInfo);
            tempMed.setConsumedNumberOfPills(consumedPills);
            boxOfMeds.addMedication(tempMed);
            listOfMedsModel.addElement(tempMed);
        } catch (DateTimeParseException e) {
            JFrame tempFrame = new JFrame("Error occurred");
            JLabel tempLabel = new JLabel("An error in the input of the date was found.");
            tempFrame.add(tempLabel);
            tempFrame.pack();
            tempFrame.setLocationRelativeTo(null);
            tempFrame.setVisible(true);
        }
    }

    // EFFECTS: N/A
    @Override
    public void windowOpened(WindowEvent e) {
    }

    // EFFECTS: to dispose the frame when we are closing the window
    @Override
    public void windowClosing(WindowEvent e) {
        dispose();
    }

    // EFFECTS: to print all the events logged since the start of run time in order after the application window is
    // closed, then application is terminated
    @Override
    public void windowClosed(WindowEvent e) {
        for (Event event : eventLog) {
            System.out.println(event.toString());
        }
        System.exit(0);
    }

    // EFFECTS: N/A
    @Override
    public void windowIconified(WindowEvent e) {
    }

    // EFFECTS: N/A
    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    // EFFECTS: N/A
    @Override
    public void windowActivated(WindowEvent e) {
    }

    // EFFECTS: N/A
    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}