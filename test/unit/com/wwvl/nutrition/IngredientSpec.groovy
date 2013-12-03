package com.wwvl.nutrition

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
class IngredientSpec extends Specification {

	Ingredient ingredient

    def setup() {
		Recipe recipe =  Mock()
		Portion portion =  Mock()
		ingredient = new Ingredient(recipe: recipe,portion: portion,quantity: 5,calories: 5)
    }

	void "test quantity must be positive int"(){
		when:
		ingredient.quantity = 0
		then:
		!ingredient.validate()

		when:
		ingredient.quantity = 1
		then:
		ingredient.validate()
	}

	void "test calories must be positive int or 0"(){
		when:
		ingredient.calories = -1
		then:
		!ingredient.validate()

		when:
		ingredient.calories = 1
		then:
		ingredient.validate()
	}
}
