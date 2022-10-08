package com.bruno.mypub.drinks.service;

import com.bruno.mypub.common.query.DrinkQuery;
import com.bruno.mypub.common.utilities.DoubleFormatter;
import com.bruno.mypub.drinks.repository.entities.DrinkEntity;
import com.bruno.mypub.drinks.repository.DrinksRepository;
import com.bruno.mypub.ingredients.model.entities.IngredientEntity;
import com.bruno.mypub.ingredients.model.response.IngredientsServiceResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DrinksService {

    private final DrinksRepository drinksRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public Mono<DrinkEntity> createDrink(DrinkEntity drinkEntity) {
        return Mono.just(drinkEntity)
                .map(drinkEntity1 -> {
                    drinkEntity.setCost(drinkEntity.getIngredients().stream()
                            .mapToDouble(IngredientsServiceResponse::getPrice).sum());
                    drinkEntity.setCost(DoubleFormatter.formatNumber(drinkEntity.getCost()));
                    drinkEntity.getIngredients().forEach(ingredientEntity ->
                            ingredientEntity.setPrice(DoubleFormatter.formatNumber(ingredientEntity.getPrice())));
                    kafkaTemplate.send("drinks", "A New Drink has been added: " + drinkEntity.getName());
                    return calculateAbv(drinkEntity);
                })
                .flatMap(drinksRepository::save);
    }

    public Flux<DrinkEntity> getDrinks(DrinkQuery drinkQuery) {
        return drinksRepository.findDrink(drinkQuery);
    }

    public Mono<DrinkEntity> updateDrink(String id, DrinkEntity drinkEntityToUpdate) {
        return Mono.just(drinkEntityToUpdate)
                .flatMap(drinkEntity -> drinksRepository.findById(id)
                        .map(drinkEntitySaved -> {
            drinkEntity.setId(drinkEntitySaved.getId());
            drinkEntity.setCost(DoubleFormatter.formatNumber(drinkEntity.getIngredients().stream()
                    .mapToDouble(IngredientsServiceResponse::getPrice).sum()));
            drinkEntity.getIngredients().forEach(ingredientEntity -> ingredientEntity.setPrice(DoubleFormatter.formatNumber(ingredientEntity.getPrice())));
            kafkaTemplate.send("drinks", drinkEntity.getName() + " has been updated");
            return calculateAbv(drinkEntity);
        })).flatMap(drinksRepository::save);
    }

    public Mono<String> deleteDrink(String id) {
        return drinksRepository.customDelete(id);
    }

    private DrinkEntity calculateAbv(DrinkEntity drinkEntity) {
        List<Double> totalAbv = new ArrayList<>();
        List<Integer> totalVolume = new ArrayList<>();
        drinkEntity.getIngredients().forEach(ingredientsServiceResponse ->
                totalVolume.add(ingredientsServiceResponse.getQuantity()));
        drinkEntity.getIngredients().stream().filter(ingredientEntity -> ingredientEntity.getAbv() != null)
                .forEach(ingredientsServiceResponse ->
                        totalAbv.add(ingredientsServiceResponse.getAbv()
                                * ingredientsServiceResponse.getQuantity() / 100));
        drinkEntity.setAbv(DoubleFormatter.formatNumber((totalAbv.stream()
                .reduce(0.0, Double::sum) * 100) / totalVolume.stream().reduce(0, Integer::sum)));
        return drinkEntity;
    }

}
