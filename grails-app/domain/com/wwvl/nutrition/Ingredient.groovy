package com.wwvl.nutrition

class Ingredient {
	Integer calories
	Integer quantity

	static belongsTo = [recipe: Recipe, portion:Portion]

	static constraints = {
		calories min: 1
		quantity min: 1
    }

	static mapping = {
		recipe column: 'recipeID'
		portion column: 'portionID'
	}
}
