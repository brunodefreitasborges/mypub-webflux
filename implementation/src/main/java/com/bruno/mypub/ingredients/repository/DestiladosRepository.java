package com.bruno.mypub.ingredients.repository;

import com.bruno.mypub.ingredients.model.entities.DestiladoIngredientEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestiladosRepository extends ReactiveMongoRepository<DestiladoIngredientEntity, String>, DestiladosCustomRepository {


}
