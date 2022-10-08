package com.bruno.mypub.common.query;


import com.bruno.mypub.common.enums.IngredientCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientQuery {

    @Builder.Default
    private String id = "0";
    @NotNull(message = "Category is required")
    private IngredientCategory category;
    @Builder.Default
    private String name = "";
    @Builder.Default
    private Double priceFloor = 0.00;
    @Builder.Default
    private Double priceCeil = 0.00;
    @Builder.Default
    private Integer stock = 0;

}
