package com.bensmann.rcms

class RcmsMenu {
	
	Long id
	String name
	String pageId
	Short position
	Boolean active
	
	static belongsTo = [RcmsPage]
	
	static hasMany = [
		items: RcmsMenuItem
	]
	
	static constraints = {
		name(nullable: true)
		items(nullable: false, widget: 'autocomplete')
		position()
		active()
	}
	
}
