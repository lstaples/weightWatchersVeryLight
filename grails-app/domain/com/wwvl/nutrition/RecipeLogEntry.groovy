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
		if(!recipe)
			calories = 0
		else
			calories = recipe.caloriesPerServing() * quantity
	}

}
