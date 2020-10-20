import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestaurantMain extends Application {

    private static Restaurant restaurant0520;

    /**
     * Creates a stage/screen for the main home screen to run on. Is Called in the main method.
     * @param primaryStage The stage that is created when the program is run.
     * @throws Exception An IOException
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Opening the main restaurant screen.
        Parent root = FXMLLoader.load(getClass().getResource("/Restaurant.fxml"));
        Scene MainScene = new Scene(root);
        primaryStage.setScene(MainScene);
        primaryStage.show();

        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);
        primaryStage.setTitle("Restaurant 0520");

        restaurant0520 = new Restaurant("restaurant0520");

        try{
            passRestaurantToKitchen(restaurant0520);
            passRestaurantToTableOrdering(restaurant0520);
            passRestaurantToTableLayout(restaurant0520);
            passRestaurantToManager(restaurant0520);
            passRestaurantToBill(restaurant0520);
            passRestaurantToScanOrderArrival(restaurant0520);

            } catch (NullPointerException ex) {
            }
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    /**
     * Passes a single common restaurant to the controller for Kitchen Screens.
     * @param restaurant restaurant0520
     */
    public void passRestaurantToKitchen(Restaurant restaurant){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("KitchenScreen.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(RestaurantMain.class.getName()).log(Level.SEVERE,null,ex);
        }
        KitchenScreenController controller = loader.getController();
        controller.setRestaurant0520(restaurant);
    }

    /**
     * Passes a single common restaurant to the controller for Table Layout Screens.
     * @param restaurant restaurant0520
     */
    public void passRestaurantToTableLayout(Restaurant restaurant){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TableLayouts.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(RestaurantMain.class.getName()).log(Level.SEVERE,null,ex);
        }
        TableLayoutController controller = loader.getController();
        controller.setRestaurant0520(restaurant);

    }

    /**
     * Passes a single common restaurant to the controller for Table Ordering Screens.
     * @param restaurant restaurant0520
     */
    public void passRestaurantToTableOrdering(Restaurant restaurant){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/TableOrdering.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(RestaurantMain.class.getName()).log(Level.SEVERE,null,ex);
        }
        TableOrderingController controller = loader.getController();
        controller.setRestaurant0520(restaurant);
    }

    /**
     * Passes a single common restaurant to the controller for Manager Screens.
     * @param restaurant restaurant0520
     */
    public void passRestaurantToManager(Restaurant restaurant)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Manager.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(RestaurantMain.class.getName()).log(Level.SEVERE,null,ex);
        }
        ManagerController controller = loader.getController();
        controller.setRestaurant0520(restaurant);
    }

    /**
     * Passes a single common restaurant to the controller for Bill Screens.
     * @param restaurant restaurant0520
     */
    public void passRestaurantToBill(Restaurant restaurant)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ShowBill.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(RestaurantMain.class.getName()).log(Level.SEVERE,null,ex);
        }
        ShowBillController controller = loader.getController();
        controller.setRestaurant0520(restaurant);
    }

    /**
     * Passes a single common restaurant to the controller for Scan Order Arrival Screens.
     * @param restaurant restaurant0520
     */
    public void passRestaurantToScanOrderArrival(Restaurant restaurant)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ScanOrderArrival.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(RestaurantMain.class.getName()).log(Level.SEVERE,null,ex);
        }
        ScanOrderArrivalController controller = loader.getController();
        controller.setRestaurant0520(restaurant);
    }
}