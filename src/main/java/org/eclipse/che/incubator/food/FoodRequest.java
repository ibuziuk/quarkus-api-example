package org.eclipse.che.incubator.food;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FoodRequest {

    @NotBlank
    @Size(max = 40)
    public String name;

    @NotBlank
    @Size(max = 40)
    public String restaurantName;

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    public BigDecimal price;
}
