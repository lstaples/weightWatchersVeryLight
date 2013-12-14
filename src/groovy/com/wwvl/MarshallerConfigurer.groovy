package com.wwvl

import com.wwvl.nutrition.*
import grails.converters.JSON

class MarshallerConfigurer {

	void configure(){
		JSON.registerObjectMarshaller(Food){Food food ->
			return [id: food.id,name: food.name ,portions: food.portions]
		}

		JSON.registerObjectMarshaller(Ingredient){Ingredient ingredient->
			return [id: ingredient.id,quantity: ingredient.quantity, calories: ingredient.calories, portion: ingredient.portion]
		}

		JSON.registerObjectMarshaller(com.wwvl.nutrition.LogEntry){com.wwvl.nutrition.LogEntry logEntry ->
			return [id: logEntry.id
					,name: logEntry.getName()
					,type:logEntry.getType()
					,dateEaten: logEntry.dateEaten.format("MM/dd/yyyy")
					,quantity: logEntry.quantity
					,calories: logEntry.calories
					,meal: logEntry.meal.toString()
			]
		}

		JSON.registerObjectMarshaller(Meal){Meal meal->
			return [name: meal.toString()]
		}

		JSON.registerObjectMarshaller(Portion){Portion portion ->
			return [id: portion.id,description: portion.description,calories: portion.calories, foodID: portion.food.id, foodName: portion.food.name]
		}

		JSON.registerObjectMarshaller(Recipe){Recipe recipe->
			return [id: recipe.id,name: recipe.name , servings: recipe.servings, calories: recipe.calories, ingredients: recipe.ingredients]
		}

		JSON.registerObjectMarshaller(com.wwvl.weight.LogEntry){com.wwvl.weight.LogEntry logEntry ->
			return [id: logEntry.id,dateWeighed: logEntry.dateWeighed.format('MM/dd/yyyy'),weight:logEntry.weight]
		}
	}

}
