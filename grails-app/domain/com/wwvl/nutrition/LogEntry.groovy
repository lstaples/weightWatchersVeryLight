package com.wwvl.nutrition

import com.wwvl.auth.User
abstract class LogEntry {
	Date dateEaten
	Meal meal
	Integer quantity
	Integer calories

	static belongsTo = [user:User]

    static constraints = {
		quantity min: 1
		calories min: 1
		dateEaten unique: true
    }

	static mapping = {
		autoImport false
		tablePerHierarchy false
		user column: 'userID'
		table 'calorieIntakeLogEntry'
	}
}
