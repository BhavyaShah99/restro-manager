package model;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.*;

/**
 * A chef in the kitchen of the restaurant.
 */
public class Cook extends Receiver {

    /**
     * The inventory which stores all the ingredients.
     */
    private Inventory inventory;

    /**
     * The OrderManager who manages all the orders.
     */
    private OrderManager orderManager;
    private ArrayList<Order> jobs;

    private String name;
    private String EmployeeID;

    /**
     * Initializes this Cook.
     *
     * @param inventory the restaurant's inventory, which stores all the ingredients.
     * @param orderManager the OrderManager which stores and manages all the food orders.
     */
    public Cook(String name, String id, OrderManager orderManager, Inventory inventory) {
        super(name, id, inventory);
        this.inventory = inventory;
        this.name = name;
        this.EmployeeID = id;
        this.orderManager = orderManager;
        this.jobs = new ArrayList<>();
    }

    /**
     * Get an order that is first in line in OrderManager.
     */
    public void getJob ()
    {
        Order newOrder = orderManager.getFirstOrderInQueue();
        orderManager.getOrdersNotReady().remove(newOrder);
        if (newOrder!=null)
        {
            newOrder.setIsConfirmed();
            Order newJob = newOrder;
            jobs.add(newJob);
            if (!newJob.getIsRejected()) {
//                System.out.println("Chef " + name + " received a new job to cook [" + newJob.getDish().getName() + "]");
            } else { if(newJob.getRecook()) {
//                System.out.println("Chef " + name + " received a job to recook [" + newJob.getDish().getName() + "]");
            } else {
//                System.out.println("Chef " + name + " received a job to fix [" + newJob.getDish().getName() + "]");
            }
            }
        } else
        {
            System.out.println("NO NEW ORDERS");
        }
    }

    public void cookJob(int orderNum)
    {
        if (isOrderInJobs(orderNum)) {
            if (!findJob(orderNum).getBeganCooking()) {
                Order jobToCook = findJob(orderNum);
                if (jobToCook.getIsRejected()) {
                    System.out.println("Chef " + this.name + " fixing returned dish " + jobToCook.getDish().getName() + " | Complaint: " + jobToCook.getReasonForRejection());
                    if (jobToCook.getRecook()) {
                        jobToCook.setBeganCooking();
                        System.out.println("Chef " + this.name + " began cooking " + jobToCook.getDish().getName() + " | " + "orderID = " + orderNum);
                        inventory.removeSomeIngredients(jobToCook.getDish());
                        return;
                    }
                }
                jobToCook.setBeganCooking();
                System.out.println("Chef " + this.name + " began cooking " + jobToCook.getDish().getName() + " | " + "orderID = " + orderNum);
                inventory.removeSomeIngredients(jobToCook.getDish());
            } else {
                System.out.println("CHEF " + this.name + " IS ALREADY COOKING THIS ORDER");
            }
        } else {
            System.out.println("CHEF " + this.name + " IS NOT IN CHARGE OF THIS ORDER");
        }
    }

    private boolean isOrderInJobs(int orderNum)
    {
        boolean ret = false;
        for(Order job : jobs)
        {
            if (job.getOrderId() == orderNum)
            {
                ret = true;
            }
        }
        return ret;
    }

    public void listJobsToConsole()
    {
        if (jobs.size()!=0) {
            System.out.println("Chef " + this.name + "'s" + " jobs list");
        } else {
            System.out.println("Chef " + this.name +"'s jobs to cook is empty");
        }
        for(Order job : jobs)
        {
            System.out.println("Order ID = " +job.getOrderId()+"|"+"Dish Name = "+job.getDish().getName());
        }
    }


    /**
     * Marks that the Order with id orderId is ready for delivery.
     *
     * Precondition: The order with the id OrderId has not been prepared. There is enough ingredient
     * in the inventory to prepare the order with id orderId.
     *
     * @param orderNum the id of the Order that is ready for delivery.
     */
    public void fillOrder(int orderNum) {
        if (isOrderInJobs(orderNum)) {
            if (findJob(orderNum).getBeganCooking()) {
                Order order = findJob(orderNum);
                jobs.remove(order);
                System.out.println("Chef " + this.name + " has finished cooking " + order.getDish().getName());
                orderManager.orderPickupReady(orderNum);
            } else {
                System.out.println("Chef "+this.name+" | Order number ["+orderNum+"] has not began being cooked yet!");
            }
        } else {
            System.out.println("Chef "+this.name+" IS NOT IN CHARGE OF THIS ORDER");
        }

    }

    private Order findJob(int orderId)
    {
        Order ret = null;
        for (Order order : jobs)
        {
            if (order.getOrderId() == orderId)
            {
                ret = order;
                break;
            }
        }
        return ret;
    }

    public ArrayList<Order> getJobs() {
        return jobs;
    }
}
