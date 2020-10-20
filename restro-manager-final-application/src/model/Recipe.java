package model;
import java.util.*;

public class Recipe
{

    private Map<Ingredient, Double> recipeList;

    public Recipe ()
    {
        recipeList = new HashMap<>();
    }

    public void addIngredient(Ingredient ingredient, double amount)
    {
        recipeList.put(ingredient,amount);
    }

    public Map<Ingredient,Double> getRecipeList()
    {
        return recipeList;
    }
}
