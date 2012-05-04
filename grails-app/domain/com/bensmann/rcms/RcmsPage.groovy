package com.bensmann.rcms

class RcmsPage {
	
	Long id
	Date dateCreated
	Date lastUpdated
	String name
	String title
	RcmsPageProfile profile
	
	static hasMany = [
		story: RcmsStory,
		post: RcmsPost,
		menu: RcmsMenu
	]
	
	static constraints = {
		name(nullable: false)
		title(nullable: false)
		story(nullable: true)
		post(nullable: true)
	}
	
}
