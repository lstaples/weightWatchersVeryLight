package com.wwvl.nutrition

import com.wwvl.auth.User
import spock.lang.*

class IngredientIntSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

	void "test Ingredient mappings are correct"() {
		when: "a Ingredient is setup correctly"
		def user = new User(username: 'admin',password: 'myPasswrord')
		def food = new Food(user: user, name: 'salad') .save()
		def portion = new Portion(food: food, description: 'bowl', calories: 10).save()
		def recipe =  new Recipe(user: user, name: 'Cobb Salad', calories: 10, servings: 10).save()
		def ingredient = new Ingredient(recipe: recipe, portion: portion, calories: 1, quantity: 5)

		then: "it persists without errors"
		ingredient.save()
	}}
