package com.wwvl.nutrition


class RecipeLogEntry extends LogEntry {

	static belongsTo = [recipe:Recipe]

	static mapping = {
		recipe column: 'recipeID'
	}

	String getType(){
		"Recipe"
	}

	String getName(){
		recipe.name
	}

	void reTotalCalories(){
		if(!recipe || !recipe.servings)
			calories = 0
		else
			calories = Math.floor((recipe.calories ?: 0 / recipe.servings) * quantity)
	}
}
