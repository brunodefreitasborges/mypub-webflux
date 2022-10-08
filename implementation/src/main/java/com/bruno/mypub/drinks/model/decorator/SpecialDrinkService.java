package com.bruno.mypub.drinks.model.decorator;

import com.bruno.mypub.common.utilities.DoubleFormatter;
import com.bruno.mypub.drinks.mapper.DrinksServiceMappers;
import com.bruno.mypub.drinks.model.response.DrinksServiceResponse;
import com.bruno.mypub.drinks.repository.DrinksRepository;
import com.bruno.mypub.ingredients.model.request.IngredientsServiceRequest;
import com.bruno.mypub.ingredients.service.IngredientStrategyFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class SpecialDrinkService {

    private final DrinksRepository drinksRepository;
    private final IngredientStrategyFacade ingredientStrategyFacade;

    public Mono<DrinksServiceResponse> getSpecialDrink(String id, List<IngredientsServiceRequest> ingredientsRequest) {

        return Flux.fromIterable(ingredientsRequest)
                .flatMap(ingredientStrategyFacade::getIngredientForDrink)
                .collectList()
                .map(ingredientEntities -> drinksRepository.findById(id)
                        .map(drinkEntity -> new SpecialDrink(new BasicDrink(), ingredientEntities)
                                .assemble(drinkEntity))).flatMap(drinkEntityMono -> drinkEntityMono
                        .map(drinkEntity -> {
                            drinkEntity.setCost(DoubleFormatter.formatNumber(drinkEntity.getCost()));
                            return drinkEntity;
                        })).flatMap(drinkEntity -> Mono.just(DrinksServiceMappers.INSTANCE
                        .drinkEntityToResponse(drinkEntity)));
    }
}
