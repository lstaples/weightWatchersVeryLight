package com.wwvl

import com.wwvl.auth.*
import com.wwvl.nutrition.*


class testDataCreator {

	static def create(){
		def user = new User (username: 'user',password: 'password').save()

		def endUserRole = new Role(authority: 'ROLE_USER').save()
		def adminRole = new Role(authority: 'ROLE_ADMIN').save()

		new UserRole(user: user, role: endUserRole).save()
		new UserRole(user: user, role: adminRole).save()

		def bread = new Food(name: 'bread',user: user).save()
		def sliceOfBread = new Portion(description: 'slcie' ,food: bread, calories: 10).save()

		def salad = new Food(name: 'salad', user: user).save()
		def bowlOfSalad = new Portion(description: 'bowl' ,food: salad, calories: 20).save()

		def foodLogEntry = new FoodLogEntry(portion: bowlOfSalad, dateEaten: new Date(), quantity: 1, user: user,  meal: Meal.lunch)
		foodLogEntry.reTotalCalories()
		foodLogEntry.save()

		def chickenStock = new Food(name: 'Chicken Stock', user: user).save()
		def cupOfChickenStock = new Portion(description: 'cup' ,food: chickenStock, calories: 50).save()

		def chickenSoup = new Recipe(user: user, servings: 4, name: 'chicken soup').save()

		def soupBase = new Ingredient(quantity: 3, portion: cupOfChickenStock, recipe: chickenSoup)
		soupBase.reTotalCalories()
		soupBase.save()

		def recipeLogEntry = new RecipeLogEntry(recipe: chickenSoup, dateEaten: new Date(), quantity: 1, user: user, meal: Meal.dinner)
		recipeLogEntry.reTotalCalories()

		recipeLogEntry.save()

		new NutritionTestData(user: user, bread: bread, sliceOfBread: sliceOfBread, salad: salad
		,bowlOfSalad: bowlOfSalad,foodLogEntry: foodLogEntry, chickenStock: chickenStock
		,cupOfChickenStock:cupOfChickenStock,chickenSoup:chickenSoup,soupBase:soupBase
		,recipeLogEntry:recipeLogEntry)
	}

}

class NutritionTestData{
	User user
	Food bread
	Portion sliceOfBread
	Food salad
	Portion bowlOfSalad
	FoodLogEntry foodLogEntry
	Food chickenStock
	Portion cupOfChickenStock
	Recipe chickenSoup
	Ingredient soupBase
	RecipeLogEntry recipeLogEntry
}
