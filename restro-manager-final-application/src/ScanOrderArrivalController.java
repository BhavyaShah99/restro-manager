import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ScanOrderArrivalController implements Initializable {

    /**
     * Instance of Receiver and Inventory
     */
    static Manager manager;
    static Server server;
    static Cook cook;
    static Restaurant restaurant0520;
    static Inventory inventory;

    /**
     * All Observable lists
     */
    private final ObservableList<String> choiceBoxOptions = FXCollections.observableArrayList();
    private final ObservableList<String> employeeRoleOptions = FXCollections.observableArrayList("Manager","Server","Cook");

    /**
     * Choice Boxes
     */
    @FXML
    private ChoiceBox<String> ingredientChoices = new ChoiceBox<String>();
    @FXML
    private ChoiceBox<String> employeeRoleChoiceBix = new ChoiceBox<String>();

    /**
     * Text Fields
     */
    @FXML
    private TextField qtyText = new TextField();
    @FXML
    private TextField newIngredientTextField = new TextField();
    @FXML
    private TextField newIngredientQtyTextField = new TextField();
    @FXML
    private TextField newIngredientThresholdTextField = new TextField();
    @FXML
    private TextField newIngredientUnitsTextField = new TextField();
    @FXML
    private TextField employeeIDTextField = new TextField();

    /**
     * Buttons
     */
    @FXML
    private Button scanArrivalBtn;
    @FXML
    private Button backToInventory;
    @FXML
    private Button addNewIngredientToInventoryBtn;
    @FXML
    private Button  backToMainBtn;
    @FXML
    private Button clearFieldsBtnTab1;
    @FXML
    private Button clearFieldsBtnTab2;

    private ArrayList<Ingredient> inventoryList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
try{
        inventory = restaurant0520.getRestaurantManager().getInventory();

        inventoryList = inventory.getIngredientList();

        String filePath = "inventory.txt";
        File file = new File(filePath);

        /*
         * Reading the inventory and adding the ingredients to the scan dropdown menu.
         */
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String line = fileReader.readLine(); //skipping insignificant lines
            fileReader.readLine();//skipping insignificant lines
            fileReader.readLine();//skipping insignificant lines
            int i = 0;
            String[] testBlank = null;
            while (line != null) {
                line = fileReader.readLine();
                if (line != null) {
                    testBlank = line.split(",");
                }
                i++;
                if (line != null && testBlank.length == 4) {
                    String[] array = line.split(",");
                    String name = array[0];
                    choiceBoxOptions.add(name);
                } else
                {
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File :");
            alert.setContentText("Inventory file not found");
            alert.showAndWait();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File :");
            alert.setContentText("Inventory file data input is invalid");
            alert.showAndWait();
        }

        /*
         * Setting the dropdown options for all the choice boxes
         */
        ingredientChoices.setItems(choiceBoxOptions);

        employeeRoleChoiceBix.setItems(employeeRoleOptions);

        /*
         * Takes the user back to the home screen on click of the backToMainBtn
         */
        backToMainBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Parent restaurantViewParent = FXMLLoader.load(getClass().getResource("Restaurant.fxml"));
                    Scene restaurantScene = new Scene(restaurantViewParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setTitle("Restaurant 0520");
                    window.setScene(restaurantScene);
                    window.show();
                    window.setResizable(false);
                    window.fullScreenProperty();
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Page:");
                    alert.setContentText("Unable to load Restaurant Screen");
                    alert.showAndWait();
                }
            }
        });

        /*
         Button to go back to the inventory.
         */
        backToInventory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Parent tableViewParent = FXMLLoader.load(getClass().getResource("Manager.fxml"));
                    Scene tablesScene = new Scene(tableViewParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setTitle("Manager");
                    window.setScene(tablesScene);
                    window.show();
                    window.setResizable(false);
                    window.fullScreenProperty();
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Page:");
                    alert.setContentText("Unable to load Manager Screen");
                    alert.showAndWait();
                }
            }
        });

        /*
         Button to scan new ingredients that arrive into the inventory
         */
        scanArrivalBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (employeeIDTextField.getText().equals("") || qtyText.getText().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Request Failed");
                    alert.setContentText("Please fill in all the fields.");
                    alert.showAndWait();
                }
                else {

                    try{
                        Double employeeIDTextFieldTest = Double.parseDouble(employeeIDTextField.getText());
                        Double qtyTextTest = Double.parseDouble(qtyText.getText());
                    } catch (NumberFormatException ex)
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Input");
                        alert.setContentText("Quantity or ID entered is not a number");
                        alert.showAndWait();
                    }
                    String tempUnit = "";
                    for (int i = 0; i < inventoryList.size(); i++) {
                        if (inventoryList.get(i).getName().equals(ingredientChoices.getValue())) {
                            tempUnit = inventoryList.get(i).getUnit();
                            break;
                        }
                    }

                    String units = tempUnit;

                    if(employeeRoleChoiceBix.getSelectionModel().getSelectedItem().equals("Manager"))
                    {
                        manager = (Manager) restaurant0520.getEmployee("M:"+employeeIDTextField.getText());
                        if (manager != null)
                        {
                            manager.scan(ingredientChoices.getValue(),Double.parseDouble(qtyText.getText()),units);
                            inventory.updateInventoryFile();
                            scanConfirmAlert();
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Alert");
                            alert.setHeaderText("InputError");
                            alert.setContentText("Please input a valid Employee Info. Entered employee doesn't exist");
                            alert.showAndWait();
                        }
                    }
                    else if (employeeRoleChoiceBix.getSelectionModel().getSelectedItem().equals("Server"))
                    {
                        server = (Server) restaurant0520.getEmployee("S:"+employeeIDTextField.getText());
                        if (server != null)
                        {
                            server.scan(ingredientChoices.getValue(),Double.parseDouble(qtyText.getText()),units);
                            inventory.updateInventoryFile();
                            scanConfirmAlert();
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Alert");
                            alert.setHeaderText("InputError");
                            alert.setContentText("Please input a valid Employee Info. Entered employee doesn't exist");
                            alert.showAndWait();
                        }
                    }
                    else if (employeeRoleChoiceBix.getSelectionModel().getSelectedItem().equals("Cook"))
                    {
                        cook = (Cook) restaurant0520.getEmployee("C:"+employeeIDTextField.getText());
                        if (cook != null)
                        {
                            cook.scan(ingredientChoices.getValue(),Double.parseDouble(qtyText.getText()),units);
                            inventory.updateInventoryFile();
                            scanConfirmAlert();
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Alert");
                            alert.setHeaderText("InputError");
                            alert.setContentText("Please input a valid Employee Info. Entered employee doesn't exist");
                            alert.showAndWait();
                        }
                    }
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Null Employee Error");
                        alert.setContentText("Please select your role and enter your ID number.");
                        alert.showAndWait();
                    }
                }
            }
        });

        /*
         * Button to add a new ingredient to the inventory file.
         */
        addNewIngredientToInventoryBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String ingredient = newIngredientTextField.getText();
                String qty = newIngredientQtyTextField.getText();
                String threshold = newIngredientThresholdTextField.getText();
                String ingredientUnit = newIngredientUnitsTextField.getText();

                if (newIngredientTextField.getText().equals("") || newIngredientQtyTextField.getText().equals("")
                        || newIngredientThresholdTextField.getText().equals("")
                        || newIngredientUnitsTextField.getText().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Request Failed");
                    alert.setContentText("Please fill in all the fields.");
                    alert.showAndWait();
                } else {
                    try {
                        Double qtyTest = Double.parseDouble(qty);
                        Double thresholdTest = Double.parseDouble(threshold);
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Input");
                        alert.setContentText("Quantity or Threshold entered is not a number");
                        alert.showAndWait();
                    }
                    Ingredient newIngredient = new Ingredient(ingredient,Double.parseDouble(qty),Double.parseDouble(threshold),ingredientUnit);

                    inventoryList.add(newIngredient);

                    inventory.updateInventoryFile();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmed");
                    alert.setHeaderText("Added");
                    alert.setContentText(ingredient+" was added to the inventory");
                    alert.showAndWait();
                }


            }
        });

        clearFieldsBtnTab1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                employeeRoleChoiceBix.getSelectionModel().clearSelection();
                ingredientChoices.getSelectionModel().clearSelection();
                employeeIDTextField.setText("");;
                qtyText.setText("");;
            }
        });

        clearFieldsBtnTab2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newIngredientTextField.setText("");
                newIngredientQtyTextField.setText("");
                newIngredientThresholdTextField.setText("");
                newIngredientUnitsTextField.setText("");
            }
        });

        } catch (NullPointerException ex) {
        }
    }

    public void setRestaurant0520(Restaurant restaurant)
    {
        restaurant0520 = restaurant;
    }

    public void scanConfirmAlert()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Scan Confirmed");
        alert.setHeaderText("Scanned");
        alert.setContentText("New order arrival was added to the Inventory");
        alert.showAndWait();
    }
}