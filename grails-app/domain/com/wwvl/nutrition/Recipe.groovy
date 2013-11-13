package com.wwvl.nutrition

class Recipe {

	String name
	Integer calories
	Integer servings

	static hasMany = [ingredients: Ingredient, logEntries: RecipeLogEntry]

    static constraints = {
		calories min: 1
		servings  min: 1
		name unique: true
    }

}
