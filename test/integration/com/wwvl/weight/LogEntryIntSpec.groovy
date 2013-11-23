package com.wwvl.weight

import com.wwvl.auth.User
import spock.lang.*

class LogEntryIntSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test weight LogEntry mappings are correct"() {
		when: "A weight Log entry is correct"
		def user = new User(username: 'admin',password: 'myPasswrord')
		def logEntry = new LogEntry(user: user, weight: 185, dateWeighed: new Date())
		then:"it persists without errors"
    }
}
