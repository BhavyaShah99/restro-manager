package model;


import java.io.IOException;

/**
 * A person who scans each item or a list of items back into the inventory when a new shipment of
 * ingredients arrive.
 */
public abstract class Receiver {

    /**
     * The inventory of the restaurant that this Receiver is in.
     */
    private Inventory inventory;


    /**
     * The id of this Receiver.
     */
    private String employeeId;

    private String name;


    /**
     * Initializes this Receiver.
     *
     * @param inventory the inventory of the restaurant that this Receiver is in.
     */
    public Receiver(String name, String id, Inventory inventory)
    {
        this.inventory = inventory;
        this.name = name;
        this.employeeId = id;
    }

    /**
     * Changed
     *
     */
    public void scan(String name, double amount, String unit) {
        Ingredient i = new Ingredient(name, amount, unit);
        inventory.addIngredient(i);
    }

    public String getName()
    {
        return name;
    }

    /**
     * Returns the id of the Employee.
     * @return the id of the Employee.
     */
    public String getEmployeeId() {
        return employeeId;
    }
}
