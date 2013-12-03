package com.wwvl.nutrition

import com.wwvl.auth.User
import grails.converters.JSON

class FoodLogController {

	def springSecurityService
	def NutritionService

	def list(FoodLogListCommand fc) {
		if(fc.hasErrors()){
			response.status = 400
			render fc.errors as JSON
		}
		User user = User.load(springSecurityService.principal.id)
		def logs
		if(fc.meal){
			try{
				def meal = fc.meal as Meal
			}
			catch (IllegalArgumentException e){
				render(status: 400, text: "Invalid meal")
				return
			}
			logs = LogEntry.findAllByUserAndDateEatenAndMeal(user,fc.date,fc.meal,[sort: "meal"])
		}
		else
			logs = LogEntry.findAllByUserAndDateEaten(user,fc.date,[sort: "meal"])

		render logs as JSON
	}


	def summary(FoodLogSummaryCommand fc) {
		if(fc.hasErrors()){
			response.status = 400
			render fc.errors as JSON
		}
		User user = User.load(springSecurityService.principal.id)
		fc.resolveDates(user)
		def logs = LogEntry.createCriteria().list{
			eq("user",user)
			ge("dateEaten",fc.startDate)
			le("dateEaten",fc.endDate)
			projections{
			   sum "calories"
			   groupProperty "dateEaten"
			}
			order("dateEaten","asc")
		}
		LinkedHashMap<LogEntryDailySummary> dailySummaries = LogEntryDailySummary.computeAveragesFromSet(fc.startDate,fc.endDate,logs)
		def result = dailySummaries.values().collect{LogEntryDailySummary summary ->
			[calories: summary.calories
			,dateEaten: summary.dateEaten.format("MM/dd/yyyy")
			,fiveDayAvg: summary.fiveDayAvg.setScale(2,BigDecimal.ROUND_UP)
			,tenDayAvg: summary.tenDayAvg.setScale(2,BigDecimal.ROUND_UP)
		]}
		render result as JSON
	}

	def create() {}

	def show() {
	}

	def delete() {}

	def update() {}
}

class LogEntryCommand{
	Integer id
	Integer recipeID
	Integer portionID
	Date dateEaten
	Meal meal
	Integer quantity

	static constraints = {
		importFrom LogEntry
		id nullable: true
		recipeID nullable: true
		portionID nullable: true
	}

}

class FoodLogListCommand{
	Date date
	String meal

	static constraints = {
		meal nullable: true
	}
}

class FoodLogSummaryCommand{
	Date startDate
	Date endDate

	void resolveDates(User user){
		if(!startDate)
			startDate = user.dateCreated.clearTime()
		if(!endDate){
			endDate= LogEntry.createCriteria().get{
				eq("user",user)
				projections{
					max "dateEaten"
				}
			}   as Date
		}
	}
}

