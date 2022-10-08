package com.bruno.mypub.ingredients.mapper;

import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.ingredients.model.entities.DestiladoIngredientEntity;
import com.bruno.mypub.ingredients.model.entities.FruitIngredientEntity;
import com.bruno.mypub.ingredients.model.entities.IngredientEntity;
import com.bruno.mypub.ingredients.model.entities.SoftDrinkIngredientEntity;
import com.bruno.mypub.ingredients.model.request.IngredientsServiceRequest;
import com.bruno.mypub.ingredients.model.response.IngredientsServiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;


@Mapper
public interface IngredientsServiceMappers {

    IngredientsServiceMappers INSTANCE = Mappers.getMapper( IngredientsServiceMappers.class );

    IngredientQuery ingredientRequestToQuery(IngredientsServiceRequest request);
    @Mapping(target = "stock", source = "quantity")
    DestiladoIngredientEntity ingredientRequestToDestilado(IngredientsServiceRequest request);

    @Mapping(target = "stock", source = "quantity")
    SoftDrinkIngredientEntity ingredientRequestToSoftDrink(IngredientsServiceRequest request);

    @Mapping(target = "stock", source = "quantity")
    FruitIngredientEntity ingredientRequestToFruit(IngredientsServiceRequest request);

    @Mapping(target = "quantity", source = "stock")
    IngredientsServiceResponse destiladoIngredientEntityToResponse(DestiladoIngredientEntity entity);

    @Mapping(target = "quantity", source = "stock")
    IngredientsServiceResponse softDrinkIngredientEntityToResponse(SoftDrinkIngredientEntity entity);

    @Mapping(target = "quantity", source = "stock")
    IngredientsServiceResponse fruitIngredientEntityToResponse(FruitIngredientEntity entity);


}
