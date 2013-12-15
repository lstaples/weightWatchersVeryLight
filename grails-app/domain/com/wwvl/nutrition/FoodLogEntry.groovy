package com.wwvl.nutrition

class FoodLogEntry extends LogEntry{

	static belongsTo = [portion: Portion]

	static mapping = {
		portion column: 'portionID'
	}

	String getType(){
		"Food"
	}

	String getName(){
		portion.food.name
	}

	void reTotalCalories(){
		calories = (portion?.calories ?: 0) * quantity
	}
}
