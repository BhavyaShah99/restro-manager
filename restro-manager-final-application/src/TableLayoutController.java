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
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableLayoutController implements Initializable {
    @FXML private Button backBtn;
    @FXML private Button managerbtn;
    @FXML private Button table1Btn,table2Btn,table3Btn,table4Btn,table5Btn,table6Btn,table7Btn,table8Btn;
    @FXML ListView servingListView;
    @FXML TextField serverIdTxt;
    @FXML Button serveBtn,serverIdSet;


    static Restaurant restaurant0520;
    static ObservableList<Order> toDeliverList = FXCollections.observableArrayList();
    Server server;
    String serverId;
    boolean serverIsSetted = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            servingListView.setDisable(true);
            serveBtn.setDisable(true);
            updateDeliverList();

            backBtn.setOnAction(new EventHandler<ActionEvent>() {
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
                    }
                }
            });

            managerbtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Parent managerViewParent = FXMLLoader.load(getClass().getResource("Manager.fxml"));
                        Scene managerScene = new Scene(managerViewParent);
                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        window.setTitle("Manager");
                        window.setScene(managerScene);
                        window.show();
                    } catch (IOException ex) {
                    }
                }
            });
        }catch (NullPointerException ex) {
        }
    }

    public void clickConfirmBtn(ActionEvent actionEvent) {
        //Click button
        //whatToServeLab changed to the first Dish on the What to serve list
        //WhatTOServeList first one is removed
        //WhatToServeList set String .
    }

    public void clickTables(ActionEvent actionEvent) throws IOException {
        Button btn = (Button) actionEvent.getSource();
        String str = btn.getText();
        Integer tablenum = Integer.valueOf(str);

        passTableToOrdering(tablenum);


        Parent managerViewParent = FXMLLoader.load(getClass().getResource("/TableOrdering.fxml"));
        Scene managerScene = new Scene(managerViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Server's Ordering Screen");
        window.setScene(managerScene);
        window.show();
    }

    public void setRestaurant0520(Restaurant restaurant) {
        this.restaurant0520 = restaurant;
    }

    public void passTableToOrdering(Integer integer){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/TableOrdering.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(TableLayoutController.class.getName()).log(Level.SEVERE,null,ex);
        }
        TableOrderingController orderingController = loader.getController();
        orderingController.setTableNumber(integer);
    }


    //***************************************ServingTab*********************************************************************
    public void clickServerListView(MouseEvent mouseEvent) {
    }

    public void ClickServerIdSet(ActionEvent event) {
        serverId = serverIdTxt.getText().toString();
        server = (Server)restaurant0520.getEmployee("S:"+serverId);
        if (server != null) {
            serverIdTxt.setDisable(true);
            serverIdSet.setDisable(true);
            servingListView.setDisable(false);
            serveBtn.setDisable(false);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setHeaderText("Input Error");
            alert.setContentText("Please input a valid serverID.");
            alert.showAndWait();
        }
    }

    public void clickServeBtn(ActionEvent event) {
        try {
            Order toDeliver = (Order) servingListView.getSelectionModel().getSelectedItem();
            server.deliverOrder(toDeliver.getOrderId());
            updateDeliverList();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setHeaderText("Input Error");
            alert.setContentText("Please choose a Order to deliver.");
            alert.showAndWait();
        }
    }

    public void setDefaultMode(){

    }
    
    public void updateDeliverList(){
        toDeliverList.clear();
        Map<Integer,Order> orders = restaurant0520.getRestaurantManager().getOrderManager().getOrdersReadyForPickup();
        for(Map.Entry<Integer,Order> item:orders.entrySet()){
            toDeliverList.add(item.getValue());
        }
        servingListView.setItems(toDeliverList);

    }
}
