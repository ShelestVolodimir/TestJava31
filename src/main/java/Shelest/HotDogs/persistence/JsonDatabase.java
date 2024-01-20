package Shelest.HotDogs.persistence;

import Shelest.HotDogs.model.Order;
import Shelest.HotDogs.model.Ingredient;
import Shelest.HotDogs.model.Recipe;
import Shelest.HotDogs.model.Identifiable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static Shelest.HotDogs.util.FileUtils.JacksonJSON.readValuesFromFile;
import static Shelest.HotDogs.util.FileUtils.JacksonJSON.writeValuesToFile;


// Реалізація інтерфейсу 'Database', що працює з даними формату JSON,
// які збережені у вигляді файлів на диску
public class JsonDatabase implements Database {

    private static final String ROOT_FOLDER = "src/main/resources";
    private static final String DB_FOLDER = "database";

    private static final String ORDERS_DB_FILENAME = "orders_db.json";
    private static final String INGREDIENTS_DB_FILENAME = "ingredients_db.json";
    private static final String RECIPES_DB_FILENAME = "recipes_db.json";

    @Override
    public void saveOrder(Order order) {
        List<Order> orders = getOrders();
        incrementIdAndAddToList(orders, order);

        writeValuesToFile(getDBFilenamePath(ORDERS_DB_FILENAME), orders);
    }

    @Override
    public List<Order> getOrders() {
        return readValuesFromFile(getDBFilenamePath(ORDERS_DB_FILENAME), Order.class);
    }

    @Override
    public void saveIngredient(Ingredient ingredient) {
        List<Ingredient> ingredients = getIngredients();
        incrementIdAndAddToList(ingredients, ingredient);

        writeValuesToFile(getDBFilenamePath(INGREDIENTS_DB_FILENAME), ingredients);
    }

    @Override
    public List<Ingredient> getIngredients() {
        return readValuesFromFile(getDBFilenamePath(INGREDIENTS_DB_FILENAME), Ingredient.class);
    }

    @Override
    public void saveRecipe(Recipe recipe) {
        List<Recipe> recipes = getRecipes();
        incrementIdAndAddToList(recipes, recipe);

        writeValuesToFile(getDBFilenamePath(RECIPES_DB_FILENAME), recipes);
    }

    @Override
    public List<Recipe> getRecipes() {
        return readValuesFromFile(getDBFilenamePath(RECIPES_DB_FILENAME), Recipe.class);
    }

    // Проставляє значення id для створюваного об'єкта на 1 більше від останнього доданого в БД
    // Якщо база даних порожня встановлюється стандартне значення - 1
    private <T extends Identifiable> void incrementIdAndAddToList(List<T> existingObjects, T newObject) {
        int lastId = existingObjects.stream()
                .map(Identifiable::getId)
                .max(Integer::compareTo)
                .orElse(0);

        newObject.setId(lastId + 1);
        existingObjects.add(newObject);
    }

    // Будує шлях до файлу БД
    private Path getDBFilenamePath(String filename) {
        Path rootPath = Paths.get(ROOT_FOLDER, DB_FOLDER);
        return rootPath.resolve(Paths.get(filename)).normalize();
    }
}
