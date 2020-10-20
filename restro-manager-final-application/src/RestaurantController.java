import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RestaurantController {

    /**
     * Called when the Inventory/Manager button is clicked on the main page. Opens up Manager.fxml
     */
    @FXML
    public void openManagerGUI(ActionEvent event) throws Exception
    {
        Parent managerViewParent = FXMLLoader.load(getClass().getResource("/Manager.fxml"));
        Scene managerScene = new Scene(managerViewParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Manager");
        window.setScene(managerScene);
        window.show();
    }

    /**
     * Called when the Kitchen button is clicked on the main page. Opens up KitchenScreen.fxml
     */
    public void openKitchenGUI(ActionEvent event) throws Exception
    {
        Parent kitchenViewParent = FXMLLoader.load(getClass().getResource("/KitchenScreen.fxml"));
        Scene kitchenScene = new Scene(kitchenViewParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Kitchen Display");
        window.setScene(kitchenScene);
        window.show();
    }

    /**
     * Called when the Tables button is clicked on the main page. Opens up TableLayouts.fxml
     */
    public void openTablesGUI(ActionEvent event) throws IOException {
        Parent managerViewParent = FXMLLoader.load(getClass().getResource("/TableLayouts.fxml"));
        Scene managerScene = new Scene(managerViewParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Table Layout");
        window.setScene(managerScene);
        window.show();
    }

    /**
     * Called when the Scan button is clicked on the main page. Opens up ScanOrderArrival.fxml
     */
    public void openScanScreen(ActionEvent event) throws IOException {
        Parent scanParent = FXMLLoader.load(getClass().getResource("/ScanOrderArrival.fxml"));
        Scene scanScene = new Scene(scanParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Scan Order Arrival");
        window.setScene(scanScene);
        window.show();
    }
}
