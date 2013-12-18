package com.wwvl.nutrition

import com.wwvl.auth.User
abstract class LogEntry {
	Date dateEaten
	Meal meal
	Integer quantity
	Integer calories = 0

	static belongsTo = [user:User]

    static constraints = {
		quantity min: 1
		calories min: 0
    }

	static mapping = {
		autoImport false
		tablePerHierarchy false
		user column: 'userID'
		table 'calorieIntakeLogEntry'
	}

	void setDateEaten(Date date){
		date.clearTime()
		dateEaten = date
	}

	void setQuantity(Integer _quantity){
		quantity = _quantity
		reTotalCalories()
	}

	abstract String getType()
	abstract String getName()
	abstract void reTotalCalories()
}
