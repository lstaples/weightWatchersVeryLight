package com.wwvl.nutrition

import com.wwvl.auth.User
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class FoodLogController {

	def sessionService
	def NutritionService

	def list(FoodLogListCommand fc) {
		if(fc.hasErrors()){
			response.status = 400
			render fc.errors as JSON
		}
		User user = sessionService.getLoggedInUser()
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
		User user = sessionService.getLoggedInUser()
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
		def dailySummaries = LogEntryDailySummary.computeAveragesFromSet(fc.startDate,fc.endDate,logs)
		def result = dailySummaries.values().collect{LogEntryDailySummary summary ->
			[calories: summary.calories
			,dateEaten: summary.dateEaten.format("MM/dd/yyyy")
			,fiveDayAvg: summary.fiveDayAvg.setScale(2,BigDecimal.ROUND_UP)
			,tenDayAvg: summary.tenDayAvg.setScale(2,BigDecimal.ROUND_UP)
		]}
		render result as JSON
	}

	def create(LogEntryCommand lc) {
		if(lc.hasErrors()){
			response.status = 400
			render lc.errors as JSON
		}

		User user = sessionService.getLoggedInUser()
		def recipe
		def portion
		LogEntry logEntry
		if(lc.recipeID){
			recipe = Recipe.findByUserAndId(user,lc.recipeID)
			logEntry =  new RecipeLogEntry(recipe: recipe)
		}
		if(lc.portionID){
			portion = Portion.executeQuery(' from Portion p where p.food.user = ? and p.id = ?', [user,lc.portionID])[0]
			logEntry =  new FoodLogEntry(portion: portion)
		}

		if(!portion && ! recipe){
			render (status: 400, text: "Valid portion or recipe must be supplied")
			return
		}

		NutritionService.saveLogEntry(logEntry,lc.dateEaten, lc.meal, lc.quantity,user)

		render logEntry as JSON

	}

	def show(Integer id) {
		User user = sessionService.getLoggedInUser()
		def logEntry= LogEntry.findByIdAndUser(id,user)
		if(!logEntry){
			render (status: 404, text: "Log Entry Not Found")
			return
		}

		render logEntry as JSON
	}

	def delete(Integer id) {
		User user = sessionService.getLoggedInUser()
		def logEntry = LogEntry.findByIdAndUser(id,user)
		if(!logEntry){
			render (status: 404, text: "Log Entry Not Found")
			return
		}
		NutritionService.deleteLog(logEntry)
		render(status: 204)
	}

	def update(LogEntryCommand lc) {
		if(lc.hasErrors()){
			response.status = 400
			render lc.errors as JSON
		}

		User user = sessionService.getLoggedInUser()
		def recipe
		def portion
		LogEntry logEntry = LogEntry.findByUserAndId(user,lc.id)
		if(!logEntry){
			response.status = 404
			render "Log Entry not found"
			return
		}
		if(logEntry.getType() == 'Recipe'){
			recipe = Recipe.findByUserAndId(user,lc.recipeID)
			if(!recipe){
				response.status = 400
				render "Recipe not found"
				return
			}
			logEntry.recipe = recipe
		}

		if(logEntry.getType() == 'Food'){
			portion = Portion.executeQuery(' from Portion p where p.food.user = ? and p.id = ?', [user,lc.portionID])[0]
			if(!portion){
				response.status = 400
				render "Portion not found"
				return
			}
			logEntry.portion = portion
		}

		NutritionService.saveLogEntry(logEntry,lc.dateEaten, lc.meal, lc.quantity,user)

		render(status: 204)

	}
}

class LogEntryCommand{
	Integer id
	Integer recipeID
	Long portionID
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

