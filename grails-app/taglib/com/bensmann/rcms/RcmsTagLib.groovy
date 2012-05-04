package com.bensmann.rcms

import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH

/**
 * Ralf's CMS taglib for generating content.
 */
class RcmsTagLib {
	
	/**
	 * Our namespace.
	 */
	static namespace = "rcms"
	
	/**
	 * 
	 */
	static returnObjectForTags = ['menu', 'isActualPage', 'pageProfile', 'page', 'pageContent']
	
	/**
	 * The all-mighty Groovy template engine.
	 */
	def groovyPagesTemplateEngine
	
	/**
	 * The JCaptcha plugin service.
	 */
	def jcaptchaService
	
	/**
	 * The Rcms content service.
	 */
	def rcmsContentService
	
	/**
	 * Build main menu for a certain page.
	 */
	def menu = { attr, body ->
		def id = attr.id ?: session.actualPage
		rcmsContentService.menu(session, id)
	}
	
	/**
	 * Create a link to a page.
	 */
	def linkPage = { attr, body ->
		def id = attr.id ?: session.actualPage
		def userAgent = request.getHeader("User-Agent")
		// Produce SEO-friendly URLs?
		// http://www.user-agents.org/
		if (/*userAgent =~ /(?i)mozilla/ || */userAgent =~ /(?i)bot/ || userAgent =~ /(?i)crawl/ || userAgent =~ /(?i)spider/ || userAgent =~ /(?i)seeker/) {
			out << g.link(controller: "rcms", action: "show", id: id) {
				body()
			}
		} else {
			if (!attr.update) {
				out << g.link(controller: "rcms", action: "show", id: id) {
					body()
				}
			} else if (attr.showpost) {
				out << g.remoteLink(controller: "rcms", action: "showpost", id: id, update: [success: attr.update, failure: attr.update]) {
					body()
				}
			} else if (attr.showcontent) {
				out << g.remoteLink(controller: "rcms", action: "showcontent", id: id, update: [success: attr.update, failure: attr.update]) {
					body()
				}
			}
		}
	}
	
	/**
	 * Create links for tags.
	 */
	def linkTag = { attr, body ->
		def id = attr.id ?: session.actualPage
		out << g.link(controller: "rcms", action: "tag", id: id) {
			body() ?: id
		}
	}
	
	/**
	 * External link.
	 */
	def link = { attr, body ->
		out << "<a href=\"${attr.href}\" target=\"_new\">" << body() << "</a>"
	}
	
	/**
	 * Is given page id the actual page?
	 */
	def isActualPage = { attr, body ->
		def id = attr.id ?: session.actualPage
		if (id == session.actualPage) {
			true
		} else {
			false
		}
	}
	
	/**
	 * Insert RSS feed for a certain page.
	 */
	def rssFeed = { attr, body ->
		def id = attr.id ?: session.actualPage
		out << "<li id=\"rss\">"
		out << g.link(controller: "rcms", action: "rss", id: id) {
			body() ?: "RSS Feed"
		}
		out << "</li>"
	}
	
	/**
	 * Get page metadata.
	 */
	def pageProfile = { attr, body ->
		def id = attr.id ?: session.actualPage
		if (attr.if) {
			def test = attr.if
			if (rcmsContentService.pageProfile(session, id)."${test}") {
				body() ?: true
			} else {
				""
			}
		} else {
			def get = attr.get
			rcmsContentService.pageProfile(session, id)."${get}"
		}
	}
	
	/**
	 * Display content.
	 */
	def pageContent = { attr, body ->
		def id = attr.id ?: session.actualPage
		def t = attr.t
		rcmsContentService.content(session, id, t)
	}
	
	/**
	 * Render content which can contain Grails tags.
	 */
	def render = { attr, body ->
		def text = body()
		def model
		if (attr.snippet) {
			text = RcmsHelper.findSnippet(grailsApplication, attr.snippet)
			model = attr.params
		}
		groovyPagesTemplateEngine.createTemplate(text.toString(), "rcms_render_page.gsp").make(model).writeTo(out)
	}
	
	/**
	 * Add scripts to header for using a Google Map.
	 */
	def gmapHeader = { attr, body ->
		def host = request.getHeader("Host").split(":")[0]
		out << RcmsHelper.findSnippet(grailsApplication, "gmap_header_${host}.gsp")
	}
	
	/**
	 * Render JavaScript to show a Google map.
	 */
	def gmap = { attr, body ->
		groovyPagesTemplateEngine.createTemplate(
			RcmsHelper.findSnippet(grailsApplication, "gmap.js.gsp"),
			"gmap.js.gsp"
		).make([div: attr.div, address: attr.address, zoom: attr.zoom]).writeTo(out)
	}
	
	/**
	 * Generate captcha and return img-tag.
	 */
	def captcha = { attr, body ->
		//jcaptchaService.getCaptchaService("imageCaptcha").getChallengeForID(session.id)
		out << jcaptcha.jpeg(id: attr.id, name: attr.name, height: attr.height, width: attr.width, onclick: attr.onclick)
	}
	
	/**
	 * Render Piwik JavaScript.
	 */
	def piwik = { attr, body ->
		def id = attr.id ?: session.actualPage
		groovyPagesTemplateEngine.createTemplate(
			RcmsHelper.findSnippet(grailsApplication, "piwik.gsp"),
			"piwik.gsp"
		).make([documentTitle: id]).writeTo(out)
	}
	
}
