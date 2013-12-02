package com.wwvl.nutrition

import grails.converters.JSON

class FoodMarshaller {
	void register(){
		JSON.registerObjectMarshaller(Food){Food food ->
			return [id: food.id,name: food.name ,portions:food.portions]
		}
	}
}
