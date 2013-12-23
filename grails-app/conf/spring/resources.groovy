import grails.plugin.springsecurity.SpringSecurityUtils
import com.wwvl.NonRedirectingEntryPoint
beans = {

    def conf = SpringSecurityUtils.securityConfig

    authenticationEntryPoint(NonRedirectingEntryPoint, conf.auth.loginFormUrl) { // '/login/auth'
        forceHttps = conf.auth.forceHttps // false
        useForward = conf.auth.useForward // false
        portMapper = ref('portMapper')
        portResolver = ref('portResolver')
    }

}