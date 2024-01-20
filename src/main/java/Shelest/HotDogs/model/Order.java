package Shelest.HotDogs.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Order implements Identifiable {

    int id;
    List<Recipe> recipes;
    double amount;

    public Order(List<Recipe> recipes, double amount) {
        this.recipes = recipes;
        this.amount = amount;
    }
}
