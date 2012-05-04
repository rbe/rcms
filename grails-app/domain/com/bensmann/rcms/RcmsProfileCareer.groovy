package com.bensmann.rcms

class RcmsProfileCareer {
	
	Date started
	Date ended
	String name
	String description
	
	static constraints = {
		started(nullable: false)
		ended(nullable: true)
		name(nullable: false)
		description(nullable: false, widget: 'richtext')
	}
	
}
