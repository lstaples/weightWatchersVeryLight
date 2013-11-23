package com.wwvl.auth

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

@TestMixin(GrailsUnitTestMixin)
class RoleSpec extends Specification {

	Role role

	def setup(){
		role = new Role()
	}

	def cleanup() {
	}

	void "test role must be unique"() {
		when: "a role with a given authority exists"
		def existingRole = new Role(authority: 'admin')
		mockForConstraintsTests(Role,[existingRole])
		role.authority = 'admin'

		then: "another one cannot"
		! role.validate()

		when:"the authority changes"
		role.authority = 'user'

		then: "validation passes"
		role.validate()
	}

	void "test authority length" (){
		when: "the string is too short"
		role.authority = ''

		then: "validation fails"
		!role.validate()

		when: "the string is too long"
		def sb = new StringBuilder()
		(1..101).each {sb << 'a'}
		role.authority = sb.toString()

		then: "validation fails"
		!role.validate()

		when: "the string is just right"
		role.authority = 'admin'

		then:"validation passes"
		role.validate()
	}
}

import spock.lang.Specification
