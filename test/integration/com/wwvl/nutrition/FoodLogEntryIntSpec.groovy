package com.wwvl.nutrition

import com.wwvl.auth.User
import spock.lang.*

class FoodLogEntryIntSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test FoodLogEntry mappings are correct"() {
		when: "a FoodLog Entry is setup correctly"
		def user = new User(username: 'admin',password: 'myPasswrord')
		def food = new Food(user: user, name: 'salad').save()
		def portion = new Portion(food: food, description: 'bowl', calories: 10).save()
		def meal = Meal.snacks
		def foodLogentry = new FoodLogEntry(portion: portion, meal: meal, user: user,quantity: 10, calories: 10, dateEaten: new Date())

		then: "it persists without errors"
		foodLogentry.save()
    }
}
