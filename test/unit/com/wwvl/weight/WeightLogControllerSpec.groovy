package com.wwvl.weight


import com.wwvl.nutrition.SessionService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import com.wwvl.auth.User
import spock.lang.*

@TestFor(WeightLogController)
@Mock([User,LogEntry,WeightLogService])
class WeightLogControllerSpec extends Specification {
        LogEntry weightLog
    def setup() {
        User.metaClass.encodePassword = {null}
        def user = new User (username: 'user',password: 'password').save()
        weightLog = new LogEntry(dateWeighed: new Date(), user: user, weight: 110).save()
        SessionService sessionService = Mock()
        sessionService.getLoggedInUser() >> user
        controller.sessionService = sessionService
    }

    def cleanup() {
    }

    void "test list"() {
         when:
        def listCommand = new ListCommand(startDate: new Date() - 1,endDate: new Date() + 1)
        controller.list(listCommand)
        then:
        response.json.size() == 1
    }

    void "test show 404"(){
        when:
        controller.show(0)
        then:
        response.status == 404
    }

    void "test show 200"(){
        when:
        controller.show(weightLog.id)
        then:
        response.json.id == weightLog.id
    }

    void "test delete 404"(){
        when:
        controller.delete(0)
        then:
        response.status == 404
    }

    void "test delete 200"(){
        when:
        controller.delete(weightLog.id)
        then:
        response.status == 204
        LogEntry.count == 0
    }

    void "test update 404"(){
        when:
        def logEntryCommand = new LogEntryCommand(id:0, weight: 114, dateWeighed: new Date() + 2)
        controller.update(logEntryCommand)
        then:
        response.status == 404
    }

    void "test update 200"(){
        when:
        def logEntryCommand = new LogEntryCommand(id:weightLog.id, weight: 114, dateWeighed: new Date() + 2)
        controller.update(logEntryCommand)
        then:
        response.status == 204
        114 == LogEntry.get(weightLog.id).weight
    }

    def "test create"(){
        when:
        def logEntryCommand = new LogEntryCommand(weight: 114, dateWeighed: new Date() + 2)
        controller.create(logEntryCommand)
        then:
        response.status == 200
        response.json.logEntryID == 2
        LogEntry.count == 2
    }
}
