package model;
import javafx.scene.control.Alert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.*;
import java.util.*;

/*
Restaurant owns Menu
Server can can create Dish or delete Dish from Menu.
Server can Order dish from Menu , and able to see the possible Menu.
(If out of the Ingredient) kitchen cant serve some Dish
*/
public class Menu implements Serializable {
    private ArrayList<Dish> menu;
    private Dish bestmenu;
    private Dish todaySpecial;
    private final String fileName = "menu.txt";
    private Inventory inventory;
    private String restaurantName;

    public Menu(Inventory inventory, String restaurantName) throws IOException
    {
        menu = new ArrayList<Dish>();
        this.bestmenu = null;
        this.todaySpecial=null;
        this.inventory = inventory;
        this.restaurantName = restaurantName;
        readMenu();
        writeMenu();
    }

    public boolean readMenu() throws IOException
    {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String line = fileReader.readLine(); //skipping insignificant lines
            line = fileReader.readLine();//skipping insignificant lines
            line = fileReader.readLine();//skipping insignificant lines
            int i = 0;
            String[] testBlank = null;
            while (line != null) {
                line = fileReader.readLine();
                if (line != null && line.split(",").length>1) {
                    processLine(line);
                    i++;
                } else {
                    break;
                }
            }
        }

        return false;
    }

    public void processLine(String line)
    {
        Recipe recipe = new Recipe();
        String[] stringArray = line.split(",");
        String dishName = stringArray[0];

        String priceString = stringArray[1].replaceAll(" ", "");
        double price = Double.parseDouble(priceString);

        for (int i=2; i<stringArray.length-1; i++)
        {
            String[] ingreNameAmount = stringArray[i].replaceAll(" ","").split(":");
            String ingreName = ingreNameAmount[0];
            String ingreAmount = ingreNameAmount[1];

            if (inventory.isInInv(ingreName)) {
                Ingredient ingredient = inventory.getIngredient(ingreName);
                recipe.addIngredient(ingredient,Double.parseDouble(ingreAmount));
            } else {
            }
        }
        Dish dish = new Dish(dishName, price, recipe.getRecipeList());
        menu.add(dish);
    }


    public boolean writeMenu()
    {
        boolean ret = false;
        File fNew = new File(fileName);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        try {
            FileWriter fStream = new FileWriter(fNew, false);
            BufferedWriter out = new BufferedWriter(fStream);
            out.write("[Menu File | "+ dateFormat.format(date)+" | "+this.restaurantName+"]");
            out.newLine();
            out.newLine();
            String formatStr = "%-30s %-10s %-40s%n";
            out.write(String.format(formatStr, "Dish Name", "Price", "Ingredients"));
            for (Dish dish : menu)
            {
                String name = dish.getName();
                double price  = dish.getPrice();
                HashMap<Ingredient, Double> ingredients = (HashMap) dish.getDishRecipe();
                String ingredientsString = "";
                for (Map.Entry<Ingredient, Double> entry : ingredients.entrySet()) {
                    ingredientsString = ingredientsString + entry.getKey().getName() +":"+ entry.getValue()+", ";
                }
                out.write(String.format(formatStr, name+"," , String.valueOf(price)+"," , ingredientsString));
            }
            ret = true;
            out.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Warning");
            alert.setContentText("IOException");
            alert.showAndWait();
            ret = false;
        }
        return ret;
    }

    public void addDish(Dish dish) {
        if (!isInMenu(dish.getName())) {
            menu.add(dish);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Warning");
            alert.setContentText("This dish is already in Menu");
            alert.showAndWait();
        }
    }

    public void removeMenu(Dish dish) {
        if (menu.contains(dish) == false) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Warning");
            alert.setContentText("This dish is not in Menu");
            alert.showAndWait();
        } else {
            menu.remove(dish);
        }
    }

    /**
     *
     * @param dishName
     * @return Returns true if DishName is in the menu.
     */
    public boolean isInMenu(String dishName)
    {
        boolean ret = false;
        for (Dish d : menu)
        {
            if (d.getName().equals(dishName))
            {
                ret = true;
            }
        }
        return ret;
    }

    public Dish getDish(String dishName)
    {
        Dish ret = null;
        for (Dish d : menu)
        {
            if (d.getName().equals(dishName))
            {
                ret = d;
                break;
            }
        }
        return ret;
    }


    public void showMenu() {
        System.out.println();
        System.out.println("======== Menu ========");
        for (Dish d : menu) {
            System.out.println(d.getName()+" | Ingredients: "+ d.getDishRecipe());
            System.out.println();
        }
    }

    @Override
    public String toString() {
        String listString = "";
        for (Dish d : menu) {
            listString += d + "  :  $" + d.getPrice() + "\n";
        }
        return listString;
    }

    public ArrayList<Dish> getMenu() {
        return menu;
    }

    public Dish getBestmenu() {
        if(this.bestmenu == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("");
            alert.setContentText("Don't have Best menu yet");
            alert.showAndWait();
            //System.out.println("Don't have Best menu yet");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Best Menu:");
            alert.setHeaderText("");
            alert.setContentText("Best Menu: "+ bestmenu);
            alert.showAndWait();
            //System.out.println("Best Menu: "+ bestmenu);
        }
        return bestmenu;
    }

    public void setBestmenu(Dish bestmenu) {
        this.bestmenu = bestmenu;
    }

    public Dish getTodaySpecial(){
        if(this.todaySpecial==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Specials:");
            alert.setHeaderText("");
            alert.setContentText("Don't have today's special yet");
            alert.showAndWait();
            //System.out.println("Don't have today's special yet");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Specials:");
            alert.setHeaderText("");
            alert.setContentText("Today's Special: " + todaySpecial);
            alert.showAndWait();
            //System.out.println("Today's Special: " + todaySpecial);
        }
        return todaySpecial;
    }

    public void setTodaySpecial(Dish todaySpecial) {
        this.todaySpecial = todaySpecial;
    }
}


