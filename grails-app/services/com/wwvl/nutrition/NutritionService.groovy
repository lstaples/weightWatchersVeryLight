package com.wwvl.nutrition

import grails.transaction.Transactional
import com.wwvl.auth.User

@Transactional
class NutritionService {

    Food createFood(String foodName, User user, String portionDescription, Integer calories) {
		def food = new Food(name: foodName, user: user)
		def portion = new Portion(description: portionDescription, calories: calories)
		food.addToPortions(portion)
		food.save()
		food

    }

	void deleteFood(Food food){
		food.delete()
	}

	void updateFood(Food food, String name){
		food.name = name
		food.validate()
	}
}
