package model;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The OrderManager creates, stores, and manages all the Orders.
 */
public class OrderManager extends Observable {

    /**
     * A Map of all the orders that the Cooks have to make or fix. Each key is an order id that
     * corresponds to an order that the order id represents.
     */
    private Map<Integer, Order> ordersNotReady;
    private Map<Integer, Order> ordersConfirmed;
    private Queue<Integer> orderIDCookPriority;
    private Map<Integer, Order> ordersReadyForPickup;

    private ArrayList<Server> serverObserverList = new ArrayList<>();

    /**
     * A Map of all the orders that are currently being delivered or are already delivered. Each key
     * is an order id that corresponds to an order that the order id represents.
     */
    private Map<Integer, Order> ordersDelivered;


    /**
     * Initializes this OrderManager.
     */
    public OrderManager() {
        orderIDCookPriority = new LinkedBlockingQueue<>();
        ordersNotReady = new HashMap<>();
        ordersDelivered = new HashMap<>();
        ordersReadyForPickup = new HashMap<>();
    }

    public void registerServer (Server serverObserver)
    {
        serverObserverList.add(serverObserver);
    }

    public void unRegisterServer (Server serverObserver)
    {
        serverObserverList.remove(serverObserver);
    }

    /**
     * Adds newOrder to a Map of orders that are waiting to be made.
     *
     * @param newOrder the order that is going to be added to a Map of orders that are waiting to be
     * made.
     */
    public void addNewOrder(Order newOrder) {
        ordersNotReady.put(newOrder.getOrderId(), newOrder);
        orderIDCookPriority.add(newOrder.getOrderId());
    }

    /**
     * Returns a Collection of all the orders that are not ready for delivery.
     *
     * @return a Collection of all the orders that are not ready for delivery.
     */
    public Order getUnreadyOrder(int orderId) {
        return ordersNotReady.get(orderId);
    }

    public Map getOrdersReadyForPickup ()
    {
        return ordersReadyForPickup;
    }

    /**
     * Returns the first order in line to be cooked.
     *
     * @return Returns the first order in line to be cooked.
     */
    public Order getFirstOrderInQueue() {
        Order ret = null;
        try {
            int id = orderIDCookPriority.remove();
            ret = ordersNotReady.get(id);
        } catch (java.util.NoSuchElementException E)
        {
            ret = null; //no new orders
        }
        return ret;
    }

    public void placeOrderInFirstPriority(int orderId)
    {
        int[] temp = new int[orderIDCookPriority.size()];

        for (int i = 0; i<temp.length; i++)
        {
            temp[i] = orderIDCookPriority.remove();
        }
        orderIDCookPriority.add(orderId);
        for(int i = 0; i<temp.length; i++)
        {
            orderIDCookPriority.add(temp[i]);
        }
    }

    /**
     * Marks that the order with id orderId is ready for delivery and puts order with orderId into ordersReadyForPickup, and notify the Servers.
     *
     * Precondition: There is an Order with id orderId that has not been prepared.
     *
     * @param orderId the id of the order that is ready for delivery.
     */
    public void orderPickupReady(int orderId) {
        if (ordersNotReady.containsKey(orderId)) {
            ordersReadyForPickup.put(orderId, ordersNotReady.get(orderId));
            ordersNotReady.remove(orderId);
            notifyObservers();
        }
    }

    public void notifyObservers()
    {
        System.out.println("All servers 'orders to pick up' list has been updated");
        for (Server server : serverObserverList)
        {
            server.notifyMe();
        }
    }

    //Return a list of orders which are ready for pickup. Later use this to display list of orders ready for pickup on GUI.
    /*
    public Order getReadyOrder() {
        if (!ordersReady.isEmpty()) {
            Order order = ordersReady.poll();
            orderDelivered.put(order.getOrderId(), order);
            return order;
        }
        return null;
    }
    */

    /**
     * Returns the delivered order with id OrderId.
     *
     * @param orderId the id of a delivered order.
     * @return the delivered order with id orderId and null if there no order with id orderId.
     */
    public Order getDeliveredOrder(int orderId) {
        return ordersDelivered.get(orderId);
    }

    public Order getOrderReadyForPickup(int orderId)
    {
        return ordersReadyForPickup.get(orderId);
    }

    /**
     * Moves the order from the Map of all the delivered orders to the Map of orders that the cook
     * needs to fix.
     *
     * Precondition: There is an order with id orderId that is delivered or is currently being
     * delivered.
     *
     * @param orderId the order with id orderId that needs to be fixed.
     */
    public void deliveredToUnready(int orderId) {
        if (ordersDelivered.get(orderId) != null) {
            ordersNotReady.put(orderId, ordersDelivered.get(orderId));
            ordersDelivered.remove(orderId);
        }
    }

    public void readyForPickupToDelivered (int orderId)
    {
        if (ordersReadyForPickup.get(orderId) != null) {
            ordersDelivered.put(orderId, ordersReadyForPickup.get(orderId));
            ordersReadyForPickup.remove(orderId);
        }
    }

    public boolean orderIsInReadyForPickup(int orderID)
    {
        boolean ret = false;
        if (ordersReadyForPickup.size() != 0) {
            for (Map.Entry<Integer, Order> entry : ordersReadyForPickup.entrySet()) {
                if (orderID == entry.getValue().getOrderId()) {
                    ret = true;
                }
            }
        } else {
            System.out.println("There are no orders that are ready for pickup");
        }
        return ret;
    }

    public void printOrdersNotReadyToConsole()
    {
        if (ordersNotReady.size() != 0) {
            for (Map.Entry<Integer, Order> entry : ordersNotReady.entrySet()) {
                System.out.println("Order number: " + entry.getKey() + "| Order: " + entry.getValue().getDish());
            }
        } else
        {
            System.out.println("No orders to cook");
        }
    }

    public void printOrdersReadyForPickup()
    {
        if (ordersReadyForPickup.size() != 0) {
            for (Map.Entry<Integer, Order> entry : ordersReadyForPickup.entrySet()) {
                System.out.println("Order number: " + entry.getKey() + "| Order: " + entry.getValue().getDish());
            }
        } else
        {
            System.out.println("No orders to be picked up");
        }
    }

    public Map<Integer, Order> getOrdersNotReady() {
        return ordersNotReady;
    }


}