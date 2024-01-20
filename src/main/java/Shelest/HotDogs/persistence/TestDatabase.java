package Shelest.HotDogs.persistence;

import Shelest.HotDogs.model.Order;
import Shelest.HotDogs.model.Ingredient;
import Shelest.HotDogs.model.Recipe;

import java.util.List;
import java.util.Objects;

// Реалізація інтерфейсу 'Database', що використовується для тестових цілей
public class TestDatabase implements Database {

    @Override
    public void saveOrder(Order order) {
        System.out.printf("--> Saved order: %s%n", order);
    }

    @Override
    public List<Order> getOrders() {
        return null;
    }

    @Override
    public void saveIngredient(Ingredient ingredient) {

    }

    @Override
    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredients = List.of(
                new Ingredient("майонез", false, 1, 2),
                new Ingredient("кетчуп", false, 0.5, 1),
                new Ingredient("гірчиця", false, 0.3, 1),
                new Ingredient("солодка цибуля", true, 2.4, 5),
                new Ingredient("халапеньйо", true, 3.7, 5),
                new Ingredient("чилі", true, 1.5, 3),
                new Ingredient("солоний огірок", true, 1.2, 3)
        );

        for (int i = 1; i <= ingredients.size(); i++) {
            ingredients.get(i).setId(i);
        }

        return ingredients;
    }

    @Override
    public void saveRecipe(Recipe recipe) {

    }

    @Override
    public List<Recipe> getRecipes() {
        List<Recipe> recipes = List.of(
                new Recipe("Стандартний", List.of(
                        getIngredientById(1),
                        getIngredientById(2),
                        getIngredientById(3)
                )),
                new Recipe("Гострий", List.of(
                        getIngredientById(1),
                        getIngredientById(2),
                        getIngredientById(3),
                        getIngredientById(5),
                        getIngredientById(6)
                )),
                new Recipe("Данський", List.of(
                        getIngredientById(1),
                        getIngredientById(2),
                        getIngredientById(3),
                        getIngredientById(4),
                        getIngredientById(7)
                ))
        );

        for (int i = 1; i <= recipes.size(); i++) {
            recipes.get(i).setId(i);
        }

        return recipes;
    }

    private Ingredient getIngredientById(int id) {
        return getIngredients().stream()
                .filter(i -> Objects.equals(i.getId(), id))
                .findFirst()
                .get();
    }
}
