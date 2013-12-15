package com.wwvl.nutrition

import com.wwvl.auth.User
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
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
		}
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

	def createIngredient(IngredientCommand ic){
		if(ic.hasErrors()){
			response.status = 400
			render ic.errors as JSON
		}

		def recipe= Recipe.get(ic.recipeID)
		if(!recipe){
			render (status: 400, text: "Recipe Not Found")
			return
		}

		def portion= Portion.get(ic.portionID)
		if(!recipe){
			render (status: 400, text: "Portion Not Found")
			return
		}

		def ingredient = NutritionService.createIngredient(recipe, portion, ic.quantity)
		if(ingredient.hasErrors()){
			response.status = 500
			render ingredient.errors as JSON
		}

		render ingredient as JSON
	}

	def deleteIngredient(Integer id){
		def ingredient= Ingredient.get(id)
		if(!ingredient){
			render (status: 404, text: "Ingredient Not Found")
			return
		}
		NutritionService.deleteIngredient(ingredient);
		render (status: 204)
	}

	def updateIngredient(IngredientCommand ic){
		if(ic.hasErrors()){
			response.status = 400
			render ic.errors as JSON
		}

		def ingredient= Ingredient.get(ic.id)
		if(!ingredient){
			render (status: 404, text: "Ingredient Not Found")
			return
		}

		NutritionService.updateIngredient(ingredient, ic.quantity)
		if(ingredient.hasErrors()){
			response.status = 500
			render ingredient.errors as JSON
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

class IngredientCommand{
	Integer id
	Integer recipeID
	Integer quantity
	Integer portionID

	static constraints = {
		importFrom Ingredient
		id nullable: true
		recipeID nullable: true
		portionID nullable: true
	}
}
