import com.wwvl.testDataCreator
import com.wwvl.MarshallerConfigurer

class BootStrap {

    def init = { servletContext ->
		environments {
			development {
				testDataCreator.create()
			}
		}

		//registers marshallers for our domain objects
		new MarshallerConfigurer().configure()
    }
    def destroy = {
    }
}
