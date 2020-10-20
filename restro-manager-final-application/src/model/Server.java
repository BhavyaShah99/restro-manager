package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.scene.control.Alert;


/**
 * A waiter/server in the restaurant who takes and serves orders/food to customers.
 */
public class Server extends Receiver implements Observer {

    /**
     * Used by cook to access FoodManager
     */
    private Menu menu;
    /**
     * A table manager in server
     */
    private TableManager tableManager;

    /**
     * An instance of the order manager in this case the server.
     */
    private OrderManager orderManager;

    private Inventory inventory;

    private String name;

    private String EmployeeID;

    private Map<Integer, Order> ordersForPickup;

    private ArrayList<String> history;

    /**
     * Initializes the server
     * @param ServerManagingOrders the OrderManager which stores and manages all the food orders.
     * @param inventory the restaurant's inventory, which stores all the ingredients.
     * @param tableManager and instance of the table manager
     */
    public Server(String name, String id, Inventory inventory, Menu menu, TableManager tableManager,
                  OrderManager ServerManagingOrders)
    {
        super(name, id, inventory);
        this.name = name;
        this.EmployeeID = id;
        this.inventory = inventory;
        this.orderManager = ServerManagingOrders;
        this.menu = menu;
        this.tableManager = tableManager;
        this.ordersForPickup = orderManager.getOrdersReadyForPickup();
        this.history = new ArrayList<>();
    }

    /**
     * Updates the server that the order is ready to be delivered.
     * @param o
     */
    @Override
    public void update(java.util.Observable o, Object arg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Server's Update");
        alert.setHeaderText("Update");
        alert.setContentText("A new order is ready to be served");
        alert.showAndWait();
        //System.out.println("A new order is ready to be served");
    }

    /**
     * Creates an Order and adds it to the new order map in OrderManager.
     *
     * @param tableNum the table number, at which the people who made this order sit.
     * @param dishName the name of the dish that was ordered.
     * @return the newly created Order.
     */
    public boolean createOrder(int tableNum, String dishName, Map<String, Double> modifications) {
        if (tableManager.getTable(tableNum).isOccupied()) {
            if (!menu.isInMenu(dishName)) {
                addToHistory("Failed to create order (not a valid dish) : Dish input = '"+dishName +"', Table "+tableNum);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Server's Update");
                alert.setHeaderText("Please recheck Order Details");
                alert.setContentText("["+dishName+"] IS NOT A VALID DISH");
                alert.showAndWait();
                //System.out.println("NOT A VALID DISH");
                return false;
            }
            if (!tableManager.isInTables(tableNum)) {
                addToHistory("Failed to create order (not a valid table) : TableInput = '"+tableNum);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Server's Update");
                alert.setHeaderText("Please recheck Order Details");
                alert.setContentText("["+tableNum+"] IS NOT A VALID TABLE NUMBER");
                alert.showAndWait();
                //System.out.println("NOT A VALID TABLE NUMBER");
                return false;
            }
            Dish dish = menu.getDish(dishName);
            Map<Ingredient,Double> recipe = dish.getDishRecipe();
            Map<Ingredient,Double> newRecipe = new HashMap<>();
            newRecipe.putAll(recipe);
            for(Map.Entry<String,Double> modiItem :modifications.entrySet()){
                newRecipe.put(inventory.getIngredient(modiItem.getKey()),modiItem.getValue());
            }
            Dish newDish = new Dish(dish.getName(),dish.getPrice(),newRecipe);
            if (inventory.isEnough(newDish)) {

                newDish.setModified(true);
                for(Map.Entry<String,Double> modiItem :modifications.entrySet()){
                    newDish.addModifiedIngredients(inventory.getIngredient(modiItem.getKey()),modiItem.getValue());
                }

                Order order = new Order(tableManager.getTable(tableNum), newDish);

                String orderMadeConfirmation = this.name + " you have successfully Added an Order for table " + tableNum + "\n. DISH: " + menu.getDish(dishName).getName()+"Modi:"+newDish.getModifiedIngredients();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Server's Update");
                alert.setHeaderText("");
                alert.setContentText(orderMadeConfirmation);
                alert.showAndWait();
                //System.out.println(this.name + ": has successfully made an Order for table " + tableNum + ". DISH: " + menu.getDish(dishName).getName());
                orderManager.addNewOrder(order);
                //System.out.println(this.name + ": Added the order " + "[ table number: " + tableNum + ": Dish " + menu.getDish(dishName).getName() + "]" + "to orderManager.");
                tableManager.getTable(tableNum).setOrdered();

                //Testing
                tableManager.getTable(tableNum).getOrders().add(order);

                addToHistory("Successfully created an order with modifications: OrderID = "+order.getOrderId()+", Dish '"+order.getDish().getName() +"', Table "+order.getTableNumber());
                return true;
            }
        } else {
            addToHistory("Failed to create order (Table is not occupied) : Table = '"+tableNum);
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Server's Update");
            alert1.setHeaderText("Please recheck Order Details");
            alert1.setContentText("Table ["+tableNum+"] is not occupied. Orders can only be created for occupied tables");
            alert1.showAndWait();
            return false;
            //System.out.println("Table ["+tableNum+"] is not occupied. Orderes can only be created for occupied tables");
        }
        return false;
    }

    //Method overload. Order without modifications.
    //when success to CreateOrder it returns true
    //when fail to createOrder it returns false
    public Boolean createOrder(int tableNum, String dishName) {
        if (tableManager.getTable(tableNum).isOccupied()) {
            if (!menu.isInMenu(dishName)) {
                addToHistory("Failed to create order (not a valid dish) : Dish input = '"+dishName +"', Table "+tableNum);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Server's Update");
                alert.setHeaderText("Please recheck Order Details");
                alert.setContentText("["+dishName+"] IS NOT A VALID DISH");
                alert.showAndWait();
                //System.out.println("NOT A VALID DISH");
                return false;
            }
            if (!tableManager.isInTables(tableNum)) {
                addToHistory("Failed to create order (not a valid table) : TableInput = '"+tableNum);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Server's Update");
                alert.setHeaderText("Please recheck Order Details");
                alert.setContentText("["+tableNum+"] IS NOT A VALID TABLE NUMBER");
                alert.showAndWait();
                //System.out.println("NOT A VALID TABLE NUMBER");
                return false;
            }
            Dish dish = menu.getDish(dishName);
            if (inventory.isEnough(dish)) {
                Order order = new Order(tableManager.getTable(tableNum), menu.getDish(dishName));
                String orderMadeConfirmation = this.name + " you have successfully Added an Order for table " + tableNum + "\n\n DISH: " + menu.getDish(dishName).getName();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Server's Update");
                alert.setHeaderText("");
                alert.setContentText(orderMadeConfirmation);
                alert.showAndWait();
                //System.out.println(this.name + ": has successfully made an Order for table " + tableNum + ". DISH: " + menu.getDish(dishName).getName());
                orderManager.addNewOrder(order);
                //System.out.println(this.name + ": Added the order " + "[ table number: " + tableNum + ": Dish " + menu.getDish(dishName).getName() + "]" + "to orderManager.");
                tableManager.getTable(tableNum).setOrdered();
                tableManager.getTable(tableNum).getOrders().add(order);
                addToHistory("Successfully created an order : OrderID = "+order.getOrderId()+", Dish '"+order.getDish().getName() +"', Table "+order.getTableNumber());
                return true;
            }
        } else {
            addToHistory("Failed to create order (Table is not occupied) : Table = '"+tableNum);
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Server's Update");
            alert1.setHeaderText("Please recheck Order Details");
            alert1.setContentText("Table ["+tableNum+"] is not occupied. Orders can only be created for occupied tables");
            alert1.showAndWait();
            //System.out.println("Table ["+tableNum+"] is not occupied. Orderes can only be created for occupied tables");
            return false;
        }
        return false;
    }
    public Boolean createOrder(int tableNum, String dishName,String str) {
        if (tableManager.getTable(tableNum).isOccupied()) {
            if (!menu.isInMenu(dishName)) {
                addToHistory("Failed to create order (not a valid dish) : Dish input = '"+dishName +"', Table "+tableNum);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Server's Update");
                alert.setHeaderText("Please recheck Order Details");
                alert.setContentText("["+dishName+"] IS NOT A VALID DISH");
                alert.showAndWait();
                //System.out.println("NOT A VALID DISH");
                return false;
            }
            if (!tableManager.isInTables(tableNum)) {
                addToHistory("Failed to create order (not a valid table) : TableInput = '"+tableNum);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Server's Update");
                alert.setHeaderText("Please recheck Order Details");
                alert.setContentText("["+tableNum+"] IS NOT A VALID TABLE NUMBER");
                alert.showAndWait();
                //System.out.println("NOT A VALID TABLE NUMBER");
                return false;
            }
            Dish dish = menu.getDish(dishName);
            dish.setMsg(str);

            if (inventory.isEnough(dish)) {
                Order order = new Order(tableManager.getTable(tableNum),dish);
                order.setMsg(str);
                String orderMadeConfirmation = this.name + " you have successfully Added an Order for table " + tableNum + "\n\n DISH: " + menu.getDish(dishName).getName()+" Msg: "+dish.getMsg();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Server's Update");
                alert.setHeaderText("");
                alert.setContentText(orderMadeConfirmation);
                alert.showAndWait();
                //System.out.println(this.name + ": has successfully made an Order for table " + tableNum + ". DISH: " + menu.getDish(dishName).getName());
                orderManager.addNewOrder(order);
                //System.out.println(this.name + ": Added the order " + "[ table number: " + tableNum + ": Dish " + menu.getDish(dishName).getName() + "]" + "to orderManager.");
                tableManager.getTable(tableNum).setOrdered();
                tableManager.getTable(tableNum).getOrders().add(order);
                addToHistory("Successfully created an order : OrderID = "+order.getOrderId()+", Dish '"+order.getDish().getName() +"', Table "+order.getTableNumber());
                return true;
            }
        } else {
            addToHistory("Failed to create order (Table is not occupied) : Table = '"+tableNum);
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Server's Update");
            alert1.setHeaderText("Please recheck Order Details");
            alert1.setContentText("Table ["+tableNum+"] is not occupied. Orders can only be created for occupied tables");
            alert1.showAndWait();
            //System.out.println("Table ["+tableNum+"] is not occupied. Orderes can only be created for occupied tables");
            return false;
        }
        return false;
    }

    public void returnOrderToKitchen(int orderId, String reason, boolean recook)
    {
        Order returningOrder = orderManager.getDeliveredOrder(orderId);
        orderManager.getDeliveredOrder(orderId);
        returningOrder.setToRejected();
        returningOrder.setReasonForRejection(reason);
        if (recook) {
            returningOrder.setRecook();
            Dish returnDish = returningOrder.getDish();
            this.createOrder(returningOrder.getTableNumber(),returnDish.getName(),reason);
        }
        (tableManager.getTable(returningOrder.getTableNumber())).getBill().removeDish(returningOrder);

        addToHistory("Returned order to kitchen : OrderID = "+orderId+", Dish '"+returningOrder.getDish().getName() +"', Table "+returningOrder.getTableNumber() +", with reason '"+reason+"', ReCook = "+recook);
    }

    public void setTableOccupied(int tableNum)
    {
        if (!tableManager.getTable(tableNum).isOrdered() && !tableManager.getTable(tableNum).isOccupied() && !tableManager.getTable(tableNum).isPaid()) {
            tableManager.getTable(tableNum).setOccupied();
            addToHistory("Set table status to occupied : Table "+tableNum);
        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Server's Update");
            alert1.setHeaderText("Please recheck table number");
            alert1.setContentText("Failed to set table ["+tableNum+"] as occupied. Table may already be occupied");
            alert1.showAndWait();
            //System.out.println("Failed to set table ["+tableNum+"] as occupied");
        }
    }

    public void setTableOccupied(int tableNum,Integer seatsNum)
    {
        if (!tableManager.getTable(tableNum).isOrdered() && !tableManager.getTable(tableNum).isOccupied() && !tableManager.getTable(tableNum).isPaid()) {
            tableManager.getTable(tableNum).setOccupied();
            tableManager.getTable(tableNum).setSeats(seatsNum);
            addToHistory("Set table status to occupied : Table "+tableNum);
        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Server's Update");
            alert1.setHeaderText("Please recheck table details");
            alert1.setContentText("Failed to set table ["+tableNum+"] as occupied. Table may already be occupied");
            alert1.showAndWait();
        }
    }

    public void setPaid(int tableNum) {
        double total = tableManager.getTable(tableNum).getBill().getTotal();
        ArrayList<Order> currOrders = tableManager.getTable(tableNum).getBill().getServedOrders();
        String str ="";
        for(Order order :currOrders){
            str += order.getDish().getName() +" " + order.getDish().getPrice()+" ";
        }
        str +="\n"+total;
        /*tableManager.getTable(tableNum).payCurrentBill();*/
        tableManager.getTable(tableNum).payAllBill();
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setHeaderText("YOu Paid"  + str);
        alert1.showAndWait();
        /*if (tableManager.getTable(tableNum).isOrdered() && tableManager.getTable(tableNum).isOccupied()) {
            tableManager.getTable(tableNum).setPaid();
            tableManager.getTable(tableNum).resetTable();
            int id = tableManager.getTable(tableNum).getOrders().get(0).getOrderId();
            String ordersInTable = " with orderID's {" + Integer.toString(id);
            for (int i=1; i<tableManager.getTable(tableNum).getOrders().size(); i++)
            {
                id = tableManager.getTable(tableNum).getOrders().get(i).getOrderId();
                ordersInTable = ordersInTable + ", "+id;
            }
            ordersInTable = ordersInTable + "}";
            addToHistory("Set table status to paid : Table "+tableNum + ordersInTable);
        } else {
            int id = tableManager.getTable(tableNum).getOrders().get(0).getOrderId();
            String ordersInTable = " with orderID's {" + Integer.toString(id);
            for (int i=1; i<tableManager.getTable(tableNum).getOrders().size(); i++)
            {
                id = tableManager.getTable(tableNum).getOrders().get(i).getOrderId();
                ordersInTable = ordersInTable + ", "+id;
            }
            ordersInTable = ordersInTable + "}";
            addToHistory("Failed to set table status to paid : Table "+tableNum + ordersInTable);
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Server's Update");
            alert1.setHeaderText("Please recheck table details");
            alert1.setContentText("Failed to set table ["+tableNum+"] as paid. Either table hasn't ordered yet or its not occupied");
            alert1.showAndWait();
            //System.out.println("Failed to set table ["+tableNum+"] as paid. Either table hasn't ordered yet or its not occupied");
        }*/
    }

    public void tableFinished(int tableNum)
    {
        if (tableManager.getTable(tableNum).isPaid()) {
            tableManager.getTable(tableNum).resetTable();
            int id = tableManager.getTable(tableNum).getOrders().get(0).getOrderId();
            String ordersInTable = " with orderID's {" + Integer.toString(id);
            for (int i=1; i<tableManager.getTable(tableNum).getOrders().size(); i++)
            {
                id = tableManager.getTable(tableNum).getOrders().get(i).getOrderId();
                ordersInTable = ordersInTable + ", "+id;
            }
            ordersInTable = ordersInTable + "}";
            addToHistory("Checked out table (table finished): Table "+tableNum + ordersInTable);
        } else {
            addToHistory("Failed to check out table (table has not paid yet): Table "+tableNum);
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Server's Update");
            alert1.setHeaderText("Please recheck table details");
            alert1.setContentText("Failed to declare the table ["+tableNum+"] as finished. Table has not paid yet");
            alert1.showAndWait();
            //System.out.println("Failed to declare the table ["+tableNum+"] as finished. Table has not paid yet");
        }
    }

    public void notifyMe()
    {
        ordersForPickup = orderManager.getOrdersReadyForPickup();
    }

    public void printListOfOrdersForPickUpToConsole()
    {
        System.out.println("[Server "+this.name+"] Orders to be picked up");
        for (Map.Entry<Integer, Order> entry : ordersForPickup.entrySet())
        {
            int orderId = entry.getKey();
            Order order = entry.getValue();
            System.out.println("ID: "+orderId + "| Dish: "+ order.getDish());
        }
    }

    /**
     * Allows the server to print the bill
     * @param tableNumber the table number of whose bill needs to be printed.
     */
    public void printBill(int tableNumber)
    {
        tableManager.getTable(tableNumber).getBill().showBill();
    }

    /**
     * Server delivers the order and shifts from ready orders to delivered orders.
     * @param orderID the Id of the order that was delivered
     */
    public void deliverOrder(int orderID)
    {
        if (orderManager.orderIsInReadyForPickup(orderID)) {
            Order order = orderManager.getOrderReadyForPickup(orderID);
            orderManager.readyForPickupToDelivered(orderID);
            order.setToDelivered();
            int tableNumOfOrder = order.getTableNumber();
            tableManager.getTable(tableNumOfOrder).getBill().addOrder(order);
            addToHistory("Delivered order to table : OrderID = "+orderID+", Dish '"+order.getDish().getName() +"', Table "+tableNumOfOrder);
        }
    }

    private void addToHistory(String details)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String formatStr = "%-10s %-15s%n";
        String log;
        log = String.format(formatStr, dateFormat.format(date), details);
        history.add(log);
    }

    public ArrayList getHistory()
    {
        return this.history;
    }


    public String printBillFx(int tableNumber)
    {
        return tableManager.getTable(tableNumber).getBill().toString();
    }

    public void finishTable(int tableNum){
        tableManager.getTable(tableNum).resetTable();
    }

    public void cancelOrder(Order order, int tableNum){
        int orderId = order.getOrderId();
        if(orderManager.getOrdersNotReady().containsKey(orderId) && order.getBeganCooking() ==false){
            orderManager.getOrdersNotReady().remove(orderId);
            tableManager.getTable(tableNum).getOrders().remove(order);
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setTitle("Canceled the Order");
            alert1.setContentText("Success");
            alert1.showAndWait();
        }
        else{
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Fail to Cancel the Order");
            alert1.setContentText("the order is in the progress");
            alert1.showAndWait();
        }
    }


}
