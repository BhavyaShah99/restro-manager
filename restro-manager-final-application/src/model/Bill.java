package model;
import javafx.scene.control.Alert;

import java.util.ArrayList;

/**
 * The Bill assigned to each table. Bill contains a list of dishes and is responsible for printing this bill.
 */
public class Bill {

    private ArrayList<Order> servedOrders;

    private double total;
    private double tax;
    private double subTotal;
    private double tipAmount;
    private boolean tip;

    /**
     * Create new Bill with empty ArrayList of Dishes and a running total of 0.
     */
    public Bill() {
        this.servedOrders = new ArrayList<>();
        this.subTotal = 0;
        if(this.getServedOrders().size()>0){
            for(Order o:this.servedOrders){
                subTotal += o.getDish().getPrice();
            }
        }

        this.tax = subTotal*0.13;
        this.total = subTotal + this.tax;
        this.tip = false;

    }

    /**
     * Adds the
     * @param order Add this dish to Bill.
     */
    public void addOrder(Order order) {
        servedOrders.add(order);
        subTotal += order.getDish().getPrice();
        updateBill();

    }

    public void setTip(boolean tip) {
        this.tip = tip;
    }

    public void updateBill() {
        this.tax = subTotal*0.13;
        this.total = subTotal + this.tax;
    }

    /**
     *
     * @param order Remove this dish from the bill.
     */
    public void removeDish(Order order) {
        for (Order o : this.servedOrders) {
            if (o.getOrderId() == o.getOrderId()) {
                servedOrders.remove(o);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Removal From Bill Confirmed");
                alert.showAndWait();

            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Cant remove");
                alert.setContentText("Fail");
                alert.showAndWait();

            }
        }
    }

    /**
     *
     * @return Returns the total cost of all dishes served including tax.
     */
    public double getTotal(){return this.total ;}

    /**
     * Prints this bill to Console.
     */
    public void showBill() {
        System.out.println(this.toString());
    }

    /**
     *
     * @return Returns this bill as a string.
     */
    @Override
    public String toString() {
        String bill = "=================BILL===================================\n";
        for (Order order : servedOrders) {
            bill += ("\t$" + order.getDish().getPrice() +"\t"+ order.getDish().getName()  + "\n");
        }
        bill += "\n\n\n";
        bill += "\t subTotal : $" + subTotal +"\n";
        bill += "\t HST/GST : $" + tax + "\n";
        if (tip) {
            bill += "\t 15% Tip: $" + tipAmount + "\n";
        }
        bill += "\t total : $" + total + "\n";

        return bill + "\n======================================================";
    }

    /**
     * Sets servedDishes to null and total costs to 0.
     */
    public void resetBill() {
        this.servedOrders = null;
        this.subTotal = 0;
        setTip(false);
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getTax() {
        return tax;
    }

    public ArrayList<Order> getServedOrders() {
        return servedOrders;
    }
}
