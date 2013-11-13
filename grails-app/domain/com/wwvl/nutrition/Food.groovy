package com.wwvl.nutrition

class Food {
	String name

	static hasMany = [portions: Portion]

    static constraints = {
		name size: 1..255, unique: true
    }
}
