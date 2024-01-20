package Shelest.HotDogs.persistence;

import Shelest.HotDogs.model.Order;
import Shelest.HotDogs.model.Ingredient;
import Shelest.HotDogs.model.Recipe;

import java.util.List;

public interface Database {

    void saveOrder(Order order);

    List<Order> getOrders();

    void saveIngredient(Ingredient ingredient);

    List<Ingredient> getIngredients();

    void saveRecipe(Recipe recipe);

    List<Recipe> getRecipes();
}
