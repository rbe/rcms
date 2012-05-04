package com.bensmann.rcms

class RcmsStory {
	
	Long id
	Date dateCreated
	Date lastUpdated
	String name
	String title2
	String content
	String meta
	
	String special
	String infoTitle
	String infoText
	String imageUrl
		
	static hasMany = [
		tag: RcmsTag
	]
	
	static belongsTo = [
		page: RcmsPage
	]
	
	static mappings = {
		columns {
			content type: 'text'
		}
	}
	
	static constraints = {
		name(nullable: false)
		title2(nullable: true)
		content(nullable: false)
		tag(nullable: true, widget: 'autocomplete')
		imageUrl(nullable: false)
		meta(nullable: true)
	}
	
}
