package com.wwvl.weight

import com.wwvl.auth.User
import grails.converters.JSON

class WeightLogController {

	def springSecurityService
	def WeightLogService

    def list(ListCommand lc) {
		User user = User.get(springSecurityService.principal.id)
		lc.resolveDates(user)
		def logEntries = LogEntry.findAllByUserAndDateWeighedGreaterThanEqualsAndDateWeighedLessThanEquals(user,lc.startDate,lc.endDate)
		render logEntries as JSON
	}
	def create(LogEntryCommand logEntryCommand) {
		if(logEntryCommand.hasErrors()){
			response.status = 400
			render logEntryCommand.errors as JSON
		}

		def logEntry = new LogEntry(logEntryCommand.getSimpleProperties())

		User user = User.load(springSecurityService.principal.id)
		logEntry.user = user

		if(!WeightLogService.createLog(logEntry)){
			response.status = 500
			render logEntry.errors as JSON
		}

		def result = [logEntryID : logEntry.id]
		render result as JSON
	}

	def show(Long id){
		User user = User.load(springSecurityService.principal.id)
		def logEntry = LogEntry.findByIdAndUser(id,user)
		if(!logEntry)
			response.sendError(404,"Weight log entry not found")

		render logEntry as JSON
	}

	def delete(Long id){
		User user = User.load(springSecurityService.principal.id)
		def logEntry = LogEntry.findByIdAndUser(id,user)
		if(!logEntry)
			response.sendError(404,"Weight log entry not found")
		WeightLogService.deleteLog(logEntry)
		render(status: 204)
	}

	def update(LogEntryCommand logEntryCommand){

		if(logEntryCommand.hasErrors()){
			response.status = 400
			render logEntryCommand.errors as JSON
		}
		def user = User.load(springSecurityService.principal.id)
		def logEntry = LogEntry.findByIdAndUser(logEntryCommand.id,user)
		if(!logEntry)
			response.sendError(404,"Weight log entry not found")

		logEntry.properties =  logEntryCommand.getSimpleProperties()
		if(!WeightLogService.updateLog(logEntry)){
			response.status = 500
			render logEntry.errors as JSON
		}
		render(status: 204)
	}

}

class LogEntryCommand{
	Long id
	Date dateWeighed
	Integer weight

	static constraints = {
		importFrom LogEntry
		id nullable: true
	}

	def getSimpleProperties(){
		[dateWeighed: dateWeighed, weight:weight]
	}
}

class ListCommand{
	Date startDate
	Date endDate

	void resolveDates(User user){
		if(!startDate)
			startDate = user.dateCreated.clearTime()
		if(!endDate){
			endDate= LogEntry.createCriteria().get{
				eq("user",user)
			   projections{
				   max "dateWeighed"
			   }
		   }   as Date
		}
	}

}


