package com.bruno.mypub.drinks.repository;

import com.bruno.mypub.drinks.repository.entities.DrinkEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrinksRepository extends ReactiveMongoRepository<DrinkEntity, String>, DrinksCustomRepository {
}
