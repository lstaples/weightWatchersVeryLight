package com.wwvl.nutrition

import grails.converters.JSON

class MealMarshaller {
	void register(){
		JSON.registerObjectMarshaller(Meal){Meal meal->
			return [name: meal.toString()]
		}
	}
}
