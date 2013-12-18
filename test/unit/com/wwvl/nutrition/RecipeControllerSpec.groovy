package com.wwvl.nutrition

import com.wwvl.NutritionTestData
import com.wwvl.auth.*
import com.wwvl.testDataCreator
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RecipeController)
@Mock([User,Role,UserRole,Food,Portion,Ingredient,Recipe,FoodLogEntry,RecipeLogEntry,LogEntry,NutritionService])
class RecipeControllerSpec extends Specification {
    NutritionTestData testData
    Recipe newRecipe
    def setup() {
        User.metaClass.encodePassword = {null}
        testData = testDataCreator.create()
        SessionService sessionService = Mock()
        sessionService.getLoggedInUser() >> testData.user
        controller.sessionService = sessionService
        newRecipe = new Recipe(name: 'alfredo', servings: 15, user: testData.user).save(flush: true)

    }
    def cleanup() {
    }

    void "test list recipe"() {
        when:
        controller.listRecipe()
        then:
        response.status == 200
        response.json.size() == 2
    }

    void "test show recipe 404 "() {
        when:
        controller.showRecipe(0)
        then:
        response.status == 404
    }

    void "test show recipe 200 "() {
        when:
        controller.showRecipe(testData.chickenSoup.id.toInteger())
        then:
        response.status == 200
        response.json.id == testData.chickenSoup.id
    }

    void "test create recipe"() {
        when:
        def cmd = new RecipeCommand(name: 'lasagne', servings: 15)
        controller.createRecipe(cmd)
        then:
        response.status == 200
        Recipe.count == old(Recipe.count) + 1
    }

    void "test delete recipe fail log entry"() {
        when:
        controller.deleteRecipe(testData.chickenSoup.id.toInteger())
        then:
        response.status == 400
    }

    void "test delete recipe 404"() {
        when:
        controller.deleteRecipe(0)
        then:
        response.status == 404
    }

    void "test delete recipe 204"() {
        when:
        controller.deleteRecipe(newRecipe.id.toInteger())
        then:
        response.status == 204
        Recipe.count == old(Recipe.count) - 1
    }

    void "test update recipe 404"() {
        when:
        def cmd = new RecipeCommand(name: 'lasagne', servings: 15, id: 0)
        controller.updateRecipe(cmd)
        then:
        response.status == 404
    }

    void "test update recipe 204"() {
        when:
        def cmd = new RecipeCommand(name: 'lasagne', servings: 15, id: testData.chickenSoup.id)
        controller.updateRecipe(cmd)
        then:
        response.status == 204
        Recipe.get(testData.chickenSoup.id).servings == 15
    }

    void "test create ingredient portion not found" (){
        when:
        def cmd = new IngredientCommand(recipeID: testData.chickenSoup.id, portionID: 0, quantity: 1)
        controller.createIngredient(cmd)
        then:
        response.status == 400
    }

    void "test create ingredient recipe not found" (){
        when:
        def cmd = new IngredientCommand(recipeID: 0, portionID: testData.sliceOfBread.id, quantity: 1)
        controller.createIngredient(cmd)
        then:
        response.status == 400
    }

    void "test create ingredient" (){
        when:
        def cmd = new IngredientCommand(recipeID: testData.chickenSoup.id, portionID: testData.sliceOfBread.id, quantity: 1)
        controller.createIngredient(cmd)
        println Ingredient.list()
        then:
        response.status == 200
        Ingredient.count == old(Ingredient.count) + 1
    }

    void "test delete ingredient 404"() {
        when:
        controller.deleteIngredient(0)
        then:
        response.status == 404
    }

    void "test delete ingredient 204"() {
        when:
        controller.deleteIngredient(testData.soupBase.id.toInteger())
        then:
        response.status == 204
        Ingredient.count == old(Ingredient.count) - 1
    }

    void "test update ingredient 404"() {
        when:
        def cmd = new IngredientCommand(id: 0,  quantity: 10)
        controller.updateIngredient(cmd)
        then:
        response.status == 404
    }

    void "test update ingredient 204"() {
        when:
        def cmd = new IngredientCommand(id: testData.soupBase.id,  quantity: 10)
        controller.updateIngredient(cmd)
        then:
        response.status == 204
        Ingredient.get(testData.soupBase.id).quantity == 10
    }
}
