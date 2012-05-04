package com.bensmann.rcms

import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH

/**
 * Ralf's CMS controller.
 */
class RcmsController {
	
	/**
	 * GrailsApplication object.
	 */
	def grailsApplication
	
	/**
	 * The Grails mail service.
	 */
	def mailService
	
	/**
	 * The JCaptcha plugin service.
	 */
	def jcaptchaService
	
	/**
	 * Ralf's CMS content service.
	 */
	def rcmsContentService
	
	/**
	 * 
	 */
	def viewService
	
	/**
	 * Root directory for views.
	 */
	def viewRoot
	
	/**
	 * The default action.
	 */
	def defaultAction = "show"
	
	/**
	 * The before interceptor.
	 * See http://www.grails.org/Controllers+-+Interceptors
	 */
	def beforeInterceptor = {
		init()
		responseNoCache()
	}
	
	/**
	 * Initialize.
	 */
	def init() {
		viewRoot = viewService.findViewRoot()
	}
	
	/**
	 * Save actual page in session.
	 */
	private def setActualPage(actualPage) {
		session.actualPage = actualPage
	}
	
	/**
	 * The 'show' action: render a page.
	 */
	def show = {
		// Which page should be displayed?
		def pageId = params.id ?: "index"
		def gsp = viewService.findGsps(grailsApplication).find {
			(it == pageId || it == "rcms/" + pageId + ".gsp")
		}
		// Set actual page ID in session
		setActualPage(pageId) // TODO Why use different namings here?
		// Display the GSP or render content dynamically using content.gsp
		if (gsp) {
			render(view: pageId)
		} else {
			render(view: "page")
		}
	}
	
	/**
	 * Render "content" snippet.
	 */
	def showcontent = {
		// Which page should be displayed?
		def pageId = params.id
		// Set actual page ID in session
		setActualPage(pageId) // TODO Why use different namings here?
		// Piwik
		rcms.piwik(id: pageId)
		// Get content from service
		def all = rcmsContentService.content(session, pageId, "all")
		if (all) {
			render rcms.render(
				snippet: "content.gsp"
			)
		} else {
			render ""
		}
	}
	
	/**
	 * Render "post" snippet.
	 */
	def showpost = {
		// Which page should be displayed?
		def pageId = params.id
		// Piwik
		rcms.piwik(id: pageId)
		// Get posts
		def post = rcmsContentService.content(session, pageId, "post")
		if (post) {
			// Render post snippet for each post
			post.each { p ->
				render rcms.render(
					params: [post: p],
					snippet: "post.gsp"
				)
			}
		} else {
			render ""
		}
	}
	
	/**
	 * Search content of a page: story, post.
	 */
	def search = {
		// TODO
	}
	
	/**
	 * Submit an email form after captcha validation.
	 * TODO Provide this through a service.
	 */
	def email = {
		// Construct mail body
		println "email: ${params}"
		def builder = new StringBuilder()
		params.findAll {
			it.key =~ /email_/
		}.each {
			builder << "${it.key}: ${it.value}\n"
		}
		println "New email: ${builder.toString()}"
		// Check captcha
		def captcha = params.imageCaptcha?.toUpperCase()
		def ok = jcaptchaService.validateResponse("imageCaptcha", session.id, captcha)
		println "email: got captcha: ${captcha}: ${ok}"
		if (ok) {
			// TODO Render a email template
			// Send mail
			mailService.sendMail {
				to "jobs@bensmann.com"
				subject "Kontaktformular www.bensmann.com"
				body builder.toString()
			}
			render "<h3>Vielen Dank für Ihre Anfrage!</h3>"
		} else {
			render "<h3>Vielen Dank für Ihre Anfrage!</h3>"
		}
	}
	
	/**
	 * Geocode an airport using geonames.org.
	 */
	def geocodeAirport = {
		def result = rcmsContentService.geocodeAirport(params.iata)
		render result as grails.converters.JSON
	}
	
}
