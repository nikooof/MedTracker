package ui;

import java.io.FileNotFoundException;

// Main file to run the program, else throws fileNotFoundException if the file was not found
public class Main {
    public static void main(String[] args) {
        try {
            new MedsTrackerGUI();
//            new MedsTracker();
        } catch (FileNotFoundException e) {
            System.out.println("The file was not found!");
        }
    }
}