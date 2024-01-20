package Shelest.HotDogs.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Ingredient implements Identifiable {

    int id;
    String name;
    boolean isTopping;
    double costPrice;
    double sellingPrice;

    public Ingredient(String name, boolean isTopping, double costPrice, double sellingPrice) {
        this.name = name;
        this.isTopping = isTopping;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
    }
}
