package com.wwvl.nutrition

import com.wwvl.auth.*
import com.wwvl.nutrition.*
import com.wwvl.testDataCreator
import com.wwvl.NutritionTestData
import grails.test.mixin.Mock
import spock.lang.*
import grails.test.mixin.TestFor

@TestFor(NutritionService)
@Mock([User,Role,UserRole,Food,Portion,Ingredient,Recipe,FoodLogEntry,RecipeLogEntry])
class NutritionServiceSpec extends Specification {
	NutritionTestData testData
	def setup() {
        User.metaClass.encodePassword = {null}
        testData = testDataCreator.create()
	}

	def cleanup() {
	}

	void "test that deleting an ingredient removes calories from a recipe"() {
		when:
        def ingredientCalories = testData.soupBase.calories
       	service.deleteIngredient(testData.soupBase)
		then:
        testData.chickenSoup.calories==old(testData.chickenSoup.calories) - ingredientCalories
	}

    void "test that adding an ingredient adds calories to a recipe"(){
        when:
        service.createIngredient(testData.chickenSoup,testData.sliceOfBread,1)
        then:
        testData.chickenSoup.calories==old(testData.chickenSoup.calories) + testData.sliceOfBread.calories

    }

    void"test that updating ingredient quantity modifies recipe"(){
        when:
        def currentQty = testData.soupBase.quantity
        service.updateIngredient(testData.soupBase, testData.soupBase.quantity + 1)
        then:
        testData.chickenSoup.calories==old(testData.chickenSoup.calories) + testData.cupOfChickenStock.calories

    }

    void "test when recipe servings are updated recipe log calorie count are re-figured"(){
        when:
        service.updateRecipe(testData.chickenSoup,testData.chickenSoup.name,1)
        then:
        testData.recipeLogEntry.calories == testData.chickenSoup.calories * testData.recipeLogEntry.quantity

    }
    void "test when recipe calories are updated recipe log calorie counts are re-totaled"(){
        when:
        testData.chickenSoup.calories-=10
        then:
        testData.recipeLogEntry.calories == old(testData.recipeLogEntry.calories) - Math.floor((10 / testData.chickenSoup.servings)) * testData.recipeLogEntry.quantity

    }

    void "test when portion calories are changed food log calorie counts are updated"(){
        when:
        service.updatePortion(testData.bowlOfSalad,'bowl of Salad',30)
        then:
        testData.foodLogEntry.calories == old(testData.foodLogEntry.calories) - old(testData.bowlOfSalad.calories) + 30

    }

    void "test when portion calories are changed ingredient calorie counts are updated"(){
        when:
        service.updatePortion(testData.cupOfChickenStock,'cup of chicken stock',30)
        def qty = testData.soupBase.quantity
        then:
        testData.soupBase.calories == old(testData.soupBase.calories) - (old(testData.cupOfChickenStock.calories) * qty) + (30 * qty)

    }

    void "test when recipe log servings are updated then the calories is re-configured"(){
        when:
        def currentQty = testData.recipeLogEntry.quantity
        service.saveLogEntry(testData.recipeLogEntry,new Date(),Meal.dinner,10,testData.user)
        then:
        testData.recipeLogEntry.calories == testData.chickenSoup.caloriesPerServing() * 10

    }

}
