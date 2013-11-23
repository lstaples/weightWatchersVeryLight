package com.wwvl.nutrition

import com.wwvl.auth.User
import spock.lang.*

class PortionIntSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

	void "test Portion mappings are correct"() {
		when: "a Portion is setup correctly"
		def user = new User(username: 'admin',password: 'myPasswrord')
		def food = new Food(user: user, name: 'salad').save()
		def portion = new Portion(food: food, description: 'bowl', calories: 10).save()

		then: "it persists without errors"
		portion.save()
	}
}
