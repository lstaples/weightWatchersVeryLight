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
}
