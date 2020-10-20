package model;

import javafx.scene.control.Alert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;


public class Inventory {
    private ArrayList<Ingredient> ingredients;
    //file directory for inventory.txt
    private final String fileName = "inventory.txt";
    private String restaurantName;

    /**
     * Creates a new Inventory, then loads list of ingredients from text file.
     * @throws IOException
     */
    public Inventory(String restaurantName) throws IOException{
        ingredients = new ArrayList<Ingredient>();
        this.restaurantName = restaurantName;
        loadIngredients();
        updateInventoryFile();
    }

    /**
     * Loads the list of ingredients from text file.
     * @throws IOException
     */
    public void loadIngredients() throws IOException
    {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String line = fileReader.readLine(); //skipping insignificant lines
            line = fileReader.readLine();//skipping insignificant lines
            line = fileReader.readLine();//skipping insignificant lines
            //System.out.println("Importing inventory data from "+ fileNameIn);
            int i = 0;
            String[] testBlank = null;
            while (line != null) {
                line = fileReader.readLine();
                if (line != null) {
                    testBlank = line.split(",");
                }
                i++;
                if (line != null && testBlank.length == 4) {
                    processLine(line);
                } else
                {
                    break;
                }
            }
        }
    }

    /**
     * Process each line from inventory.txt using regex to create new ingredient.
     * @param line
     */
    private void processLine(String line)
    {
        String[] array = line.split(",");
        String name = array[0];

        String amountString = array[1].replace(" ","");
        double amount = Double.parseDouble(amountString);

        String thresholdString = array[2].replace(" ","");
        double threshold = Double.parseDouble(thresholdString);
        String unit = array[3].replace(" ","");
        Ingredient ingredient = new Ingredient(name,amount,threshold,unit);

        boolean add = false;
        if (ingredients.size()==0)
        {
            ingredients.add(ingredient);
        } else {
            for (int i=0; i<ingredients.size(); i++)
            {
                if (ingredient.getName().equals(ingredients.get(i).getName()) && ingredient.getUnit().equals(ingredients.get(i).getUnit()))
                {
                    ingredients.get(i).setAmount(ingredients.get(i).getAmount()+ingredient.getAmount());
                    break;
                } else
                {
                    add = true;
                }
            }
            if (add)
            {
                ingredients.add(ingredient);
            }
        }
    }

    public void updateInventoryFile()
    {
        File fNew = new File(fileName);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        try {
         FileWriter fStream = new FileWriter(fNew, false);
         BufferedWriter out = new BufferedWriter(fStream);
         out.write("[Inventory File | "+ dateFormat.format(date)+" | "+this.restaurantName+"]");
         out.newLine();
         out.newLine();
         String formatStr = "%-25s %-10s %-10s %-15s%n";
         out.write(String.format(formatStr, "Name", "Amount", "Threshold", "Units"));
         for (Ingredient i : ingredients)
         {
             String name = i.getName();
             double amount = i.getAmount();
             double threshold = i.getThreshold();
             String units = i.getUnit();

             out.write(String.format(formatStr, name+",", String.valueOf(amount)+",", String.valueOf(threshold)+",", units));
         }
         out.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Warning");
            alert.setContentText("IOException");
            alert.showAndWait();
            //System.out.println("IOException");
        }
    }

    /**
     *
     * @return returns the list of ingredients
     */
    public ArrayList getIngredientList()
    {
        return ingredients;
    }

    /**
     * Add ingredient to ingredients.
     * @param ingredient
     */
    public void addIngredient(Ingredient ingredient)
    {
        if (isInInv(ingredient)) {
            addSomeIngredient(ingredient, ingredient.getAmount());
        } else {
            ingredients.add(ingredient);
        }
    }

    /**
     * Iterates over Ingredients and returns list of ingredients that needs to be reordered.
     * @return
     */
    public ArrayList getDeficientIngredients(){
        ArrayList<Ingredient> getDeficientIngredients = new ArrayList<>();
        for ( int i=0; i<ingredients.size(); i++) {
            if (ingredients.get(i).reorder())
            {
                getDeficientIngredients.add(ingredients.get(i));
            }
        }
        return getDeficientIngredients;
    }


    /**
     * Removes a specified amount of a specified ingredient.
     * @param ingredient the ingredient that the customer wants to remove
     * @param amount the amount of ingredients
     */
    public void removeSomeIngredients(Ingredient ingredient, double amount)
    {
        for ( int i=0; i< ingredients.size(); i++)
        {
            if (ingredients.get(i).getName().equals(ingredient.getName()))
            {
                ingredients.get(i).setAmount(ingredients.get(i).getAmount()-amount);
            }
        }
    }

    public void removeSomeIngredients(Dish dish)
    {

        for (Map.Entry<Ingredient, Double> entry : dish.getDishRecipe().entrySet())
        {
            Ingredient ingredient = entry.getKey();
            double amount = entry.getValue();
            for (int i = 0; i < ingredients.size(); i++)
            {
                if (ingredients.get(i).getName().equals(ingredient.getName()))
                {
                    ingredients.get(i).setAmount(ingredients.get(i).getAmount() - amount);
                    break;
                }
            }
        }
    }



    public boolean isEnough(Ingredient ingredient, double amount)
    {
        boolean ret = false;
        if (!isInInv(ingredient))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Look at inventory");
            alert.setContentText(ingredient.getName() +" is not in inventory");
            alert.showAndWait();
            //System.out.println(ingredient.getName() +" is not in inventory");
            return false;
        }
        for ( int i=0; i< ingredients.size(); i++)
        {
            if (ingredients.get(i).getName().equals(ingredient.getName()))
            {
                if (ingredients.get(i).getAmount()>=amount)
                {
                    ret = true;
                }
            }
        }
        return ret;
    }

    public boolean isEnough(Dish dish)
    {
        boolean ret = false;
        for (Map.Entry<Ingredient, Double> entry : dish.getDishRecipe().entrySet())
        {
            Ingredient ingredient = entry.getKey();
            double amount = entry.getValue();

            if (!isInInv(ingredient))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Look at inventory");
                alert.setContentText(ingredient.getName() +" is not in inventory");
                alert.showAndWait();
                //System.out.println(ingredient.getName() +" is not in inventory");
                return false;
            }
            for ( int i=0; i< ingredients.size(); i++)
            {
                if (ingredients.get(i).getName().equals(ingredient.getName()))
                {
                    if (ingredients.get(i).getAmount()>=amount)
                    {
                        ret = true;
                    }
                }
            }
        }
        return ret;
    }

    /**
     *
     * @param ingredient
     * @return Returns true if the specified ingredient is in this inventory
     */
    public boolean isInInv(Ingredient ingredient)
    {
        boolean ret = false;
        for ( int i=0; i< ingredients.size(); i++)
        {
            if (ingredients.get(i).getName().equals(ingredient.getName()))
            {
                ret = true;
            }
        }
        return ret;
    }

    public boolean isInInv(String ingredientName)
    {
        boolean ret = false;
        for ( int i=0; i< ingredients.size(); i++)
        {
            if (ingredients.get(i).getName().equals(ingredientName))
            {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Add more of an existing ingredient.
     * Searches for ingredient, then adds 'amount'.
     * this method is to be used by Manager after confirmation of delivered ingredients.
     * @param ingredient
     */
    private void addSomeIngredient (Ingredient ingredient, double amount)
    {
        for ( int i=0; i<ingredients.size(); i++)
        {
            if (ingredients.get(i).getName().equals(ingredient.getName()))
            {
                ingredients.get(i).setAmount((ingredients.get(i).getAmount())+(amount));
            }
        }
    }

    /**
     *
     * @param ingName
     * @return Returns a ingredient from ingredients with the specified name.
     */
    public Ingredient getIngredient(String ingName) {
        Ingredient ret = null;
        for (Ingredient ing : this.ingredients) {
            if (ing.getName().equals(ingName)) {
                ret = ing;
                break;
            }
        }
        return ret;
    }
}
