package com.bruno.mypub.ingredients.repository;

import com.bruno.mypub.common.exceptions.NotFoundException;
import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.drinks.repository.entities.DrinkEntity;
import com.bruno.mypub.ingredients.model.entities.FruitIngredientEntity;
import io.spring.gradle.dependencymanagement.org.codehaus.plexus.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@AllArgsConstructor
public class FruitCustomRepositoryImpl implements FruitCustomRepository{

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<FruitIngredientEntity> findFruit(IngredientQuery fruitQuery) {
        Query query = new Query();

        if (!Objects.equals(fruitQuery.getId(), "0")) {
            query.addCriteria(Criteria.where("id").is(fruitQuery.getId()));
            return reactiveMongoTemplate.find(query, FruitIngredientEntity.class)
                    .switchIfEmpty(Flux.error(new NotFoundException("ID not found: " + fruitQuery.getId())));
        } else if(!Objects.equals(fruitQuery.getName(), "")) {
            query.addCriteria(Criteria.where("name").regex(StringUtils.capitalise(fruitQuery.getName())));
            return reactiveMongoTemplate.find(query, FruitIngredientEntity.class)
                    .switchIfEmpty(Flux.error(new NotFoundException("Fruit not found: " + fruitQuery.getName())));
        } else if(fruitQuery.getPriceFloor() != 0.00 && fruitQuery.getPriceCeil() != 0.00) {
            query.addCriteria(Criteria.where("price").gte(fruitQuery.getPriceFloor())
                    .lte(fruitQuery.getPriceCeil()));
            return reactiveMongoTemplate.find(query, FruitIngredientEntity.class);
        } else if (fruitQuery.getStock() != 0) {
            query.addCriteria(Criteria.where("stock").lte(fruitQuery.getStock()));
            return reactiveMongoTemplate.find(query, FruitIngredientEntity.class);
        } return reactiveMongoTemplate.findAll(FruitIngredientEntity.class);
    }

    @Override
    public Mono<String> customDelete(String id) {
        if(Objects.equals(id, "0")) {
            return reactiveMongoTemplate.dropCollection(FruitIngredientEntity.class)
                    .flatMap(unused -> Mono.just("Successfully Dropped Collection."));
        } Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return reactiveMongoTemplate.findAndRemove(query, FruitIngredientEntity.class)
                .switchIfEmpty(Mono.error(new NotFoundException("ID not found: " + id)))
                .flatMap(fruitIngredientEntity ->
                        Mono.just("Successfully deleted " + fruitIngredientEntity.getName()));
    }
}
