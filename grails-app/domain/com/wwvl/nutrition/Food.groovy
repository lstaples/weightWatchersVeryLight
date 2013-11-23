package com.wwvl.nutrition

import com.wwvl.auth.User
class Food {
	String name

	static hasMany = [portions: Portion]

	static belongsTo = [user:User]

    static constraints = {
		name size: 1..255, unique: ['user'] ,blank: false
    }

	static mapping = {
		user column: 'userID'
	}
}
