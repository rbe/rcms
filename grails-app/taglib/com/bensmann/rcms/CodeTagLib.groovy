package com.bensmann.rcms

/**
 * A taglib for display code using syntax highlighting.
 */
class CodeTagLib {
	
	/**
	 * Our namespace.
	 */
	static namespace = "code"
	
	/**
	 * The all-mighty Groovy template engine.
	 */
	def groovyPagesTemplateEngine
	
	/**
	 * Path to syntax highlighter.
	 */
	def path = "js/syntaxhighlighter"
	
	/**
	 * Insert JavaScript and CSS in HTML header.
	 */
	def header = { attr, body ->
		out << "<script type=\"text/javascript\" src=\"${resource(dir: 'js/syntaxhighlighter/src', file: 'shCore.js')}\"></script>\n"
		attr["lang"].each {
			def res = resource(dir: path + '/scripts', file: "shBrush${it}.js")
			out << "<script type=\"text/javascript\" src=\"${res}\"></script>\n"
		}
		out << "<link href=\"${resource(dir: path + '/styles', file: 'shCore.css')}\" rel=\"stylesheet\" type=\"text/css\" />\n"
		out << "<link href=\"${resource(dir: path + '/styles', file: 'shThemeDefault.css')}\" rel=\"stylesheet\" type=\"text/css\" />\n"
	}
	
	/**
	 * Insert body needed for syntax highlighter.
	 */
	def body = { attr, body ->
		// Render template
		def text = RcmsHelper.findSnippet(grailsApplication, "code_all.gsp")
		groovyPagesTemplateEngine.createTemplate(text, "apage.gsp").make([
			clipboardSwf: g.resource(dir: path + '/scripts', file: 'clipboard.swf')
		]).writeTo(out)
	}
	
	/**
	 * Highlight a piece of code.
	 */
	def highlight = { attr, body ->
		// Get code
		def builder = new StringBuilder()
		def sourceFile = attr["file"]
		sourceFile ? new File(sourceFile).readLines().each { builder << it + "\n" } : builder << body()
		// Get model
		def model = [
			brush: attr.brush,
			wraplines: attr.wraplines ?: true,
			code: builder.toString()
		]
		// Render template
		def text = RcmsHelper.findSnippet(grailsApplication, "code_highlight.gsp")
		groovyPagesTemplateEngine.createTemplate(text, "apage.gsp").make(model).writeTo(out)
	}
	
}
