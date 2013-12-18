package com.wwvl.nutrition

import com.wwvl.auth.User
class SessionService {
    def springSecurityService
    User getLoggedInUser(){
        User.load(springSecurityService.principal.id)
    }

}
