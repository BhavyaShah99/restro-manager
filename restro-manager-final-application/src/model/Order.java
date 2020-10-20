package model;
import java.util.*;
import java.util.Map.Entry;

/**
 * An order that is placed by the customer.
 */
public class Order {

    /**
     * The food item being ordered
     */
    private Dish dish;

    /**
     * Order id to uniquely identify all orders
     */
    private int orderId;
    /**
     * Table where the order is to be served
     */
    private Table table;
    /**
     *
     */
    private boolean beganCooking;
    /**
     * If the order is confirmed by the cook
     */
    private boolean isConfirmed;
    /**
     * Variable to store weather the order is rejected by customer.
     */
    private boolean isRejected;
    /**
     * Sets order to delivered
     */
    private boolean isDelivered;
    /**
     * Variable to store the reason for the rejection and the thing needs to be fixed
     */
    private String reasonForRejection;
    String msg;

    private boolean recook;
    /**
     * The order number that increments with each order made
     */
    private static int numOrder = 0;

    private Map<String, Double> modifications;


    /**
     * Initializes an Order
     * @param table the table the order is associated to
     */
    public Order(Table table, Dish dish)
    {
        this.dish = dish;
        this.orderId = ++numOrder;
        this.table = table;
        this.isConfirmed = false;
        this.isRejected = false;
        this.isDelivered = false;
        this.beganCooking = false;
        this.recook = false;
        this.msg="";
    }

    public Order(Table table, Dish dish,Map<String, Double> modification)
    {
        this.dish = dish;
        this.modifications = modifications;
        this.orderId = ++numOrder;
        this.table = table;
        this.isConfirmed = false;
        this.isRejected = false;
        this.isDelivered = false;
        this.beganCooking = false;
        this.recook = false;
        this.msg ="";
    }

    /**
     * Sets order to delivered.
     */
    public void setToDelivered()
    {
        isDelivered = true;
    }

    public Map<String, Double> getModifications()
    {
        return this.modifications;
    }

    /**
     * Gets if order is delivered.
     * @return boolean to check if order is delivered.
     */
    public boolean getIfDelivered()
    {
        return isDelivered;
    }

    /**
     * Gets the unique id of each order
     * @return returns the order id.
     */
    public int getOrderId()
    {
        return orderId;
    }

    /**
     * Gets the table number where the order must be served
     * @return the table at which this order was placed.
     */
    public int getTableNumber()
    {
        return table.getTableNumber();
    }

    /**
     * Gets the object of the food that is ordered
     * @return the food that was ordered.
     */
    public Dish getDish()
    {
        return dish;
    }

    /**
     * Uses the food item being ordered to get the orders price. This method can be used while
     * generating bill.
     * @return the name and price of the food ordered
     */
    public double getOrderPrice()
    {
        return dish.getPrice();
    }

    /**
     * @return If the order is confirmed
     */
    public Boolean getOrderIsConfirmed()
    {
        return isConfirmed;
    }

    /**
     * Sets the order to confirmed by the cook
     */
    public void setIsConfirmed()
    {
        isConfirmed = true;
    }

    public void setBeganCooking()
    {
        beganCooking = true;
    }

    public boolean getBeganCooking()
    {
        return beganCooking;
    }

    /**
     * Sets the order to rejected when customer is not satisfied
     */
    public void setToRejected()
    {
        isRejected = true;
    }

    /**
     * Gets the rejection status of the order
     * @return if the order was rejected by the customer.
     */
    public Boolean getIsRejected()
    {
        return isRejected;
    }

    public Boolean getIsConfirmed() { return isConfirmed; }

    public void setIsConfimed() { this.isConfirmed = true; }

    /**
     * Sets order to be fixed by cook
     */
    public void setToFixed()
    {
        isRejected = false;
    }

    /**
     * Sets the reason for rejection and wat needs to be fixed;
     * @param reason reason for the order being rejected.
     */
    public void setReasonForRejection(String reason)
    {
        reasonForRejection = reason;
    }

    public void setRecook()
    {
        this.recook = true;
    }

    public boolean getRecook()
    {
        return this.recook;
    }

    /**
     * Gets the reason order was rejected.
     * @return the reason for rejecting the order.
     */
    public String getReasonForRejection()
    {
        return reasonForRejection;
    }

    /**
     * Returns a String representation of all the additions of and the subtractions of ingredients.
     * @return a String representation of all the additions of and the subtractions of ingredients.
     */
    private String toStringModifications(){
        return "ToStringModification() in Order.java";
    }

    /**
     * Returns a String representation of an Order.
     * @return a String representation of an Order.
     */
    @Override
    public String toString()
    {
        String toReturn = "(Od."+this.orderId+")"+dish.toString();
        if(this.dish.isModified()==true){
            toReturn = toReturn +"   Modified:  " + dish.getModifiedIngredients();
        }
        if(this.isDelivered){
            toReturn += "  (Serv)  ";
        }
        if(this.getIsRejected()==true){
            toReturn += "  (Rej)  ";
        }


        return toReturn + "   Table:  " + getTableNumber();

        /*"( "+this.table.toString()+") "+ this.orderId + dish.toString();*/
    }

    /**
     * Should be called after closing time of the restaurant.
     */
    public void resetOrderCounte()
    {
        this.numOrder = 0;
    }

    public boolean getIsDelivered(){
        return isDelivered;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}


