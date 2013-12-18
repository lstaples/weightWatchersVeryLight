package com.wwvl.nutrition

import com.wwvl.auth.User
import spock.lang.*

class RecipeLogEntryIntSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

	void "test RecipeLogEntry mappings are correct"() {
		when: "a RecipeLogEntry is setup correctly"
		def user = new User(username: 'admin',password: 'myPasswrord')
		def recipe =  new Recipe(user: user, name: 'Cobb Salad', calories: 10, servings: 10).save()
		def meal = Meal.snacks
		def recipeLogEntry = new RecipeLogEntry(recipe: recipe, meal: meal, user: user,quantity: 10, dateEaten: new Date())
        recipeLogEntry.reTotalCalories()
		then: "it persists without errors"
		recipeLogEntry.save()
	}
}
