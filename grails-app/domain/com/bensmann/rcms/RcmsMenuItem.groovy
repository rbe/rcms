package com.bensmann.rcms

class RcmsMenuItem {
	
	Long id
	String name
	String pageId
	Boolean active
	Short position
	
	static belongsTo = [RcmsMenu]
	
	static constraints = {
		name(nullable: false)
		pageId(nullable: false)
		position()
		active()
	}
	
}
