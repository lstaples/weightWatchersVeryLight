package com.wwvl.auth

class Role {

	String authority

	static mapping = {
		cache true
		table 'roles'
	}

	static constraints = {
		authority blank: false, unique: true, size: 1..100
}
}
