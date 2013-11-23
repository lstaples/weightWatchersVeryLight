package com.wwvl.nutrition

import com.wwvl.auth.User
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
class FoodSpec extends Specification {
    def setup() {
    }

    def cleanup() {
    }

    void "test name is unique to the user"() {
		User user = Mock()
		def existingFood =   new Food(user: user, name: 'pizza')
		mockForConstraintsTests(Food,[existingFood])

		when: "A user has a food with a given name"
		Food food = new Food(user: user, name: 'pizza')

		then: "they cannot have another"
		!food.validate()
		food.errors["name"] == "unique"

		when: "the name is changed"
		food.name = 'corn'

		then: "validation passes"
		food.validate()
    }

	void "test  name length" (){
		User user = Mock()

		when: "the string is too short"
		def food = new Food(user: user, name: '')

		then: "validation fails"
		!food.validate()

		when: "the string is too long"
		def sb = new StringBuilder()
		(1..260).each {sb << 'a'}
		food.name= sb.toString()

		then: "validation fails"
		!food.validate()

		when: "the string is just right"
		food.name= 'corn'

		then:"validation passes"
		food.validate()
	}
}
