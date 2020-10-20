package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.io.*;

public class Dish implements Serializable {
    /**
     * Dish class is individual menu.
     */

    private String name;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isReturned() {
        return isreturned;
    }

    public void setisReturned(boolean returned) {
        this.isreturned = returned;
    }

    private String msg;
    private boolean isreturned =false;
    private double price;
    private Map<Ingredient, Double> dishRecipe;
    private boolean dishDelivered = false;
    private boolean isModified = false;
    private Map<Ingredient, Double> modifiedIngredients;


    /**
     * @param name        Dish's name
     * @param price       Dish's price
     * @param ingredients Dish's default ingredients that list of
     *                    <key:(Ingredient)ingredient, value: (Double)numbers of Ingredient>
     */
    public Dish(String name, double price, Map<Ingredient, Double> ingredients) {
        this.name = name;
        this.price = price;
        this.dishRecipe = ingredients;
        this.isModified = false;
        this.modifiedIngredients =new HashMap<>();
        this.isreturned = false;
        this.msg = "";
    }


    /**
     * @return (Double) Dish's price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return String Dish's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public Map<Ingredient, Double> getDishRecipe() {
        return dishRecipe;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        String str ="";
        str+="$"+this.price+"\t"+this.name +"\t";
        if(this.msg.equals("")==false){ str+= "  msg: "+this.msg;}
         return str;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public boolean isModified() {
        return isModified;
    }

    public void addModifiedIngredients(Ingredient ing ,Double db) {
        this.modifiedIngredients.put(ing,db);
    }

    public Map<Ingredient, Double> getModifiedIngredients() {
        return modifiedIngredients;
    }
}