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

	Portion createPortion(Food food, String description, Integer calories){
		def portion = new Portion(description: description, calories: calories)
		food.addToPortions(portion)
		portion
	}

	void deletePortion(Portion portion){
		portion.delete()
	}

	void updatePortion(Portion portion, String description, Integer calories) {
		portion.description = description
		portion.calories = calories
		portion.validate()
	}
}
