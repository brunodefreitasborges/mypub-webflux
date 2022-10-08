package com.emailmicroservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientModel {

    private String id;
    private String category;
    private String name;
    private Double price;
    private Integer stock;
    private Integer quantity;
    private Double abv;
}
