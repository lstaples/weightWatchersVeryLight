package com.wwvl.nutrition

import com.wwvl.auth.User
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class FoodController {

	def sessionService
	def NutritionService

    def listFood() {
		User user = sessionService.getLoggedInUser()
		render Food.findAllByUser(user) as JSON
	}

	def showFood(Integer id){
		User user = sessionService.getLoggedInUser()
		def food= Food.findByIdAndUser(id,user)
		if(!food){
			render (status: 404, text: "Food Not Found")
			return
		}

		render food as JSON
	}

	def createFood(FoodCommand fc) {

		if(fc.hasErrors()){
			response.status = 400
			render fc.errors as JSON
		}

		if(fc.portion.hasErrors()){
			response.status = 400
			render fc.portion.errors as JSON
		}

		User user = sessionService.getLoggedInUser()
		def food = NutritionService.createFood(fc.name,user, fc.portion.description, fc.portion.calories)

		if(food.hasErrors()){
			response.status = 500
			render food.errors as JSON
		}

		if(food.portions[0].hasErrors()){
			response.status = 500
			render food.portions[0].errors as JSON
		}
		render food as JSON
	}

	def deleteFood(Integer id){
		User user = sessionService.getLoggedInUser()
		def food = Food.findByIdAndUser(id,user)
		if(!food){
			render (status: 404, text: "Food Not Found")
			return
		}

		def  c = Portion.createCriteria()
		def portions = c.list{
			eq("food",food)
			isNotEmpty("recipeIngredients")
		}
		if(portions) {
			render (status: 400, text: "Food cannot be deleted because it belongs to at least 1 recipe")
			return
		}

		c = Portion.createCriteria()
		portions = c.list{
			eq("food",food)
			isNotEmpty("logEntries")
		}

		if(portions) {
			render (status: 400, text: "Food cannot be deleted because it belongs to at least 1 log entry")
			return
		}
		NutritionService.deleteFood(food);
		render (status: 204)
	}

	def updateFood(FoodCommand fc){
		if(fc.hasErrors()){
			response.status = 400
			render fc.errors as JSON
		}
		def user = sessionService.getLoggedInUser()

		def food = Food.findByIdAndUser(fc.id,user)
		if(!food){
			render (status: 404, text: "Food Not Found")
			return
		}

		NutritionService.updateFood(food, fc.name)
		if(food.hasErrors()){
			response.status = 500
			render food.errors as JSON
		}
		render(status: 204)
	}

	def createPortion(PortionCommand pc){
		if(pc.hasErrors()){
			response.status = 400
			render pc.errors as JSON
		}

		def food = Food.get(pc.foodID)
		if(!food){
			render (status: 400, text: "Food Not Found")
			return
		}

		def portion = NutritionService.createPortion(food, pc.description, pc.calories)
		if(portion.hasErrors()){
			response.status = 500
			render portion.errors as JSON
		}

		render portion as JSON
	}

	def deletePortion(Integer id){
		def portion = Portion.get(id)
		if(!portion){
			render (status: 404, text: "Portion Not Found")
			return
		}

		if(portion.recipeIngredients) {
			render (status: 400, text: "Portion cannot be deleted because it belongs to at least 1 recipe")
			return
		}

		if(portion.logEntries) {
			render (status: 400, text: "Portion cannot be deleted because it belongs to at least 1 log entry")
			return
		}
		NutritionService.deletePortion(portion);
		render (status: 204)
	}

	def updatePortion(PortionCommand pc){
		if(pc.hasErrors()){
			response.status = 400
			render pc.errors as JSON
		}

		def portion= Portion.get(pc.id)
		if(!portion){
			render (status: 404, text: "Portion Not Found")
			return
		}

		NutritionService.updatePortion(portion, pc.description, pc.calories)
		if(portion.hasErrors()){
			response.status = 500
			render portion.errors as JSON
		}
		render(status: 204)
	}

	def listMeals(){
		render Meal.values() as JSON
	}

}

class FoodCommand {
	Integer id
	String name
	PortionCommand portion

	static constraints = {
		importFrom Food
		id nullable: true
	}

}

class PortionCommand{
	Integer id
	String description
	Integer calories
	Integer foodID

	static constraints = {
		importFrom Portion
		id nullable: true
		foodID nullable: true
	}
}
