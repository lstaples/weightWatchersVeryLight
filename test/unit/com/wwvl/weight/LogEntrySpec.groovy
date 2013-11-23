package com.wwvl.weight

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.wwvl.auth.User

@TestMixin(GrailsUnitTestMixin)
class LogEntrySpec extends Specification {

	LogEntry logEntry

    def setup() {
		logEntry = new LogEntry()
    }

    def cleanup() {
    }

    void "test setting dateWeighed clears out the time stamp"() {
		when: "the dateWeighed property is set"
		Calendar cal  = Calendar.getInstance()
		cal.set(2013,Calendar.JANUARY,1,7,12,13)
		logEntry.dateWeighed = cal.getTime()

		then: "its time stamp is cleared"
		logEntry.dateWeighed.format('MM/dd/yyyy hh:mm:ss') == '01/01/2013 12:00:00'
    }

	void "test dateWeighed must be unique per user"() {
		when: "A user has a weight in on a given day"
		User user1 = Mock()
		User user2 = Mock()
		def dateWeighed = new Date()
		def existingLogEntry =  new LogEntry(user: user1,weight: 150,dateWeighed: dateWeighed)
		mockForConstraintsTests(LogEntry,[existingLogEntry])

		logEntry.dateWeighed = dateWeighed
		logEntry.weight = 175
		logEntry.user = user1

		then: "they cannot have another"
		!logEntry.validate()
		logEntry.errors["dateWeighed"] == "unique"

		when: "the user is changed"
		logEntry.user = user2

		then: "validation passes"
		logEntry.validate()
	}
}
