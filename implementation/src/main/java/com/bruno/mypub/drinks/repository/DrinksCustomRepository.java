package com.bruno.mypub.drinks.repository;

import com.bruno.mypub.common.query.DrinkQuery;
import com.bruno.mypub.drinks.repository.entities.DrinkEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DrinksCustomRepository {

    Flux<DrinkEntity> findDrink(DrinkQuery query);
    Mono<String> customDelete(String id);

}
