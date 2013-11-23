package com.wwvl.nutrition

import com.wwvl.auth.User
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class RecipeSpec extends Specification {
    def setup() {
    }

    def cleanup() {
    }

	void "test name is unique to the recipe"() {
		User user = Mock()
		User user2 = Mock()
		def existingRecipe = new Recipe(user: user, name: 'cobb salad',calories: 10,servings: 2)
		mockForConstraintsTests(Recipe,[existingRecipe])

		when: "A user has a recipe with a given name"
		Recipe recipe= new Recipe(user: user, name: 'cobb salad',calories: 10,servings: 2)

		then: "they cannot have another"
		!recipe.validate()
		recipe.errors["name"] == "unique"

		when: "the user is changed"
		recipe.user = user2

		then: "validation passes"
		recipe.validate()
	}

	void "test name length" (){
		User user = Mock()

		when: "the string is too short"
		def recipe= new Recipe(user: user, name: '',calories: 10,servings: 2)

		then: "validation fails"
		!recipe.validate()

		when: "the string is too long"
		def sb = new StringBuilder()
		(1..260).each {sb << 'a'}
		recipe.name= sb.toString()

		then: "validation fails"
		!recipe.validate()

		when: "the string is just right"
		recipe.name= 'cobb salad'

		then:"validation passes"
		recipe.validate()
	}

	void "test calories must be positive int"(){
		User user = Mock()
		def recipe= new Recipe(user: user, name: 'cobb salad',calories: 10,servings: 2)

		when:
		recipe.calories = 0
		then:
		!recipe.validate()

		when:
		recipe.calories = 1
		then:
		recipe.validate()
	}

	void "test servings must be positive int"(){
		User user = Mock()
		def recipe= new Recipe(user: user, name: 'cobb salad',calories: 10,servings: 2)

		when:
		recipe.servings = 0
		then:
		!recipe.validate()

		when:
		recipe.servings = 1
		then:
		recipe.validate()
	}
}
