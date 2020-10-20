package model;

import javafx.scene.control.Alert;

import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.*;
import java.util.Date;

public class Manager extends Receiver
{
    private final int request = 20;
    private Inventory inventory;
    private ArrayList<Ingredient> deficientIngredients;
    private ArrayList<Ingredient> ingredients;

    private String name;
    private String EmployeeID;

    public Manager (String name, String id, Inventory inv)
    {
        super(name,id,inv);
        this.inventory = inv;
        this.ingredients = inv.getIngredientList();
        this.deficientIngredients = inv.getDeficientIngredients();
    }

    /**
     * cycles through list of ingredients, sorted by deficient ingredients first, and displays a list of
     * (name, amount, threshold, unit), and indicates which ingredients are deficient.
     * prints to text file.
     */
    public void printInventoryToConsole() throws IOException
    {
        updateDefIngredients();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        String name;
        double amount;
        double threshold;
        String unit;
        boolean print = true;
        String formatStr = "%-25s %-10s %-10s %-15s%n";

        System.out.println();
        System.out.println("============================================================");
        System.out.println("[Inventory File | "+ dateFormat.format(date)+"]");
        System.out.println();
        System.out.println("[Ingredients below reorder threshold]");

        System.out.println(String.format(formatStr, "Name", "Amount", "Threshold", "Units"));

        for (int i=0; i<deficientIngredients.size(); i++)
        {
            name = deficientIngredients.get(i).getName();
            amount = deficientIngredients.get(i).getAmount();
            threshold = deficientIngredients.get(i).getThreshold();
            unit = deficientIngredients.get(i).getUnit();

            System.out.print(String.format(formatStr, name+",", String.valueOf(amount)+",", String.valueOf(threshold)+",", unit));
        }
        System.out.println();
        System.out.println("[Ingredients above reorder threshold]");
        System.out.println(String.format(formatStr, "Name", "Amount", "Threshold", "Units"));
        for (int i=0; i<ingredients.size(); i++)
        {
            for (int j=0; j<deficientIngredients.size(); j++)
            {
                if (ingredients.get(i).getName().equals(deficientIngredients.get(j).getName()))
                {
                    print = false;
                    break;
                }
                else
                {
                    print = true;
                }
            }
            if (print)
            {
                name = ingredients.get(i).getName();
                amount = ingredients.get(i).getAmount();
                threshold = ingredients.get(i).getThreshold();
                unit = ingredients.get(i).getUnit();

                System.out.print(String.format(formatStr, name+",", String.valueOf(amount)+",", String.valueOf(threshold)+",", unit));
            }
        }
        System.out.println("============================================================");
        System.out.println();
    }

    public void printInventoryToTextFile()
    {
        updateDefIngredients();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String fileNameOut = "src/ManagerInventoryPrint.txt";
        System.out.println("PrintInventoryToTextFile");
        File fNew = new File(fileNameOut);
        System.out.println("PrintInventoryToTextFile  New File");
        try {
            FileWriter fStream = new FileWriter(fNew, false);
            BufferedWriter out = new BufferedWriter(fStream);
            out.write("[Inventory File | "+ dateFormat1.format(date)+"]");out.newLine();
            out.newLine();
            out.write("[Ingredients below reorder threshold]");
            out.newLine();
            String formatStr = "%-25s %-10s %-10s %-15s%n";
            out.write(String.format(formatStr, "Name", "Amount", "Threshold", "Units"));
            for (int i=0; i<deficientIngredients.size(); i++)
            {
                String name = deficientIngredients.get(i).getName();
                double amount = deficientIngredients.get(i).getAmount();
                double threshold = deficientIngredients.get(i).getThreshold();
                String unit = deficientIngredients.get(i).getUnit();

                out.write(String.format(formatStr, name+",", String.valueOf(amount)+",", String.valueOf(threshold)+",", unit));
            }
            out.newLine();
            out.write("[Ingredients above reorder threshold]");
            out.newLine();
            out.write(String.format(formatStr, "Name", "Amount", "Threshold", "Units"));
            boolean print = true;
            for (int i=0; i<ingredients.size(); i++)
            {
                for (int j=0; j<deficientIngredients.size(); j++)
                {
                    if (ingredients.get(i).getName().equals(deficientIngredients.get(j).getName()))
                    {
                        print = false;
                        break;
                    }
                    else
                    {
                        print = true;
                    }
                }
                if (print)
                {
                    String name = ingredients.get(i).getName();
                    double amount = ingredients.get(i).getAmount();
                    double threshold = ingredients.get(i).getThreshold();
                    String unit = ingredients.get(i).getUnit();
                    out.write(String.format(formatStr, name+",", String.valueOf(amount)+",", String.valueOf(threshold)+",", unit));
                }
            }
            System.out.println("PrintInventoryToTextFile  out.close");
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

    private void updateDefIngredients()
    {
        this.deficientIngredients = inventory.getDeficientIngredients();
    }

    public void printOrderRequest()
    {
        updateDefIngredients();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String fileNameOut = "src/requests.txt";
        File fNew = new File(fileNameOut);
        try {
            FileWriter fStream = new FileWriter(fNew, false);
            BufferedWriter out = new BufferedWriter(fStream);
            out.write("[Order Requests | "+ dateFormat1.format(date)+"]");out.newLine();
            out.newLine();
            String formatStr = "%-25s %-10s %-10s %-15s%n";
            out.write(String.format(formatStr, "Name", "Amount", "Threshold", "Units"));
            for (int i=0; i<deficientIngredients.size(); i++)
            {
                String name = deficientIngredients.get(i).getName();
                double threshold = deficientIngredients.get(i).getThreshold();
                String unit = deficientIngredients.get(i).getUnit();

                out.write(String.format(formatStr, name+",", request+",", String.valueOf(threshold)+",", unit));
            }
            out.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("IOException in Manager.printInventoryToTextFile()");
        }
    }

    public void addDeliveryToInventory(Ingredient ingredient)
    {
        inventory.addIngredient(ingredient);
    }

}
