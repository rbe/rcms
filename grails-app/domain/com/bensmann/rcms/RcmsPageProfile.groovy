package com.bensmann.rcms

class RcmsPageProfile {
	
	Long id
	String name
	Boolean search
	Boolean menu
	
	static constraints = {
		name(nullable: false)
		search()
		menu()
	}
	
}
