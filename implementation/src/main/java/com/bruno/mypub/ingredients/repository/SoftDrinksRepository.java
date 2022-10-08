package com.bruno.mypub.ingredients.repository;

import com.bruno.mypub.ingredients.model.entities.SoftDrinkIngredientEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftDrinksRepository extends ReactiveMongoRepository<SoftDrinkIngredientEntity, String>, SoftDrinksCustomRepository {

}
