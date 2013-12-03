class UrlMappings {

	static mappings = {

		"/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }

		"/weight"(controller: "WeightLog", parseRequest: true) {
			action = [GET: "list", POST: "create"]
		}
		"/weight/$id"(controller: "WeightLog", parseRequest: true) {
			action = [GET: "show", POST: "update", DELETE: "delete"]
		}

		"/food"(controller: "Food", parseRequest: true) {
			action = [GET: "listFood", POST: "createFood"]
		}
		"/food/$id"(controller: "Food", parseRequest: true) {
			action = [GET: "showFood", POST: "updateFood", DELETE: "deleteFood"]
		}

		"/food/$foodID/portion"(controller: "Food", parseRequest: true) {
			action = [POST: "createPortion"]
		}

		"/food/$foodID/portion/$id"(controller: "Food", parseRequest: true) {
			action = [POST: "updatePortion", DELETE: "deletePortion"]
		}

		"/meals"(controller: "Food", parseRequest: true) {
			action = [GET: "listMeals"]
		}

		"/recipe"(controller: "Recipe", parseRequest: true) {
			action = [GET: "listRecipe", POST: "createRecipe"]
		}
		"/recipe/$id"(controller: "Recipe", parseRequest: true) {
			action = [GET: "showRecipe", POST: "updateRecipe", DELETE: "deleteRecipe"]
		}

		"/recipe/$recipeID/ingredient"(controller: "Recipe", parseRequest: true) {
			action = [POST: "createIngredient"]
		}

		"/recipe/$recipeID/ingredient/$id"(controller: "Recipe", parseRequest: true) {
			action = [POST: "updateIngredient", DELETE: "deleteIngredient"]
		}

		"/foodLog"(controller: "FoodLog", parseRequest: true) {
			action = [POST: "create"]
		}

		"/foodLog/list/$date"(controller: "FoodLog", parseRequest: true) {
			action = [GET: "list"]
		}

		"/foodLog/summary"(controller: "FoodLog", parseRequest: true) {
			action = [GET: "summary"]
		}

		"/foodLog/$id"(controller: "FoodLog", parseRequest: true) {
			action = [GET: "show", POST: "update", DELETE: "delete"]
		}



        "/"(view:"/index")
        "500"(view:'/error')
	}
}
