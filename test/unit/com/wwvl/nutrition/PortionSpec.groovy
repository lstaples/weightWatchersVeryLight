package com.wwvl.nutrition

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
class PortionSpec extends Specification {
	Portion portion

    def setup() {
		Food food = Mock()
		portion =  new Portion(food: food,description: 'tbs',calories: 25)
    }

    def cleanup() {
    }

	void "test calories must be positive int"(){
		when:
		portion.calories = 0
		then:
		!portion.validate()

		when:
		portion.calories = 1
		then:
		portion.validate()
	}

	void "test name length" (){

		when: "the string is too short"
		portion.description = ''

		then: "validation fails"
		!portion.validate()

		when: "the string is too long"
		def sb = new StringBuilder()
		(1..260).each {sb << 'a'}
		portion.description= sb.toString()

		then: "validation fails"
		!portion.validate()

		when: "the string is just right"
		portion.description= 'cup'

		then:"validation passes"
		portion.validate()
	}
}
