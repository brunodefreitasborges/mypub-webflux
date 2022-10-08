package com.bruno.mypub.ingredients.repository;

import com.bruno.mypub.common.exceptions.NotFoundException;
import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.drinks.repository.entities.DrinkEntity;
import com.bruno.mypub.ingredients.model.entities.DestiladoIngredientEntity;
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
public class DestiladosCustomRepositoryImpl implements DestiladosCustomRepository{

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<DestiladoIngredientEntity> findDestilados(IngredientQuery ingredientQuery) {

        Query query = new Query();

        if (!Objects.equals(ingredientQuery.getId(), "0")) {
            query.addCriteria(Criteria.where("id").is(ingredientQuery.getId()));
            return reactiveMongoTemplate.find(query, DestiladoIngredientEntity.class)
                    .switchIfEmpty(Flux.error(new NotFoundException("ID not found: " + ingredientQuery.getId())));
        } else if(!Objects.equals(ingredientQuery.getName(), "")) {
            query.addCriteria(Criteria.where("name").regex(StringUtils.capitalise(ingredientQuery.getName())));
            return reactiveMongoTemplate.find(query, DestiladoIngredientEntity.class)
                    .switchIfEmpty(Flux.error(new NotFoundException("Destilado not found: " + ingredientQuery.getName())));
        } else if(ingredientQuery.getPriceFloor() != 0.00 && ingredientQuery.getPriceCeil() != 0.00) {
            query.addCriteria(Criteria.where("price").gte(ingredientQuery.getPriceFloor())
                    .lte(ingredientQuery.getPriceCeil()));
            return reactiveMongoTemplate.find(query, DestiladoIngredientEntity.class);
        } else if (ingredientQuery.getStock() != 0) {
            query.addCriteria(Criteria.where("stock").lte(ingredientQuery.getStock()));
            return reactiveMongoTemplate.find(query, DestiladoIngredientEntity.class);
        } return reactiveMongoTemplate.findAll(DestiladoIngredientEntity.class);


    }

    @Override
    public Mono<String> customDelete(String id) {
        if(Objects.equals(id, "0")) {
            return reactiveMongoTemplate.dropCollection(DestiladoIngredientEntity.class)
                    .flatMap(unused -> Mono.just("Successfully Dropped Collection."));
        } Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return reactiveMongoTemplate.findAndRemove(query, DestiladoIngredientEntity.class)
                .switchIfEmpty(Mono.error(new NotFoundException("ID not found: " + id)))
                .flatMap(destiladoIngredientEntity ->
                        Mono.just("Successfully deleted " + destiladoIngredientEntity.getName()));
    }
}
