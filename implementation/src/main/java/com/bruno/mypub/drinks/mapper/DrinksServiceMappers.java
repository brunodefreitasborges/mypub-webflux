package com.bruno.mypub.drinks.mapper;

import com.bruno.mypub.drinks.repository.entities.DrinkEntity;
import com.bruno.mypub.drinks.model.request.DrinksServiceRequest;
import com.bruno.mypub.drinks.model.response.DrinksServiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DrinksServiceMappers {

    DrinksServiceMappers INSTANCE = Mappers.getMapper(DrinksServiceMappers.class);

    DrinksServiceResponse drinkEntityToResponse(DrinkEntity entity);

    default DrinkEntity drinkRequestToEntity(DrinksServiceRequest request) {
        return DrinkEntity.builder()
                .name(request.getName())
                .glass(request.getGlass())
                .garnish(request.getGarnish())
                .preparation(request.getPreparation())
                .build();
    }

}
