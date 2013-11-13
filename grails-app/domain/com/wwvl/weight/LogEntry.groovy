package com.wwvl.weight

class LogEntry {
	Date dateWeighed
	Integer weight

    static constraints = {
		dateWeighed unique: true
    }
	static mapping = {
		table 'weightLogEntry'
	}
}
