package com.wwvl.nutrition

class Portion {
	String description
	Integer calories

	static belongsTo = [food: Food]
	static hasMany = [recipeIngredients: Ingredient, logEntries:FoodLogEntry]

    static constraints = {
		description size: 1..255
		calories min: 1
    }

	static mapping = {
		food column: 'foodID'
	}
}
