package com.bensmann.rcms

class RcmsProfileOccupation {
	
	String name
	String description
	
	static constraints = {
		name(nullable: false)
		description(nullable: false, widget: 'richtext')
	}
	
}
