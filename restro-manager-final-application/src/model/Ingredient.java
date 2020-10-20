package model;

import java.io.*;

public class Ingredient implements Serializable{
    private String name;
    private double amount;
    private double yetAmount;
    private double threshold;
    private String unit;

    public Ingredient(String name, double amount, double threshold, String unit) {
        this.name = name;
        this.amount = amount;
        this.threshold = threshold;
        this.unit = unit;
        this.yetAmount = amount;
    }

    public Ingredient(String name, double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.threshold = threshold;
        this.unit = unit;
        this.yetAmount = amount;
    }

    public Ingredient(Ingredient ingredient) {
        this(ingredient.getName(), ingredient.getAmount(), ingredient.getThreshold(), ingredient.getUnit());
    }

    /*
    public Ingredient(String name) {
        this.name = name;
        this.amount = this.minAmount;
    }
    */

    public String getName() {
        return this.name;
    }

    public double getYetAmount() {
        return yetAmount;
    }

    public void setYetAmount(double yetAmount) {
        this.yetAmount = yetAmount;
    }

    public void setThreshold(double minAmount) {
        this.threshold = minAmount;
    }

    public double getThreshold() {
        return this.threshold;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return this.unit;
    }

    public boolean reorder() {
        return (amount < threshold);
    }

    @Override
    public String toString() {
        return this.name;
    }

}
