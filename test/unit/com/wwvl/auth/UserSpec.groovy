package com.wwvl.auth

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
class UserSpec extends Specification {

	User user
    def setup() {
		user =  new User(username: 'admin', password: 'test')
    }

    def cleanup() {
    }

	void "test user must be unique"() {
		when: "a user with a given username exists"
		def existingUser = new User(username: 'admin', password: 'test')
		mockForConstraintsTests(User,[existingUser])

		then: "another one cannot"
		! user.validate()

		when:"the user name changes"
		user.username = 'user'

		then: "validation passes"
		user.validate()
	}

	void "test password cannot be blank"(){
		when:
		user.password = ''

		then:
		!user.validate()

		when:
		user.password = 'test'

		then:
		user.validate()
	}

	void "test user name length" (){
		when: "the string is too short"
		user.username = ''

		then: "validation fails"
		!user.validate()

		when: "the string is too long"
		def sb = new StringBuilder()
		(1..101).each {sb << 'a'}
		user.username= sb.toString()

		then: "validation fails"
		!user.validate()

		when: "the string is just right"
		user.username= 'admin'

		then:"validation passes"
		user.validate()
	}
}
