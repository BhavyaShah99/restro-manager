package model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.io.*;

public class RestaurantManager {


    private String name;
    private Inventory inventory;
    private OrderManager orderManager;
    private TableManager tableManager;
    private Menu menu;

    private final String menuName = "src/Menu.ser";
    //private final String menuName = "/Users/saeon/Desktop/STA258/group_0520/phase1/src/Menu.ser";


    public RestaurantManager(String restaurantName) throws IOException {
        inventory = new Inventory(restaurantName);
        this.orderManager = new OrderManager();
        this.tableManager = new TableManager();
        //Object m = readBinaryDataFileMenu();
        //this.menu = (m!=null) ? (Menu) m : new Menu();
        //writeBinaryDataFileMenu();
        this.menu = new Menu(inventory, restaurantName);
    }

    public Server createServer(String name, int id)
    {
        Server server = new Server(name, "S:"+id, inventory, menu, tableManager, orderManager);
        orderManager.registerServer(server);
        return server;
    }

    public Cook createCook(String name, int id)
    {
        return new Cook(name, "C:"+id, orderManager,inventory);
    }

    public Manager createManager(String name, int id)
    {
        return new Manager(name, "M:"+id, inventory);
    }

    public OrderManager getOrderManager()
    {
        return orderManager;
    }

    public TableManager getTableManager() {
        return this.tableManager;
    }

    public Menu getMenu()
    {
        return this.menu;
    }

    public ArrayList<Dish> getMenuList ()
    {
        return this.getMenu().getMenu();
    }

    public Inventory getInventory()
    {
        return this.inventory;
    }

    public String getName()
    {
        return this.name;
    }

    public void writeBinaryDataFileMenu()
    {
        File fOut = new File(menuName);
        try {
            FileOutputStream fileOut = new FileOutputStream(fOut);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(menu);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in phase1/src/Menu.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readBinaryDataFileMenu()
    {
        Menu ret = null;
        File fIn = new File(menuName);
        try {
            FileInputStream fileIn = new FileInputStream(fIn);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ret = (Menu) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Successfully read Menu.ser");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Menu class not found");
            c.printStackTrace();
        }
        return ret;
    }

    @Override
    public String toString() {
        return "restaurant Manager";
    }
}
