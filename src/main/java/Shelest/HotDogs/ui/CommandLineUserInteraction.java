package Shelest.HotDogs.ui;

import Shelest.HotDogs.model.Ingredient;
import Shelest.HotDogs.model.Order;
import Shelest.HotDogs.model.Recipe;
import Shelest.HotDogs.persistence.Database;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CommandLineUserInteraction implements UserInteraction {

    Database database;

    @Override
    public void run() {
        List<Recipe> chosenRecipes = new ArrayList<>();
        do {
            chosenRecipes.add(chooseRecipeInteraction());
        } while (addAnotherInteraction());

        calculateAndCreateOrder(chosenRecipes);
    }

    //Консольна взаємодія з пропозицією обрати рецепт для додавання до замовлення
    private Recipe chooseRecipeInteraction() {
        System.out.println("\n" + getFormattedDefaultRecipes());

        while (true) {
            int input = printMessageAndWaitInt("Введіть номер рецепту щоб додати до замовлення");

            if (input == 0) {
                return createRecipeInteraction();
            }

            Optional<Recipe> recipe = database.getRecipes().stream()
                    .filter(r -> r.getId() == input)
                    .findFirst();

            if (recipe.isPresent()) {
                if (printMessageAndWaitBoolean("\nБажаєте додати топінги?")) {
                    List<Ingredient> ingredients = new ArrayList<>(recipe.get().getIngredients());
                    addIngredientsInteraction(ingredients, true);
                    recipe.get().setIngredients(ingredients);
                }

                return recipe.get();
            }

            System.out.printf("Рецепт з ID %s не існує%n", input);
        }
    }

    //Консольна взаємодія для створення власного рецепту
    private Recipe createRecipeInteraction() {
        List<Ingredient> ingredients = new ArrayList<>();

        addIngredientsInteraction(ingredients, false);

        if (printMessageAndWaitBoolean("\nБажаєте додати топінги?")) {
            addIngredientsInteraction(ingredients, true);
        }

        return new Recipe(ingredients);
    }

    //Консольна взаємодія з пропозицією обрати основні інградієнти або додати топінги до хот-дога
    private void addIngredientsInteraction(List<Ingredient> ingredients, boolean addToppings) {
        database.getIngredients().stream()
                .filter(ingredient -> ingredient.isTopping() == addToppings)
                .forEach(ingredient -> {
                    String message = "\nДодати %s?".formatted(ingredient.getName());
                    if (printMessageAndWaitBoolean(message)) {
                        ingredients.add(ingredient);
                    }
                });
    }

    //Консольна взаємодія з пропозицією додати ще один хот-дог до замовлення
    private boolean addAnotherInteraction() {
        return printMessageAndWaitBoolean("\nБажаєте додати ще один хот-дог до замовлення?");
    }

    //Розраховує суму, створює замовлення і зберігає в базу даних
    private void calculateAndCreateOrder(List<Recipe> recipes) {
        int discount = calculateDiscountByOrderSize(recipes.size());
        double orderAmount = calculateOrderAmount(recipes, discount);

        Order order = new Order(recipes, orderAmount);

        String message = "\nВаше замовлення: \n%s\nНа суму: %.2f гривень"
                .formatted(getFormattedRecipes(recipes), order.getAmount());
        if (discount != 0) {
            message += "(знижка %d%%)".formatted(discount);
        }

        System.out.print(message);

        if (printMessageAndWaitBoolean("\n\nВсе вірно?")) {
            database.saveOrder(order);
            System.out.println("\nДякуємо за замовлення!");
        }
    }

    //Розрахувати суму замовлення з урахуванням базової вартості і знижки
    private double calculateOrderAmount(List<Recipe> recipes, int discount) {
        double basePrice = 30;

        double ingredientsAmount = recipes.stream()
                .map(Recipe::getIngredients)
                .flatMap(List::stream)
                .mapToDouble(Ingredient::getSellingPrice)
                .sum();
        double orderBaseAmount = recipes.size() * basePrice;

        double orderTotalAmount = orderBaseAmount + ingredientsAmount;
        double discountAmount = (orderTotalAmount * discount) / 100.0;

        return orderTotalAmount - discountAmount;
    }

    //Розраховує розмір знижки залежно від кількості замовлених хот-догів
    private int calculateDiscountByOrderSize(int orderSize) {
        //Ключ - мінімальний розмір замовлення, значення - розмір знижки у %
        Map<Integer, Integer> discounts = new HashMap<>() {{
            put(3, 5);
            put(6, 7);
            put(10, 10);
        }};

        int discount = 0;
        for (Map.Entry<Integer, Integer> entry : discounts.entrySet()) {
            if (orderSize >= entry.getKey()) {
                discount = entry.getValue();
            }
        }
        return discount;
    }

    //Форматує наявні в базі даних рецепти для другу в консоль
    //Також включає можливість створити свій рецепт під індексом "0"
    private String getFormattedDefaultRecipes() {
        String formattedRecipes = getFormattedRecipes(database.getRecipes());

        return "0 - Створити власний рецепт\n" + formattedRecipes;
    }

    private String getFormattedRecipes(List<Recipe> recipes) {
        return recipes.stream()
                .map(this::formatRecipe)
                .collect(Collectors.joining("\n"));
    }

    //Форматує конкретний рецепт для друку в консоль
    //Приклад - "2 - Стандарт (кетчуп, майонез, гірчиця)"
    private String formatRecipe(Recipe recipe) {
        String ingredients = recipe.getIngredients().stream()
                .map(Ingredient::getName)
                .collect(Collectors.joining(", "));

        return "%s - %s (%s)".formatted(recipe.getId(), recipe.getName(), ingredients);
    }

    //Друкує повідомлення в консоль і очікує від користувача ціле число як відповідь
    private int printMessageAndWaitInt(String inputMessage) {
        inputMessage += ": ";
        System.out.print(inputMessage);

        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.println("Неправильне введення.");
            System.out.print(inputMessage);
        }

        return scanner.nextInt();
    }

    //Друкує повідомлення в консоль і очікує від користувача "Так" або "Ні" як відповідь
    private boolean printMessageAndWaitBoolean(String inputMessage) {
        inputMessage += "\n1 - Так\n2 - Ні\nВідповідь: ";
        System.out.print(inputMessage);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!scanner.hasNextInt()) {
                scanner.next();
                System.out.print("Неправильне введення.\nВідповідь: ");
                continue;
            }

            int input = scanner.nextInt();

            if (!List.of(1, 2).contains(input)) {
                System.out.print("Неправильне введення.\nВідповідь: ");
                continue;
            }

            return input == 1;
        }
    }
}
