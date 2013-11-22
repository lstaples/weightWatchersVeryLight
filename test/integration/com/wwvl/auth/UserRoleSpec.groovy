package com.wwvl.auth

import spock.lang.*

class UserRoleSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

	void "test the UserRole mapping is correct"() {
		when: "A new UserRole is correctly populated"
		def role = new Role(authority: 'Admin').save()
		def admin = new User(username: 'admin', password: 'myPassword').save()
		def userRole = new UserRole(user: admin,role: role)

		then: "saving it persists with no errors"
		userRole.save()
	}
}
