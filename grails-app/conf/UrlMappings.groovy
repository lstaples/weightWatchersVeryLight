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

		"/food"(controller: "Nutrition", parseRequest: true) {
			action = [GET: "listFood", POST: "createFood"]
		}
		"/food/$id"(controller: "Nutrition", parseRequest: true) {
			action = [GET: "showFood", POST: "updateFood", DELETE: "deleteFood"]
		}

		"/food/$foodID/portion"(controller: "Nutrition", parseRequest: true) {
			action = [POST: "createPortion"]
		}

		"/food/$foodID/portion/$id"(controller: "Nutrition", parseRequest: true) {
			action = [POST: "updatePortion", DELETE: "deletePortion"]
		}

		"/meals"(controller: "Nutrition", parseRequest: true) {
			action = [GET: "listMeals"]
		}

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
