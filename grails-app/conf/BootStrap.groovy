import com.wwvl.nutrition.*
import com.wwvl.auth.*
import com.wwvl.MarshallerConfigurer

class BootStrap {

    def init = { servletContext ->
		environments {
			development {
				def user = new User (username: 'user',password: 'password').save()

				def endUserRole = new Role(authority: 'ROLE_USER').save()
				def adminRole = new Role(authority: 'ROLE_ADMIN').save()

				new UserRole(user: user, role: endUserRole).save()
				new UserRole(user: user, role: adminRole).save()

				def food = new Food(name: 'deletable',user: user).save()
				def portion = new Portion(description: 'alsoDeletable' ,food: food, calories: 10).save()

				def food2 = new Food(name: 'salad', user: user).save()
				def portion2 = new Portion(description: 'bowl' ,food: food2, calories: 20).save()

				def logEntry = new FoodLogEntry(portion: portion2, dateEaten: new Date(), quantity: 1, user: user,  meal: Meal.breakfast)
				logEntry.reTotalCalories()
				logEntry.save()

				def food3 = new Food(name: 'Chicken Stock', user: user).save()
				def portion3 = new Portion(description: 'cup' ,food: food3, calories: 50).save()

				def recipe = new Recipe(user: user, servings: 4, name: 'chicken soup').save()

				def ingredient = new Ingredient(quantity: 3, portion: portion3, recipe: recipe)
				ingredient.reTotalCalories()
				ingredient.save()

				def logEntry2 = new RecipeLogEntry(recipe: recipe, dateEaten: new Date(), quantity: 1, user: user, meal: Meal.dinner)
				logEntry2.reTotalCalories()
				logEntry2.save()
			}
		}

		//registers marshallers for our domain objects
		new MarshallerConfigurer().configure()
    }
    def destroy = {
    }
}
