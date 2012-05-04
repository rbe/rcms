package com.bensmann.rcms

import java.text.SimpleDateFormat

/**
 * Ralf's CMS content service. Delivers content from a database.
 */
class RcmsContentService {
	
	static scope = "prototype"
	boolean transactional = false
	
	/**
	 * Parse ISO dates
	 */
	static def isoDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
	
	/**
	 * Find page: either the given page id or any parent page
	 */
	def findPage(id, closure) {
		def split = id.split("\\_")
		def index = split.length - 1
		def p
		while (index > -1) {
			def crit = split[0..index].join("_")
			p = RcmsPage.withCriteria {
				ilike "name", crit
			}
			if (closure(p)) {
				break
			}
			index--
		}
		p[0]
	}
	
	/**
	 * A menu.
	 */
	def menu(session, id) {
		def map = []
		findPage(id, { p ->
			if (p && p.menu[0]?.size() > 0) {
				true
			}
		})?.menu?.findAll { menu ->
			menu.active == true
		}?.sort { menu ->
			menu.position
		}.each { menu ->
			map << [
				title: menu.name,
				items: menu.items?.findAll { item ->
							item.active
						}?.sort {
							it.position
						}.collect { item ->
							[pageId: item.pageId, name: item.name]
						}
			]
		}
		map
	}
	
	/**
	 * Get profile of a page.
	 */
	def pageProfile(session, id) {
		def p = findPage(id, { p ->
			if (p) {
				true
			}
		})?.profile
		if (p) {
			[
				search: p.search,
				menu: p.menu
			]
		} else {
			[
				search: false,
				menu: true
			]
		}
	}
	
	/**
	 * Retrieve content.
	 */
	def content(session, id, type) {
		//
		def page = RcmsPage.findByName(id)
		if (!page) return
		//
		def all = type == "all"
		def ret = []
		if (all || type == "head") {
			ret << [
				title: page?.title
			]
		}
		if (all || type == "story") {
			page?.story?.each { story ->
				ret << [
					id: story.id,
					type: "story",
					dateCreated: story.dateCreated,
					lastUpdated: story.lastUpdated,
					special: story.special,
					name: story.name,
					title2: story.title2,
					content: story.content,
					tags: story.tag.collect { it.name },
					meta: story.meta ?: "Version ${story.version} vom ${isoDate.parse(story.lastUpdated.toString()).format("dd.MM.yyyy")}",
					infoTitle: story.infoTitle,
					infoText: "<img src=\"${story.imageUrl}\"/><p>${story.infoText}</p>"
				]
			}
		}
		if (all || type == "post") {
			/*page?.post?*/RcmsPost.withCriteria {
				and {
					eq "page.id", page.id
					eq "active", true
				}
				order "lastUpdated", "desc"
			}?.each { post ->
				ret << [
					id: post.id,
					type: "post",
					dateCreated: post.dateCreated,
					lastUpdated: post.lastUpdated,
					name: post.name,
					title2: post.title2,
					content: post.content,
					tags: post.tag.collect { it.name },
					meta: post.meta ?: "Version ${post.version} vom ${isoDate.parse(post.lastUpdated.toString()).format("dd.MM.yyyy")}"
				]
			}
		}
		log.debug ret
		ret
	}
	
	/**
	 * Geocode an airport.
	 */
	def geocodeAirport(iata) {
		def base = "http://ws.geonames.org/search?"
		def qs = []
		qs << "name_equals=" + URLEncoder.encode(iata)
		qs << "fcode=airp"
		qs << "style=full"
		def url = new URL(base + qs.join("&"))
		def connection = url.openConnection()
		//
		def result = [:]
		if (connection.responseCode == 200) {
			def xml = connection.content.text
			def geonames = new XmlSlurper().parseText(xml)
			result.name = geonames.geoname.name as String 
			result.lat = geonames.geoname.lat as String
			result.lng = geonames.geoname.lng as String
			result.state = geonames.geoname.adminCode1 as String
			result.country = geonames.geoname.countryCode as String
		} else {
			log.error("GeocoderService.geocodeAirport FAILED")
			log.error(url)
			log.error(connection.responseCode)
			log.error(connection.responseMessage)
		}		 
		return result
	}
	
}
