package com.wwvl.nutrition

import com.wwvl.NutritionTestData
import com.wwvl.auth.*
import com.wwvl.testDataCreator
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(FoodLogController)
@Mock([User,Role,UserRole,Food,Portion,Ingredient,Recipe,FoodLogEntry,RecipeLogEntry,LogEntry,NutritionService])

class FoodLogControllerSpec extends Specification {
    NutritionTestData testData
    def setup() {
        User.metaClass.encodePassword = {null}
        testData = testDataCreator.create()
        SessionService sessionService = Mock()
        sessionService.getLoggedInUser() >> testData.user
        controller.sessionService = sessionService
    }

    def cleanup() {
    }

    void "test list invalid meal"() {
        when:
        def cmd = new FoodLogListCommand(date: new Date().clearTime(),meal: "brinner")
        controller.list(cmd)
        then:
        response.status == 400
    }

    void "test list"() {
        when:
        def cmd = new FoodLogListCommand(date: new Date().clearTime())
        controller.list(cmd)
        then:
        response.json.size() == 2
    }

    void "test show 404"(){
        when:
        controller.show(0)
        then:
        response.status == 404
    }

    void "test show 200"(){
        when:
        controller.show(testData.recipeLogEntry.id.toInteger())
        then:
        response.json.id == testData.recipeLogEntry.id
    }

    void "test delete 404"(){
        when:
        controller.delete(0)
        then:
        response.status == 404
    }

    void "test delete 200"(){
        when:
        controller.delete(testData.recipeLogEntry.id.toInteger())
        then:
        response.status == 204
        RecipeLogEntry.count == old(RecipeLogEntry.count) - 1
    }

    void "test create 400"(){
        when:
        def cmd = new LogEntryCommand(recipeID: 0, dateEaten: new Date(), meal: Meal.dinner, quantity: 1)
        controller.create(cmd)
        then:
        response.status == 400
    }

    void "test create 200"(){
        when:
        def cmd = new LogEntryCommand(recipeID: 1, dateEaten: new Date() + 1, meal: Meal.dinner, quantity: 1)
        controller.create(cmd)
        then:
        response.status == 200
        RecipeLogEntry.count == old(RecipeLogEntry.count) + 1
    }

    void "test update 404"(){
        when:
        def cmd = new LogEntryCommand(id:0, recipeID: 1, dateEaten: new Date(), meal: Meal.dinner, quantity: 1)
        controller.update(cmd)
        then:
        response.status == 404
    }

    void "test update 400"(){
        when:
        def cmd = new LogEntryCommand(id:testData.recipeLogEntry.id, recipeID: 0, dateEaten: new Date(), meal: Meal.dinner, quantity: 1)
        controller.update(cmd)
        then:
        response.status == 400
    }

    void "test update 200"(){
        when:
        def cmd = new LogEntryCommand(id:testData.recipeLogEntry.id, recipeID: 1, dateEaten: new Date() + 1, meal: Meal.dinner, quantity: 2)
        controller.update(cmd)
        then:
        response.status == 204
        RecipeLogEntry.get(testData.recipeLogEntry.id).quantity == 2
    }
}
