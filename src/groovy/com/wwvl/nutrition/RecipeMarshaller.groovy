package com.wwvl.nutrition

import grails.converters.JSON

class RecipeMarshaller {
	void register(){
		JSON.registerObjectMarshaller(Recipe){Recipe recipe->
			return [id: recipe.id,name: recipe.name , servings: recipe.servings, calories: recipe.calories, ingredients: recipe.ingredients]
		}
	}

}
