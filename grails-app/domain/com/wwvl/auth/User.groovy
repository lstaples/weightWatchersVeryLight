package com.wwvl.auth

import com.wwvl.nutrition.*
import com.wwvl.weight.*

class User {

	transient springSecurityService

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true , size: 1..50
		password blank: false
	}

	static hasMany = [foods:Food,recipes:Recipe, intakeLogEntries: com.wwvl.nutrition.LogEntry, weightLogEntries: com.wwvl.weight.LogEntry ]

	static mapping = {
		password column: '`password`'
		table 'users'

	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
