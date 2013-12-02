package com.wwvl.weight

import grails.converters.JSON


class LogEntryMarchaller {
	void register(){
		JSON.registerObjectMarshaller(LogEntry){LogEntry logEntry ->
		return [id: logEntry.id,dateWeighed: logEntry.dateWeighed.format('MM/dd/yyyy'),weight:logEntry.weight]
		}
	}
}
