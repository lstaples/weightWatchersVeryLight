package com.wwvl.nutrition

import com.wwvl.NutritionTestData
import com.wwvl.auth.*
import com.wwvl.testDataCreator
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(FoodController)
@Mock([User,Role,UserRole,Food,Portion,Ingredient,Recipe,FoodLogEntry,RecipeLogEntry,LogEntry,NutritionService])
class FoodControllerSpec extends Specification {
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

    void "test list food"() {
        when:
        controller.listFood()
        then:
        response.json.size() == 3
    }

    void "test show food 404"() {
        when:
        controller.showFood(0)
        then:
        response.status == 404
    }

    void "test show food 200"() {
        when:
        controller.showFood(testData.bread.id.toInteger())
        then:
        response.json.id == testData.bread.id
    }

    void "test create food"() {
        when:
        def portionCmd = new PortionCommand(description: 'cup', calories: 120)
        def foodCmd = new FoodCommand(name: 'milk', portion: portionCmd)
        controller.createFood(foodCmd)
        then:
        response.status == 200
        Food.count == old(Food.count) + 1
        Portion.count == old(Portion.count) + 1
    }

    void "test delete food 404"(){
        when:
        controller.deleteFood(0)
        then:
        response.status == 404
    }

    void "test delete food 204"() {
        when:
        controller.deleteFood(testData.bread.id.toInteger())
        then:
        response.status == 204
        Food.count == old(Food.count) - 1
    }

    void "test update food 404"(){
        when:
        def cmd = new FoodCommand(name: 'wheat bread', id: 0)
        controller.updateFood(cmd)
        then:
        response.status == 404
    }

    void "test update food 204"(){
        when:
        def cmd = new FoodCommand(name: 'wheat bread', id: testData.bread.id)
        controller.updateFood(cmd)
        then:
        response.status == 204
        Food.get(testData.bread.id).name == 'wheat bread'
    }

    void "test create portion food not found"() {
        when:
        def portionCmd = new PortionCommand(description: 'loaf', calories: 500, foodID: 0)
        controller.createPortion(portionCmd)
        then:
        response.status == 400
    }

    void "test create portion"() {
        when:
        def portionCmd = new PortionCommand(description: 'loaf', calories: 500, foodID: testData.bread.id)
        controller.createPortion(portionCmd)
        then:
        response.status == 200
        testData.bread.portions.size() == old(testData.bread.portions.size()) + 1
    }

    void "test delete portion fail with recipe association"() {
        when:
        controller.deletePortion(testData.cupOfChickenStock.id.toInteger())
        then:
        response.status == 400
    }

    void "test delete portion fail with log entry association"() {
        when:
        controller.deletePortion(testData.bowlOfSalad.id.toInteger())
        then:
        response.status == 400
    }

    void "test delete portion 404"() {
        when:
        controller.deletePortion(0)
        then:
        response.status == 404
    }

    void "test delete portion 204"() {
        when:
        controller.deletePortion(testData.sliceOfBread.id.toInteger())
        then:
        response.status == 204
        if(testData.bread.portions)
            testData.bread.portions.size() == old(testData.bread.portions.size()) - 1
        else
            old(testData.bread.portions.size()) == 1
    }

    void "test update portion 404"(){
        when:
        def portionCmd = new PortionCommand(description: 'loaf', calories: 500, id: 0)
        controller.updatePortion(portionCmd)
        then:
        response.status == 404
    }

    void "test update portion 204"(){
        when:
        def portionCmd = new PortionCommand(description: 'loaf', calories: 500, id: testData.sliceOfBread.id)
        controller.updatePortion(portionCmd)
        then:
        response.status == 204
        Portion.get(testData.sliceOfBread.id).calories == 500
    }

    void "test meal list"(){
        when:
        controller.listMeals()
        then:
        response.json.size() == 4
    }



}
