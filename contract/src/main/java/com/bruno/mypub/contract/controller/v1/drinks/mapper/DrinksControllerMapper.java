package com.bruno.mypub.contract.controller.v1.drinks.mapper;

import com.bruno.mypub.contract.controller.v1.drinks.model.request.DrinksControllerRequest;
import com.bruno.mypub.contract.controller.v1.drinks.model.response.DrinksControllerResponse;
import com.bruno.mypub.contract.controller.v1.ingredients.mapper.IngredientsControllerMapper;
import com.bruno.mypub.drinks.model.request.DrinksServiceRequest;
import com.bruno.mypub.drinks.model.response.DrinksServiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DrinksControllerMapper {

    DrinksControllerMapper INSTANCE = Mappers.getMapper(DrinksControllerMapper.class);

    DrinksControllerResponse drinksServiceToController(DrinksServiceResponse response);


    default DrinksServiceRequest drinksControllerToService(DrinksControllerRequest request) {
        return DrinksServiceRequest.builder()
                .ingredients(request.getIngredients().stream().map(IngredientsControllerMapper
                        .INSTANCE::ingredientControllerToService).toList())
                .glass(request.getGlass())
                .garnish(request.getGarnish())
                .name(request.getName())
                .preparation(request.getPreparation())
                .build();
    }

}
