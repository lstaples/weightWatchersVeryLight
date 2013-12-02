package com.wwvl.weight

import grails.transaction.Transactional

@Transactional
class WeightLogService {

    def createLog(LogEntry logEntry) {
		logEntry.save()
    }

	def updateLog(LogEntry logEntry) {
		logEntry.save()
	}

	def deleteLog(LogEntry logEntry){
		logEntry.delete()
	}
}
