package com.wwvl.auth

import spock.lang.*

class UserSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

	void "test the User mapping is correct"() {
		when: "A new user is correctly populated"
		def admin = new User(username: 'admin', password: 'myPassword')

		then: "saving it persists with no errors"
		admin.save()
	}
}
