package com.wwvl.nutrition

import com.wwvl.auth.User
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
class RecipeLogEntrySpec extends Specification {
	RecipeLogEntry recipeLogEntry
    def setup() {
		User user = Mock()
		Recipe recipe = Mock()
		recipeLogEntry =  new RecipeLogEntry(user: user, recipe: recipe, dateEaten: new Date(), meal: Meal.snacks, quantity: 1, calories: 10)
    }

    def cleanup() {
    }

	void "test setting dateEaten clears out the time stamp"() {
		when: "the dateEaten property is set"
		Calendar cal  = Calendar.getInstance()
		cal.set(2013,Calendar.JANUARY,1,7,12,13)
		recipeLogEntry.dateEaten = cal.getTime()

		then: "its time stamp is cleared"
		recipeLogEntry.dateEaten.format('MM/dd/yyyy hh:mm:ss') == '01/01/2013 12:00:00'
	}

	void "test quantity must be positive int"(){
		when:
		recipeLogEntry.quantity = 0
		then:
		!recipeLogEntry.validate()

		when:
		recipeLogEntry.quantity = 1
		then:
		recipeLogEntry.validate()
	}

	void "test calories must be positive int"(){
		when:
		recipeLogEntry.calories = 0
		then:
		!recipeLogEntry.validate()

		when:
		recipeLogEntry.calories = 1
		then:
		recipeLogEntry.validate()
	}
}
