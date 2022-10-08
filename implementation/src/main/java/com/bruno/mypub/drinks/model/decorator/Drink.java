package com.bruno.mypub.drinks.model.decorator;

import com.bruno.mypub.drinks.repository.entities.DrinkEntity;

public interface Drink {

    DrinkEntity assemble(DrinkEntity drinkEntity);

}
