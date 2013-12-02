package com.wwvl.nutrition

import com.wwvl.auth.User
import grails.converters.JSON
import grails.validation.Validateable

class NutritionController{

	def springSecurityService
	def NutritionService

    def listFood() {
		User user = User.load(springSecurityService.principal.id)
		render Food.findAllByUser(user) as JSON
	}

	def showFood(Integer id){
		User user = User.load(springSecurityService.principal.id)
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

		User user = User.load(springSecurityService.principal.id)
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
		User user = User.load(springSecurityService.principal.id)
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
			render (status: 404, text: "Food cannot be deleted because it belongs to at least 1 recipe")
			return
		}

		c = Portion.createCriteria()
		portions = c.list{
			eq("food",food)
			isNotEmpty("logEntries")
		}

		if(portions) {
			render (status: 404, text: "Food cannot be deleted because it belongs to at least 1 log entry")
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
		def user = User.load(springSecurityService.principal.id)

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
@Validateable
class PortionCommand{
	Integer id
	String description
	Integer calories

	static constraints = {
		importFrom Portion
		id nullable: true
	}
}
