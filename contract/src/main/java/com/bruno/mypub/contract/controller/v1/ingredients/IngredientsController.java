package com.bruno.mypub.contract.controller.v1.ingredients;

import com.bruno.mypub.common.enums.IngredientCategory;
import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.contract.controller.v1.ingredients.facade.IngredientsControllerFacade;
import com.bruno.mypub.contract.controller.v1.ingredients.model.request.IngredientsControllerRequest;
import com.bruno.mypub.contract.controller.v1.ingredients.model.response.IngredientsControllerResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("v1/ingredients")
public class IngredientsController {

    private final IngredientsControllerFacade ingredientsControllerFacade;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<IngredientsControllerResponse> createNewIngredient(@RequestBody @Valid IngredientsControllerRequest request) {
        return ingredientsControllerFacade.createNewIngredient(request);
    }

    @PutMapping("")
    public Mono<IngredientsControllerResponse> updateIngredient(
            @RequestParam String id,
            @RequestBody @Valid IngredientsControllerRequest request) {
        return ingredientsControllerFacade.updateIngredient(id, request);
    }

    @GetMapping("")
    public Flux<IngredientsControllerResponse> getIngredients(
            @Valid @RequestBody IngredientQuery query) {
        return ingredientsControllerFacade.getIngredients(query);
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<String> deleteIngredients(
            @RequestParam String id, @RequestParam IngredientCategory category) {
        return ingredientsControllerFacade.deleteIngredients(id, category);
    }

}
