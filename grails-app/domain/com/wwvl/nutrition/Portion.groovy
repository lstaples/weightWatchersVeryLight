package com.wwvl.nutrition

class Portion {
	String description
	Integer calories

	static belongsTo = [food: Food]
	static hasMany = [recipeIngredients: Ingredient, logEntries:FoodLogEntry]

    static constraints = {
		description size: 1..255, blank: false
		calories min: 1
    }

	static mapping = {
		food column: 'foodID'
	}

	void setCalories(Integer _calories){
		calories = _calories
		recipeIngredients.each{Ingredient i -> i.reTotalCalories()}
		logEntries.each {FoodLogEntry l -> l.reTotalCalories()}
	}
}
