package com.wwvl.nutrition

class FoodLogEntry extends LogEntry{

	static belongsTo = [portion: Portion]

	static mapping = {
		discriminator "Food"
	}
}
