package com.wwvl.nutrition

import grails.converters.JSON
class PortionMarshaller {

	void register(){
		JSON.registerObjectMarshaller(Portion){Portion portion ->
			return [id: portion.id,description: portion.description,calories: portion.calories, foodID: portion.food.id]
		}
	}

}
