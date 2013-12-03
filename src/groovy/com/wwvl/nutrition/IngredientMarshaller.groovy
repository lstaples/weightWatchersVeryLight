package com.wwvl.nutrition

import grails.converters.JSON

class IngredientMarshaller {
	void register(){
		JSON.registerObjectMarshaller(Ingredient){Ingredient ingredient->
			return [id: ingredient.id,quantity: ingredient.quantity, calories: ingredient.calories, portion: ingredient.portion]
		}
	}
}
