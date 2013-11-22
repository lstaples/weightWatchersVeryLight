package com.wwvl.weight

import com.wwvl.auth.User
class LogEntry {
	Date dateWeighed
	Integer weight

	static belongsTo = [user: User]

    static constraints = {
		dateWeighed unique: true
    }
	static mapping = {
		table 'weightLogEntry'
		user column: 'userID'
	}
}
