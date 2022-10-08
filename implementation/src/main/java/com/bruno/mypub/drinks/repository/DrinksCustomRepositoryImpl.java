package com.bruno.mypub.drinks.repository;

import com.bruno.mypub.common.exceptions.NotFoundException;
import com.bruno.mypub.common.query.DrinkQuery;
import com.bruno.mypub.drinks.repository.entities.DrinkEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@AllArgsConstructor
public class DrinksCustomRepositoryImpl implements DrinksCustomRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Flux<DrinkEntity> findDrink(DrinkQuery drinkQuery) {
        if (!Objects.equals(drinkQuery.getId(), "0")) {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(drinkQuery.getId()));
            return reactiveMongoTemplate.find(query, DrinkEntity.class)
                    .switchIfEmpty(Mono.error(new NotFoundException("Id not found: " + drinkQuery.getId())));
        } else if (!Objects.equals(drinkQuery.getName(), "")) {
            Query query = new Query();
            query.addCriteria(Criteria.where("name")
                    .regex(StringUtils.capitalize(drinkQuery.getName())));
            return reactiveMongoTemplate.find(query, DrinkEntity.class)
                    .switchIfEmpty(Mono.error(new NotFoundException("Name not found: " + drinkQuery.getName())));
        }
        return reactiveMongoTemplate.findAll(DrinkEntity.class);
    }

    @Override
    public Mono<String> customDelete(String id) {
        if (Objects.equals(id, "0")) {
            return reactiveMongoTemplate.dropCollection(DrinkEntity.class)
                    .flatMap(unused -> {
                        kafkaTemplate.send("drinks", "All drinks deleted");
                        return Mono.just("Successfully Dropped Collection.");
                    });
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return reactiveMongoTemplate.findAndRemove(query, DrinkEntity.class)
                .switchIfEmpty(Mono.error(new NotFoundException("ID not found: " + id)))
                .flatMap(drinkEntity -> {
                    kafkaTemplate.send("drinks", "Drink deleted: " + drinkEntity.getName());
                    return Mono.just("Successfully deleted " + drinkEntity.getName());
                });

    }
}
