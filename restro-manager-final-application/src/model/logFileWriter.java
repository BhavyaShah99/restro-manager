package model;

import javafx.scene.control.Alert;

import java.io.*;

public class logFileWriter {

    public void writeToLog(String input)
    {
        String writeString = input;
        File requestFile = new File("log.txt");
        try {
            FileWriter fw = new FileWriter(requestFile, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(writeString);
            pw.close();
        } catch (FileNotFoundException ex) {
            fileNotFoundAlert();
        } catch (IOException ex) {
            invalidFileDataAlert();
        }
    }

    /**
     * Displays alert box error for a file not found error.
     */
    private void fileNotFoundAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("File :");
        alert.setContentText("Inventory file not found");
        alert.showAndWait();
    }

    /**
     * Displays alert box error for invalid data in the file.
     */
    private void invalidFileDataAlert()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("File :");
        alert.setContentText("Inventory file data input is invalid");
        alert.showAndWait();
    }
}
