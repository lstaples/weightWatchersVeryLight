package com.wwvl.nutrition

import com.wwvl.auth.User
import grails.converters.JSON

class RecipeController {

	def springSecurityService
	def NutritionService

	def listRecipe() {
		User user = User.load(springSecurityService.principal.id)
		render Recipe.findAllByUser(user) as JSON
	}

	def showRecipe(Integer id){
		User user = User.load(springSecurityService.principal.id)
		def recipe= Recipe.findByIdAndUser(id,user)
		if(!recipe){
			render (status: 404, text: "Recipe Not Found")
			return
		}

		render recipe as JSON
	}

	def createRecipe(RecipeCommand rc) {

		if(rc.hasErrors()){
			response.status = 400
			render rc.errors as JSON
		}

		User user = User.load(springSecurityService.principal.id)
		def recipe = NutritionService.createRecipe(user, rc.name, rc.servings)

		if(recipe.hasErrors()){
			response.status = 500
			render recipe.errors as JSON
		}

		render recipe as JSON
	}

	def deleteRecipe(Integer id){
		User user = User.load(springSecurityService.principal.id)
		def recipe = Recipe.findByIdAndUser(id,user)
		if(!recipe){
			render (status: 404, text: "Recipe Not Found")
			return
		}

		if(recipe.logEntries) {
			render (status: 400, text: "Recipe cannot be deleted because it belongs to at least 1 log entry")
			return
		}
		NutritionService.deleteRecipe(recipe);
		render (status: 204)
	}

	def updateRecipe(RecipeCommand rc){
		if(rc.hasErrors()){
			response.status = 400
			render rc.errors as JSON
		}'
		def user = User.load(springSecurityService.principal.id)

		def recipe = Recipe.findByIdAndUser(rc.id,user)
		if(!recipe){
			render (status: 404, text: "Recipe Not Found")
			return
		}
		NutritionService.updateRecipe(recipe, rc.name, rc.servings)
		if(recipe.hasErrors()){
			response.status = 500
			render recipe.errors as JSON
		}
		render(status: 204)
	}

}

class RecipeCommand{
	Integer id
	String name
	Integer servings

	static constraints = {
		importFrom Recipe
		id nullable: true
	}

}
