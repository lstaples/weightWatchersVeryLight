package com.wwvl.weight

import com.wwvl.auth.User
class LogEntry {
	Date dateWeighed
	Integer weight

	static belongsTo = [user: User]

    static constraints = {
		dateWeighed unique: ['user']
    }
	static mapping = {
		table 'weightLogEntry'
		user column: 'userID'
	}

	void setDateWeighed(Date date){
		date.clearTime()
		dateWeighed = date
	}
}
