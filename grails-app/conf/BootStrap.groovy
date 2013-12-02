import com.wwvl.auth.User
import grails.converters.JSON
import com.wwvl.weight.LogEntryMarchaller

class BootStrap {

    def init = { servletContext ->
		environments {
			development {
				def user = new User (username: 'user',password: 'password').save()
			}
		}
		new LogEntryMarchaller().register()
    }
    def destroy = {
    }
}
