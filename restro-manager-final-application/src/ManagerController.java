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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.*;


public class ManagerController implements Initializable {

    private static Restaurant restaurant0520;
    private static Inventory inventory;

    /**
     * All Observable lists
     */
    @FXML
    private ObservableList<String> choiceBoxOptions = FXCollections.observableArrayList();

    private ObservableList<Ingredient> ingredientsArrayList = FXCollections.observableArrayList();

    /**
     * All ArrayLists
     */
    private ArrayList<Ingredient> ingredients = new ArrayList<>();


    /**
     * All variables related to choice boxes
     */
    @FXML
    private ChoiceBox<String> ingredientChoices = new ChoiceBox<>();

    /**
     * TextFields
     */
    @FXML
    private TextField ingredientOrderQty;
    @FXML
    private TextField serverIDTextField;

    /**
     * All variables related to buttons
     */
    @FXML
    private Button backToRestaurantMainBtn;
    @FXML
    private Button openTablesPageBtn;
    @FXML
    private Button reorderBtn;
    @FXML
    private Button refreshButton;
    @FXML
    private Button checkForReorderingBtn;
    @FXML
    private Button getServerInfoBtn;
    @FXML
    private Button addEmpBtn;

    @FXML
    private ChoiceBox empRoleChoiceBox;

    @FXML
    private TextField newEmpNameTextField;


    /**
     * All variables related to the table view
     */
    @FXML
    private TableView<Ingredient> ingredientTableView = new TableView<>();

    @FXML
    private ListView<String> ServerListView = new ListView<>();

    @FXML
    private ListView<String> EmployeeList = new ListView<>();

    private static ArrayList<Server> serverList;
    private static ArrayList<Cook> cookList;
    private static ArrayList<Receiver> employeeList;


    /**
     * Initializes method that will run when the manager window is opened.
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            inventory = restaurant0520.getRestaurantManager().getInventory();
            serverList = restaurant0520.getAllEmployeesOfType("S");
            cookList = restaurant0520.getAllEmployeesOfType("C");
            employeeList = restaurant0520.getEmployees();

            ObservableList<String> employeeDisplayList = generateObsEmployeeList();
            EmployeeList.setItems(employeeDisplayList);

            ObservableList<String> employeeTypeList = FXCollections.observableArrayList();

            employeeTypeList.addAll("Server","Cook","Manager");
            empRoleChoiceBox.setItems(employeeTypeList);

            /*
             * Reading the inventory and adding the ingredients to the reorder dropdown menus observable list
             */
            String filePath = "inventory.txt";
            File file = new File(filePath);
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
                    } else {
                        break;
                    }
                }
                } catch(FileNotFoundException ex){
                    fileNotFoundAlert();
                } catch(IOException ex){
                    invalidFileDataAlert();
                }

                /*
                 * Sets the drop down options for the ingredient names that can be reordered.
                 */
                ingredientChoices.setItems(choiceBoxOptions);

                /*
                 * Next four lines set the columns for the table view.
                 */
                TableColumn colName = new TableColumn("Name");
                colName.setPrefWidth(250);
                colName.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("name"));

                TableColumn colQty = new TableColumn("Quantity");
                colQty.setPrefWidth(140);
                colQty.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("amount"));

                TableColumn colThreshold = new TableColumn("Threshold");
                colThreshold.setPrefWidth(140);
                colThreshold.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("threshold"));

                TableColumn colUnits = new TableColumn("Units");
                colUnits.setPrefWidth(160);
                colUnits.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("unit"));

                colName.setPrefWidth(180);
                colQty.setPrefWidth(115);
                colThreshold.setPrefWidth(115);
                colUnits.setPrefWidth(125);

                /*
                 * Adds the Observable list to the table view and adds the columns to the table.
                 */
                ingredientTableView.setItems(getInventory());
                ingredientTableView.getColumns().addAll(colName, colQty, colThreshold, colUnits);

                /*
                 * Setting table view to editable
                 */
                ingredientTableView.setEditable(true);

                /*
                 * The action performed when the backToRestaurantMainBtn is clicked sends the user back to the main
                 * restaurant homepage.
                 */
                backToRestaurantMainBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
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
                 * The action performed when the openTablesPageBtn is clicked sends the user back to the tables layout
                 * page
                 */
                openTablesPageBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        try {
                            Parent tableViewParent = FXMLLoader.load(getClass().getResource("TableLayouts.fxml"));
                            Scene tablesScene = new Scene(tableViewParent);
                            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            window.setTitle("Tables");
                            window.setScene(tablesScene);
                            window.show();
                            window.setResizable(false);
                            window.fullScreenProperty();
                        } catch (IOException ex) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Page:");
                            alert.setContentText("Unable to load Tables Screen");
                            alert.showAndWait();
                        }
                    }
                });

                /*
                 * The action performed when the reorderBtn is clicked, it reorders the ingredient that the manger
                 * selects to reorder and writes the reorder request to the request.txt file
                 */
                reorderBtn.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {

                        String chosenIngredient = ingredientChoices.getSelectionModel().getSelectedItem();
                        String reorderQty = ingredientOrderQty.getText();

                        String tempUnit = "";
                        for (int i = 0; i < ingredientsArrayList.size(); i++) {
                            if (ingredientsArrayList.get(i).getName().equals(chosenIngredient)) {
                                tempUnit = ingredientsArrayList.get(i).getUnit();
                                break;
                            }
                        }

                        String units = tempUnit;
                        if (!ingredientChoices.isShowing() && ingredientOrderQty.getText().equals("") && tempUnit.equals("")) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Request Failed");
                            alert.setContentText("The request for reordering failed as no ingredient or quantity was " +
                                    "selected");
                            alert.showAndWait();
                        } else {

                            try {
                                Double reorderQtyTest = Double.parseDouble(reorderQty);
                                String request = chosenIngredient + "," + reorderQty + "," + units;
                                File requestFile = new File("request.txt");
                                try {
                                    FileWriter fw = new FileWriter(requestFile, true);
                                    PrintWriter pw = new PrintWriter(fw);
                                    pw.println(request);
                                    pw.close();
                                } catch (FileNotFoundException ex) {
                                    fileNotFoundAlert();
                                } catch (IOException ex) {
                                    invalidFileDataAlert();
                                }

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmed");
                                alert.setHeaderText("Request");
                                alert.setContentText("The request for " + reorderQty + " " + units + " " + chosenIngredient + " has been placed");
                                alert.showAndWait();
                            } catch (NumberFormatException ex) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Invalid Input");
                                alert.setHeaderText("Check Input");
                                alert.setContentText("Quantity entered to reorder is a not a Number");
                                alert.showAndWait();
                            }
                        }
                    }
                });

                /*
                 * Refreshes the table view so that the manager can view an up to date inventory.
                 */
                refreshButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        ingredientsArrayList.clear();
                        /*
                         * Clearing all the data from the table
                         */
                        for (int i = 0; i < ingredientTableView.getItems().size(); i++) {
                            ingredientTableView.getItems().clear();
                        }
                        ingredientTableView.getColumns().clear();
                        /*
                         * Next four lines are resetting the data and refresh the table
                         */
                        TableColumn colName = new TableColumn("Name");
                        colName.setPrefWidth(250);
                        colName.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("name"));

                        TableColumn colQty = new TableColumn("Quantity");
                        colQty.setPrefWidth(140);
                        colQty.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("amount"));

                        TableColumn colThreshold = new TableColumn("Threshold");
                        colThreshold.setPrefWidth(140);
                        colThreshold.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("threshold"));

                        TableColumn colUnits = new TableColumn("Units");
                        colUnits.setPrefWidth(160);
                        colUnits.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("unit"));
                        /*
                         * Adds the refreshed Observable list to the table view and adds the columns to the table.
                         */
                        ingredientTableView.setItems(getInventory());
                        ingredientTableView.getColumns().addAll(colName, colQty, colThreshold, colUnits);
                    }
                });

                /*
                 * This button is clicked to check if any ingredients are low in stock in the inventory.
                 */
                checkForReorderingBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        checkForReordering();
                    }
                });

                /*
                 * Uses the entered server id to get all info and activity data on the server so tha manger can keep track.
                 */
                    getServerInfoBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        serverList = restaurant0520.getAllEmployeesOfType("S");
                        String serverID = serverIDTextField.getText();
                        Server server = getServer(serverID);

                        ObservableList<String> display = FXCollections.observableArrayList();
                        display.clear();
                        if (server != null) {
                            ArrayList serverHistory = server.getHistory();
                            display = arrayListToObservableList(serverHistory);
                            Collections.reverse(display);
                        } else {
                            display.add("Not a valid server ID");
                        }
                        if (display.size()==0) {
                            display.add("This server currently has no history");
                        }
                            ServerListView.setItems(display);

                        //Add functionality here to get info on server history.
                    }
                });

                /*
                 * Creates and adds a new employee to the employees.txt file.
                 */
                addEmpBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String newEmployeeName = newEmpNameTextField.getText();
                        String type = (String) empRoleChoiceBox.getValue();

                        if (type != null)
                        {
                            if (!newEmployeeName.isEmpty() && newEmployeeName != null)
                            {
                                addEmployee(type,newEmployeeName);
                                restaurant0520.writeEmployeeTextFile();
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation");
                                alert.setHeaderText("Employee successfully added");
                                alert.setContentText("Name: "+newEmployeeName+",   Type: "+type);
                                alert.showAndWait();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Creating new employee");
                                alert.setContentText("Please enter a valid Employee name");
                                alert.showAndWait();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Creating new employee");
                            alert.setContentText("Please select an employee type");
                            alert.showAndWait();
                        }
                        refreshEmployeeDisplay();
                    }
                });
            }catch (NullPointerException ex) {
        }
        }

    /**
     * Sets the restaurant. Used in the RestaurantMain class to.
     */
    public void setRestaurant0520(Restaurant restaurant)
    {
        restaurant0520 = restaurant;
    }

    /**
     * Refreshes the list view of all employees.
     */
    private void refreshEmployeeDisplay ()
    {
        employeeList = restaurant0520.getEmployees();
        ObservableList<String> employeeDisplayList = generateObsEmployeeList();
        EmployeeList.setItems(employeeDisplayList);
    }

    /**
     * Adds a new employee to the respective lists based on the type of employee.
     * @param type Type of the new Employee
     * @param name Name of the new Employee
     */
    private void addEmployee(String type, String name)
    {
        String ret = null;
        switch (type)
        {
            case "Server" :
                restaurant0520.addServer(name);
                break;
            case "Cook" :
                restaurant0520.addCook(name);
                break;
            case "Manager" :
                restaurant0520.addManager(name);
                break;
        }
    }

    /**
     * Returns the respective server based on the ID entered
     * @param serverID The Id of the server that is returned
     * @return A server object.
     */
    private Server getServer(String serverID)
    {
        Server ret = null;
        for (Server server : serverList)
        {
            if (server.getEmployeeId().split(":")[1].equals(serverID))
            {
                ret = server;
            }
        }
        return ret;
    }

    /**
     * Generates an Observable list of all employees.
     * @return An Observable List.
     */
    private ObservableList generateObsEmployeeList ()
    {
        ObservableList<String> ret = FXCollections.observableArrayList();
        String formatStr = "%-5s %-20s %-30s%n";
        ret.add(String.format(formatStr, "[ID]", "[Role]", "[Name]"));
        for (Receiver receiver : employeeList)
        {
            String id = receiver.getEmployeeId();
            String role = id.split(":")[0];
            switch (role) {
                case "S" :
                    role = "Server";
                    break;
                case "C" :
                    role = "Cook";
                    break;
                case "M" :
                    role = "Manager";
                    break;
            }
            String name = receiver.getName();

            ret.add(String.format(formatStr, id, role, name));
        }
        return ret;
    }

    /**
     * Reads the inventory.txt file and adds Ingredient objects to the ObservableList ingredientsArrayList
     */
    private ObservableList<Ingredient> getInventory()
    {
        ingredients = inventory.getIngredientList();
        ingredientsArrayList.addAll(ingredients);
        return ingredientsArrayList;
    }

    /**
     * Used to create an Observable list of a servers Info and his/her order History.
     * @param aListString An ArrayList of strings
     * @return An Observable list.
     */
    private ObservableList arrayListToObservableList (ArrayList<String> aListString)
    {
        ObservableList<String> ret = FXCollections.observableArrayList();
        ret.clear();
        for (int i=0; i<aListString.size(); i++)
        {
            ret.add(aListString.get(i));
        }
        return ret;
    }

    private void refreshServerInfoTable()
    {

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

    /**
     * Loops through the ObservableList of ingredients to check if any ingredients are low on stock
     * in the inventory.
     */
    private void checkForReordering()
    {
        String reorderAlert = "";
        for(int i=0 ;i<ingredientsArrayList.size();i++)
        {
            if (ingredientsArrayList.get(i).reorder())
            {
                reorderAlert = reorderAlert + ingredientsArrayList.get(i).getName()+" quantity is low. Currently: "+
                        ingredientsArrayList.get(i).getAmount()+" "+ingredientsArrayList.get(i).getUnit()+" in stock \n";
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Low Inventory Alert");
        if (reorderAlert.equals(""))
        {
            alert.setContentText("No ingredients are low on stock");
        }
        else
        {
            alert.setContentText(reorderAlert);
        }
        alert.showAndWait();
    }
}
