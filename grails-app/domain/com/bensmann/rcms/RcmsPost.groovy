package com.bensmann.rcms

class RcmsPost {
	
	Long id
	Date dateCreated
	Date lastUpdated
	String name
	String title2
	String content
	String meta
	Boolean active = false
	
	static hasMany = [
		tag: RcmsTag
	]
	
	static belongsTo = [
		page: RcmsPage
	]
	
	static mapping = {
		columns {
			content type: 'text'
		}
	}
	
	static constraints = {
		name(nullable: false)
		title2(nullable: true)
		content(nullable: true, blank: true)
		tag(nullable: true, widget: 'autocomplete')
		active()
	}
	
}
