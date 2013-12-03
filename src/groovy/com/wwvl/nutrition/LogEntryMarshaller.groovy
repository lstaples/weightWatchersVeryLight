package com.wwvl.nutrition

import grails.converters.JSON


class LogEntryMarshaller {
	void register(){
		JSON.registerObjectMarshaller(LogEntry){LogEntry logEntry->
			return [id: logEntry.id
					,name: logEntry.getName()
					,type:logEntry.getType()
					,dateEaten: logEntry.dateEaten.format("MM/dd/yyyy")
					,quantity: logEntry.quantity
					,calories: logEntry.calories
					,meal: logEntry.meal.toString()
			]
		}
	}
}
