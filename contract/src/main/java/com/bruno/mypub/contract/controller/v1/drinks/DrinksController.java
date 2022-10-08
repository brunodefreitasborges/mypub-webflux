package com.bruno.mypub.contract.controller.v1.drinks;

import com.bruno.mypub.common.query.DrinkQuery;
import com.bruno.mypub.contract.controller.v1.drinks.facade.DrinksControllerFacade;
import com.bruno.mypub.contract.controller.v1.drinks.model.request.DrinksControllerRequest;
import com.bruno.mypub.contract.controller.v1.drinks.model.response.DrinksControllerResponse;
import com.bruno.mypub.contract.controller.v1.ingredients.model.request.IngredientsControllerRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("v1/drinks")
public class DrinksController {

    private final DrinksControllerFacade drinksControllerFacade;

    @PostMapping("")
    public Mono<DrinksControllerResponse> createDrink(@RequestBody @Valid DrinksControllerRequest request) {
        return drinksControllerFacade.createDrink(request);
    }

    @GetMapping("")
    public Flux<DrinksControllerResponse> getDrinks(@RequestBody @Valid DrinkQuery drinkQuery) {
        return drinksControllerFacade.getDrinks(drinkQuery);
    }

    @DeleteMapping("")
    public Mono<String> deleteDrink(@RequestParam(defaultValue = "0") String id) {
        return drinksControllerFacade.deleteDrink(id);
    }

    @PutMapping("")
    public Mono<DrinksControllerResponse> updateDrink(
            @RequestParam String id,
            @RequestBody @Valid DrinksControllerRequest request) {
        return drinksControllerFacade.updateDrink(id, request);
    }

    @GetMapping("/decorator")
    public Mono<DrinksControllerResponse> getSpecialDrink(
            @RequestParam String id,
            @RequestBody List<IngredientsControllerRequest> ingredientsRequests) {
        return drinksControllerFacade.getSpecialDrink(id, ingredientsRequests);
    }

}
