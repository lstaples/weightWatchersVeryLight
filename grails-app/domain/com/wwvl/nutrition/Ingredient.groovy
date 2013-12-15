package com.wwvl.nutrition

class Ingredient {
	Integer calories = 0
	Integer quantity

	static belongsTo = [recipe: Recipe, portion:Portion]

	static constraints = {
		calories min: 0
		quantity min: 1
    }

	static mapping = {
		recipe column: 'recipeID'
		portion column: 'portionID'
	}

	void reTotalCalories(){
		def currentCalories = calories
		calories = (portion.calories ?: 0) * quantity ?:0
		if(recipe)
			recipe.calories +=  calories - currentCalories
	}
}
