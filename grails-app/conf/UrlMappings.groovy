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

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
