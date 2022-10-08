package com.bruno.mypub.ingredients.repository;

import com.bruno.mypub.ingredients.model.entities.FruitIngredientEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FruitsRepository extends ReactiveMongoRepository<FruitIngredientEntity, String>, FruitCustomRepository {
}
