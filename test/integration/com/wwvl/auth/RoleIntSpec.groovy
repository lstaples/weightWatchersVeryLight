package com.wwvl.auth

import spock.lang.*

class RoleIntSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test the role mapping is correct"() {
		when: "A new role is correctly populated"
		def role = new Role(authority: 'Admin')

		then: "saving it persists with no errors"
		role.save()
    }
}
