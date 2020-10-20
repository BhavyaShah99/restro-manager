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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import model.*;

public class KitchenScreenController implements Initializable {

    public TableOrderingController serverScreenController;


    @FXML
    private Button backBtn,OrderIsReadyBtn,orderConfirmBtn,startCookingBtn,refreshBtn;
    @FXML
    private ListView ordersListView,cookingListView;

    static Restaurant restaurant0520;
    private Cook cook;

    //TODO change the data type of list to orders
    RestaurantController restaurantController = new RestaurantController();

    static Map<Integer, Order> ordersNotReady;

    static ObservableList<String> ordersData = FXCollections.observableArrayList();
    static ObservableList<String> cookingData = FXCollections.observableArrayList();

/*
If we want to have multiple KitchenScreenController windows, we need to declare 'cook' outside of this
class.     cook = (Cook) restaurant0520.getEmployee("C:"+0);      and assign it to each new kitchenScreenController
window that we decide to have. The 'Orders' list in the GUI reads from static restaurant0520, and the 'Cooking' list
reads from private cook so each KitchenScreenController window will share an identical order list but have different
'Cooking' lists depending on how many orders each cook decides to handle using the 'confirm order' button.
 */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ordersNotReady = restaurant0520.getRestaurantManager().getOrderManager().getOrdersNotReady();
            cook = (Cook) restaurant0520.getEmployee("C:" + 0);
            cookingListView.getSelectionModel().selectFirst();
            ordersListView.getSelectionModel().selectFirst();


            //Initialize ListView,TextField
            updateScreen();

            // Back btn to exit to main menu.
            backBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Parent restaurantViewParent = FXMLLoader.load(getClass().getResource("Restaurant.fxml"));
                        Scene restaurantScene = new Scene(restaurantViewParent);
                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        window.setTitle("RestaurantController 0520");
                        window.setScene(restaurantScene);
                        window.show();
                        window.setResizable(false);
                        window.fullScreenProperty();
                    } catch (IOException ex) {
                    }
                }

            });
        } catch (NullPointerException ex) {
        }
    }

    private void updateScreen()
    {
        ordersData = getUnConfirmedOrders();
        cookingData = arrayListToObservableList(cook.getJobs());
        ordersListView.setItems(ordersData);
        cookingListView.setItems(cookingData);
    }

    private ObservableList getUnConfirmedOrders ()
    {
        ObservableList<String> ret = FXCollections.observableArrayList();
        ordersNotReady = restaurant0520.getRestaurantManager().getOrderManager().getOrdersNotReady();
        for (Map.Entry<Integer, Order> orderEntry : ordersNotReady.entrySet())
        {
            int key = orderEntry.getKey();
            Order order = orderEntry.getValue();
            if (!order.getIsConfirmed())
            {
                if(order.getDish().isModified()==true){
                    String str = "#"+key+"(T"+ order.getTableNumber() +")"+ "\t" + order.getDish().getName() + "\tModified:  "+order.getDish().getModifiedIngredients();
                    if(order.getDish().getMsg().equals("")==false){str+=" Rejected Item: "+ order.getDish().getMsg();}
                    ret.add(str);
                }
                else{
                    String str = "#"+key+"(T"+ order.getTableNumber() +")"+ "\t" + order.getDish().getName() + "\t ";
                    if(order.getDish().getMsg().equals("")==false){str+=" Rejected Item: "+ order.getDish().getMsg();}
                    ret.add(str);
                }
            }
        }
        return ret;
    }

    public void clickOrderConfirmBtn(ActionEvent event) {
        if(ordersData.size()>0) {
            cook.getJob();
            updateScreen();
        }
    }

    public void clickStartCooking(ActionEvent event) {
        if (cook.getJobs().size()>0) {
            //cook.cookJob(cook.getJobs().get(0).getOrderId());
            cook.cookJob(getFirstJobInList());
            updateScreen();
        }
    }

    /**
     * When the 'order is ready' button is clicked, the order is no longer managed by the cook and is
     * sent to OrdersReadyForPickup list in OrderManager. It should be visible in 'Orders Ready To Serve' list in
     * the Server GUI.
     * @param event
     */
    public void clickOrderIsReady(ActionEvent event) {
        cook.fillOrder(cook.getJobs().get(0).getOrderId());
        updateScreen();
    }

    /**
     *
     * @return Returns the first job in cook.Jobs that hasn't began being cooked yet.
     */
    private int getFirstJobInList()
    {
        int ret = -1;
        for (int i=cook.getJobs().size()-1; i>=0; i--)
        {
            Order order = cook.getJobs().get(i);
            if (!order.getBeganCooking())
            {
                ret = order.getOrderId();
            }
        }
        return ret;
    }

    private void mapToList(HashMap<Integer,Order> map,ObservableList<String> observableList){
        for (Map.Entry<Integer, Order> pair : map.entrySet()) {
            observableList.add(pair.getKey().toString()+" "+pair.getValue().toString());
        }
    }

    private ObservableList arrayListToObservableList (ArrayList<Order> jobs)
    {
        ObservableList<String> ret = FXCollections.observableArrayList();
        for (Order order : jobs)
        {
            if (order.getBeganCooking())
            {
                String str = String.valueOf(order.getOrderId());
                ret.add("#" + str + "\t" + order.getDish().getName() + ",\t Currently cooking ");
            } else {
                String str = String.valueOf(order.getOrderId());

                if(order.getDish().isModified()==true){
                    String string = "#"+str+"(T"+ order.getTableNumber() +")"+ "\t" + order.getDish().getName() + "\tModified:  "+order.getDish().getModifiedIngredients();
                    ret.add(string);
                }
                else{
                    String string = "#"+str+"(T"+ order.getTableNumber() +")"+ "\t" + order.getDish().getName() + "\t ";
                    ret.add(string);
                }
            }
        }
        return ret;
    }


    public void clickRefreshBtn(ActionEvent event) {
    }

    public void setRestaurant0520(Restaurant restaurant0520){
        this.restaurant0520 = restaurant0520;
    }

}
