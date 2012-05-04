package com.bensmann.rcms

class RcmsTag {
	
	Long id
	String name
	
	static constraints = {
		name(nullable: false)
	}
	
}
