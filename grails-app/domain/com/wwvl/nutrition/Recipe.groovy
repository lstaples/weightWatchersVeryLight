package com.wwvl.nutrition

import com.wwvl.auth.User
class Recipe {

	String name
	Integer calories = 0
	Integer servings

	static hasMany = [ingredients: Ingredient, logEntries: RecipeLogEntry]
	static belongsTo = [user:User]

    static constraints = {
		calories min: 0
		servings  min: 1
		name unique: ['user'] , blank: false, size: 1..255
    }

	static mapping = {
		user column: 'userID'
	}

	void setCalories(Integer _calories){
		calories = _calories
		logEntries.each {LogEntry l -> l.reTotalCalories()}
	}

	void setServings(Integer _servings){
		servings = _servings
		logEntries.each {LogEntry l -> l.reTotalCalories()}
	}

}
