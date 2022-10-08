package com.bruno.mypub.contract.controller.v1.ingredients.mapper;

import com.bruno.mypub.contract.controller.v1.ingredients.model.request.IngredientsControllerRequest;
import com.bruno.mypub.contract.controller.v1.ingredients.model.response.IngredientsControllerResponse;
import com.bruno.mypub.ingredients.model.request.IngredientsServiceRequest;
import com.bruno.mypub.ingredients.model.response.IngredientsServiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IngredientsControllerMapper {

    IngredientsControllerMapper INSTANCE = Mappers.getMapper( IngredientsControllerMapper.class );


    IngredientsServiceRequest ingredientControllerToService(IngredientsControllerRequest controllerRequest);

    IngredientsControllerResponse ingredientServiceToController(IngredientsServiceResponse serviceResponse);


}
