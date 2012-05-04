package com.bensmann.rcms

class RcmsProfileLanguage {
	
	Long id
	String name
	
	static belongsTo = [RcmsProfile]
	
	static constraints = {
		name(nullable: false)
	}
	
}
