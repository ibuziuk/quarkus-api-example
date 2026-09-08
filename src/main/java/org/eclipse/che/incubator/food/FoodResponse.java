package org.eclipse.che.incubator.food;

import java.math.BigDecimal;

public class FoodResponse {

    public Long id;
    public String name;
    public String restaurantName;
    public BigDecimal price;

    public static FoodResponse from(Food food) {
        FoodResponse response = new FoodResponse();
        response.id = food.id;
        response.name = food.name;
        response.restaurantName = food.restaurantName;
        response.price = food.price;
        return response;
    }
}
