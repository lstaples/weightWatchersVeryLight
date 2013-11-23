package com.wwvl.nutrition

import com.wwvl.auth.User
import spock.lang.*

class FoodIntSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

	void "test the Food mapping is correct"() {
		when: "A new Food is correctly populated"
		def user = new User(username: 'admin', password: 'myPassword')
		def food = new Food(user: user,name: 'salad')

		then: "saving it persists with no errors"
		food.save()
	}
}
