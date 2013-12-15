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
		portion.validate()
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

	Recipe createRecipe(User user, String name, Integer servings){
		def recipe =  new Recipe(user: user, name: name, servings: servings)
		recipe.save()
		recipe
	}

	void deleteRecipe(Recipe recipe){
		recipe.delete()
	}

	void updateRecipe(Recipe recipe, String name, Integer servings){
		recipe.name = name
		recipe.servings = servings
		recipe.validate()
	}

	Ingredient createIngredient(Recipe recipe, Portion portion, Integer quantity){
		def ingredient = new Ingredient(quantity: quantity)
		recipe.addToIngredients(ingredient)
		portion.addToRecipeIngredients(ingredient)
		ingredient.reTotalCalories()
		ingredient.validate()
		ingredient
	}

	void deleteIngredient(Ingredient ingredient){
		ingredient.recipe.calories -= ingredient.calories
		ingredient.delete()
	}

	void updateIngredient(Ingredient ingredient, Integer quantity){
		ingredient.quantity = quantity
		ingredient.reTotalCalories()
		ingredient.validate()
	}

	void deleteLog(LogEntry logEntry){
		logEntry.delete()
	}

	void saveLogEntry(LogEntry logEntry, Date dateEaten, Meal meal, Integer quantity,User user){
		logEntry.dateEaten = dateEaten
		logEntry.meal = meal
		logEntry.quantity = quantity
		logEntry.user = user
		logEntry.reTotalCalories()
		logEntry.save()
	}

}
