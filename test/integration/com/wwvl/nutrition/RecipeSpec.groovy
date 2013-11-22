package com.wwvl.nutrition

import com.wwvl.auth.User
import spock.lang.*

class RecipeSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

	void "test Recipe mappings are correct"() {
		when: "a Recipe is setup correctly"
		def user = new User(username: 'admin',password: 'myPasswrord')
		def recipe =  new Recipe(user: user, name: 'Cobb Salad', calories: 10, servings: 10)

		then: "it persists without errors"
		recipe.save()
	}
}
