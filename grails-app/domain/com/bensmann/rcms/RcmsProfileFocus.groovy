package com.bensmann.rcms

class RcmsProfileFocus {
	
	RcmsProfileFocusGroup group
	String name
	String description
	Boolean mark = false
	
	static hasMany = [
		parentFocus: RcmsProfileFocus
	]
	
	static constraints = {
		group(nullable: false, widget: 'autocomplete_radio')
		parentFocus(nullable: true, widget: 'autocomplete_list')
		name(nullable: false)
		mark(nullable: false)
		description(nullable: true, widget: 'richtext')
	}
	
}
