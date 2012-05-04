package com.bensmann.rcms

class RcmsProfile {
	
	String firstname
	String lastname
	String address
	String postCode
	String city
	String country
	String telephone
	String facsimile
	String mobile
	String email
	Date birthday
	String placeOfBirth
	RcmsProfileMaritalStatus maritalStatus
	String miscellaneous
	
	/*
	static hasMany = [
		language: RcmsProfileLanguage,
		mainFocus: RcmsProfileFocus,
		career: RcmsProfileCareer,
		occupation: RcmsProfileOccupation,
		education: RcmsProfileEducation,
		characterize: RcmsProfileCharacterize
	]
	*/
	
	static constraints = {
		firstname(nullable: true)
		lastname(nullable: true)
		address(nullable: true)
		postCode(nullable: true)
		city(nullable: true)
		country(nullable: true)
		telephone(nullable: true)
		facsimile(nullable: true)
		mobile(nullable: true)
		email(nullable: false, email: true)
		birthday(nullable: true)
		maritalStatus(nullable: true, widget: 'autocomplete_radio')
		/*
		language(nullable: true, widget: 'autocomplete_checkbox')
		career(nullable: true, widget: 'autocomplete_list')
		occupation(nullable: true, widget: 'autocomplete_list')
		education(nullable: true, widget: 'autocomplete_list')
		characterize(nullable: true, widget: 'autocomplete_list')
		*/
	}
	
}