package Shelest.HotDogs.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Recipe implements Identifiable {

    int id;
    String name;
    List<Ingredient> ingredients;

    public Recipe(List<Ingredient> ingredients) {
        this("Custom", ingredients);
    }

    public Recipe(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }
}
