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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TableOrderingController implements Initializable {

    static Restaurant restaurant0520;
    static Server server;
    static Integer tableNumber;
    static Integer tableSeats;
    static String serverId;
    Order clickedOrder;



    @FXML TextField selectedOrderLabel;
    @FXML Label tablenumLabel;
    @FXML TextField modificationQuantity,serverIdTxt,seatTxt; //INPUT
    @FXML ListView menuListView,modiListView,ordersListView,reasonListView,reOrderListView,billListView,tableBillListView;
    @FXML Button addModificationBtn,addBtn,printBillBtn,backToTableLayoutBtn,backBtn,returnBtn,modeBtn,payBtn;

    @FXML Button seatSetBtn,serverSetBtn,addSplitBtn,addAllBtn;

    static Boolean tableIsOccupied=false;
    static Boolean serverIdSeted = false;
    static Boolean isClickedOrder = false;

    Map<String,Double> orderModification = new HashMap<>();
    ArrayList<Order> tablesOrders;

    ObservableList<Order> orders = FXCollections.observableArrayList();
    ObservableList<Order> billOrders = FXCollections.observableArrayList();
    ObservableList<Order> currBillOrders = FXCollections.observableArrayList();
    ObservableList<String> menus = FXCollections.observableArrayList();
    ObservableList<String> reasons = FXCollections.observableArrayList("Too cold","Too hot","Taste Bad","Etc");
    ObservableList<String> reOrderBool = FXCollections.observableArrayList("ReOrder","Not ReOrder");

    ObservableList<Order> spilitOrders = FXCollections.observableArrayList();





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setMenu();

            if (restaurant0520.getRestaurantManager().getTableManager().getTable(tableNumber).isOccupied() == false) {
                setDefaultMode();

            } else {
                setGetBackMode();
            }

            backBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Parent restaurantViewParent = FXMLLoader.load(getClass().getResource("/Restaurant.fxml"));
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

            backToTableLayoutBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Parent tableLayoutView = FXMLLoader.load(getClass().getResource("TableLayouts.fxml"));
                        Scene tableScene = new Scene(tableLayoutView);
                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        window.setTitle("Table Layout");
                        window.setScene(tableScene);
                        window.show();
                        window.setResizable(false);
                        window.fullScreenProperty();
                    } catch (IOException ex) {
                    }
                }
            });
        } catch(NullPointerException ex)
        {

        }
    }
    public void clickAddBtn(ActionEvent actionEvent) {
        try {
            String selectedItem = (String) menuListView.getSelectionModel().getSelectedItem();
            selectedItem = selectedItem.split(" ",2)[1].trim();
            //Dish selectedItem = (Dish)menuListView.getSelectionModel().getSelectedItem();
            if(orderModification.isEmpty()){
                if(server.createOrder(tableNumber,selectedItem)==true){
                    updateOberveOrders();
                    isClickedOrder = true;
                    //cancelBtn.setDisable(isClickedOrder);
                }
            } else {
                if(server.createOrder(tableNumber,selectedItem,orderModification) == true) {
                    orderModification.clear();
                    updateOberveOrders();
                    isClickedOrder = true;
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setHeaderText("Input Error");
            alert.setContentText("Please choose a menu item to order.");
            alert.showAndWait();
        }
    }


    public void notEnoughIngredientAlert()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Alert");
        alert.setHeaderText("Ingredient Alert:");
        alert.setContentText("Not enough ingredients to make ");
        alert.showAndWait();
    }

    public void setRestaurant0520(Restaurant restaurant){
        restaurant0520 = restaurant;
    }

    public void setMenu(){
        ArrayList<Dish> dishs = restaurant0520.getRestaurantManager().getMenu().getMenu();
        for(Dish dish : dishs){
            String formatStr = "%-10s %-40s%n";
            String line = String.format(formatStr, "$"+dish.getPrice(), dish.getName());
            menus.add(line);
        }
        menuListView.setItems(menus);
        menuListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }



    public void updateOberveOrders(){
        Table table = restaurant0520.getRestaurantManager().getTableManager().getTable(tableNumber);
        tablesOrders = table.getOrders();

        orders.clear();
        for (Order order:tablesOrders){
            orders.add(order);
        }
        billOrders.clear();
        for (Order order:table.getBill().getServedOrders()){
            billOrders.add(order);
        }
        tableBillListView.setItems(billOrders);
        ordersListView.setItems(orders);

    }


    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public boolean isInteger( String input )
    {
        try
        {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e )
        {
            return false;
        }
    }


    public void clickMenuItem(MouseEvent mouseEvent) {
        orderModification.clear();
        modiListView.setDisable(false);
        addBtn.setDisable(false);
        modeBtn.setDisable(true);

        String dishStr = (String)menuListView.getSelectionModel().getSelectedItem();
        dishStr = dishStr.split(" ",2)[1].trim();
        Dish dish = restaurant0520.getRestaurantManager().getMenu().getDish(dishStr);
        Map<Ingredient, Double> recipe = dish.getDishRecipe();
       ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();
       for(Map.Entry<Ingredient,Double> item:recipe.entrySet()){
           ingredients.add(item.getKey());
       }
       modiListView.setItems(ingredients);
    }

    public void clickAddModificationBtn(ActionEvent event) {
        try {
            Ingredient ingredient = (Ingredient) modiListView.getSelectionModel().getSelectedItem();
            Double quantity = Double.valueOf(modificationQuantity.getText());
            modificationQuantity.clear();
            orderModification.put(ingredient.getName(), quantity);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setHeaderText("Input Error");
            alert.setContentText("Please enter a valid number of ingredients to be changed");
            alert.showAndWait();
        }

    }


    public void clickModiListView(MouseEvent mouseEvent) {
        addModificationBtn.setDisable(false);
        modificationQuantity.setEditable(true);
    }



    public void clickPrintBillBtn(ActionEvent event) throws IOException {
        restaurant0520.getRestaurantManager().getTableManager().getTable(tableNumber).getBill().updateBill();
        passTableToBill(tableNumber);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ShowBill.fxml"));
        Parent p = FXMLLoader.load(getClass().getResource("/ShowBill.fxml"));;
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.showAndWait();
    }

    public void clickOrdersListView(MouseEvent mouseEvent) {
        modeBtn.setDisable(true);
        clickedOrder = (Order)ordersListView.getSelectionModel().getSelectedItem();
        selectedOrderLabel.setText(clickedOrder.toString());
        reOrderListView.setDisable(true);
        reasonListView.setDisable(true);
        if(clickedOrder.getIsDelivered()==true){
            if(clickedOrder.getIsRejected()==false){
                modeBtn.setDisable(false);
                reasonListView.getSelectionModel().selectFirst();
                reOrderListView.getSelectionModel().selectFirst();
            }

        }
    }


    public void setDefaultMode(){
        //TextField : id is available && setId available
        reasonListView.setItems(reasons);
        reOrderListView.setItems(reOrderBool);
        updateOberveOrders();



        //Boolean
        tableIsOccupied = false;
        serverIdSeted = false;
        isClickedOrder = false;


        //TextField input
        modificationQuantity.setEditable(false);
        serverIdTxt.clear();
        seatTxt.clear();
        seatTxt.setEditable(false);
        tablenumLabel.setText(tableNumber.toString());



        //ListView clcik
        menuListView.setDisable(true);
        modiListView.setDisable(true);
        reOrderListView.setDisable(true);
        reasonListView.setDisable(true);





        //Button click
        addModificationBtn.setDisable(true);
        addBtn.setDisable(true);
        printBillBtn.setDisable(true);
        seatSetBtn.setDisable(true);
        returnBtn.setDisable(true);
        modeBtn.setDisable(true);
        returnBtn.setDisable(true);



    }

    public void setGetBackMode(){
        //Button
        returnBtn.setDisable(true);
        modeBtn.setDisable(true);


        //TextInput
        tablesOrders = restaurant0520.getRestaurantManager().getTableManager().getTable(tableNumber).getOrders();
        reasonListView.setItems(reasons);
        reOrderListView.setItems(reOrderBool);
        tablenumLabel.setText(tableNumber.toString());
        tablenumLabel.setText(tableNumber.toString());
        seatTxt.setDisable(true);
        serverIdTxt.setDisable(true);
        selectedOrderLabel.clear();


        seatTxt.setText(tableSeats.toString());
        serverIdTxt.setText(serverId);
        updateOberveOrders();

        //listview
        reasonListView.setDisable(true);
        reOrderListView.setDisable(true);

    }
    public void passTableToBill(Integer integer){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ShowBill.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(TableOrderingController.class.getName()).log(Level.SEVERE,null,ex);
        }
        ShowBillController controller = loader.getController();
        controller.setTableNumber(tableNumber);
    }

    public void clickReturnBtn(ActionEvent event) throws IOException {
        Order selectedOrder = (Order)ordersListView.getSelectionModel().getSelectedItem();
        String reason = (String) reasonListView.getSelectionModel().getSelectedItem();
        Boolean bool=true;
        if(((String)reOrderListView.getSelectionModel().getSelectedItem()).equals("Not ReOrder")){
            bool =false;
        }
        server.returnOrderToKitchen(selectedOrder.getOrderId(),reason,bool);
        updateOberveOrders();
        ordersListView.getSelectionModel().clearSelection();
        modeBtn.setDisable(true);
        returnBtn.setDisable(true);
        selectedOrderLabel.setText("");
        reasonListView.setDisable(true);
        reOrderListView.setDisable(true);




    }

    public void clickSeatSetBtn(ActionEvent event) {
        //if serversetBtn not clicked

        tableSeats = Integer.valueOf(seatTxt.getText());
        tableIsOccupied = true;
        tableIsOccupied = true;
        seatSetBtn.setDisable(true);
        seatTxt.setDisable(true);
        menuListView.setDisable(false);
        server.setTableOccupied(tableNumber,tableSeats);

    }

    public void clickServerSetBtn(ActionEvent event) {
        serverId = serverIdTxt.getText().toString();
        server = (Server) restaurant0520.getEmployee("S:" + serverId);
        if (server != null) {
            serverIdSeted = true;
            serverSetBtn.setDisable(true);
            serverIdTxt.setDisable(true);
            seatSetBtn.setDisable(false);
            seatTxt.setEditable(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setHeaderText("InputError");
            alert.setContentText("Please input a valid serverID.");
            alert.showAndWait();
        }
    }

    public void clickReasonListView(MouseEvent mouseEvent) {
    }

    public void clickReOrederListView(MouseEvent mouseEvent) {
    }

    public void clickModeBtn(ActionEvent event) {
        Button btn = (Button)event.getSource();
        if(btn.getText().toString().equals("Off")) {
            modeBtn.setText("On");
            returnBtn.setDisable(false);

            reasonListView.setDisable(false);
            reOrderListView.setDisable(false);
        }
        else{
            modeBtn.setText("Off");
            returnBtn.setDisable(true);
            reasonListView.setDisable(true);
            reOrderListView.setDisable(true);
        }
    }


    public void clickcancelOrderBtn(ActionEvent event) {
        Order order = (Order)ordersListView.getSelectionModel().getSelectedItem();
        server.cancelOrder(order,tableNumber);
        updateOberveOrders();
    }

    public void clickAddSplitBtn(ActionEvent event) {
        Order currOrder = (Order)tableBillListView.getSelectionModel().getSelectedItem();

        spilitOrders.add(currOrder);
        billListView.setItems(currBillOrders);
        restaurant0520.getRestaurantManager().getTableManager().getTable(tableNumber).getBill().removeDish(currOrder);
        tableBillListView.getSelectionModel().selectFirst();

        ObservableList<Order> servedOrder = FXCollections.observableArrayList(restaurant0520.getRestaurantManager().getTableManager().getTable(tableNumber).getBill().getServedOrders());
        tableBillListView.setItems(servedOrder);
    }

    public void clickAddAllBtn(ActionEvent event) {
        server.setPaid(tableNumber);
    }


    public void clickPayBtn(ActionEvent event) {
        server.setPaid(tableNumber);
        setGetBackMode();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Table is Paid");
        alert.setContentText("Table#: "+restaurant0520.getRestaurantManager().getTableManager().getTable(tableNumber).toString());
        alert.showAndWait();
    }




    public void clickBillListView(MouseEvent mouseEvent) {
    }

    public void clickfinishTableBtn(ActionEvent event) {
        if(restaurant0520.getRestaurantManager().getTableManager().getTable(tableNumber).getBill().getServedOrders().size()!=0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not Paid Yet");
            alert.setHeaderText("Pay");
            alert.setContentText("Bill");
            alert.showAndWait();



        }
        else{server.finishTable(tableNumber);
            setDefaultMode();}


    }
}



