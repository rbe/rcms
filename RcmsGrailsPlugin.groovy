import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.ControllerArtefactHandler

/**
 * 
 */
class RcmsGrailsPlugin {
	
	/**
	 * Author and plugin.
	 */
	def author = "Ralf Bensmann"
	def authorEmail = "grails@bensmann.com"
	def title = "Rcms plugin."
	def description = """A CMS for Grails."""
	
	/**
	 * URL to the plugin's documentation.
	 */
	def documentation = "http://grails.org/rcms+Plugin"
	
	/**
	 * The plugin version.
	 */
	def version = "0.0.2"
	
	/**
	 * The version or versions of Grails the plugin is designed for.
	 */
	def grailsVersion = "1.2.0> *"
	
	/**
	 * Other plugins this plugin depends on.
	 */
	def dependsOn = [
		controllers: GrailsUtil.grailsVersion,
		core: GrailsUtil.grailsVersion,
		jcaptcha: "1.0",
		mail: "0.9",
		glue: "0.0.5"
	]
	
	/**
	 * 
	 */
	def loadAfter = [
		'controllers'
	]
	
	/**
	 * Other plugins influenced by this plugin.
	 * See http://www.grails.org/Auto+Reloading+Plugins
	 */
	def influences = [/*"controllers"*/]
	
	/**
	 * Plugins to observe for changes.
	 * See http://www.grails.org/Auto+Reloading+Plugins
	 */
	def observe = [
		'controllers'
	]
	
	/**
	 * Resources to watch.
	 * See http://www.grails.org/Auto+Reloading+Plugins
	 */
	def watchedResources = []
	
	/**
	 * Resources that are excluded from plugin packaging.
	 */
	def pluginExcludes = [
		"grails-app/views/"
	]
	
	/**
	 * Implement runtime spring config (optional).
	 * See http://www.grails.org/Runtime+Configuration+Plugins
	 */
	def doWithSpring = {
		//ConstrainedProperty.registerNewConstraint(BestFrameworkConstraint.NAME, BestFrameworkConstraint.class);
	}
	
	/**
	 * Implement post initialization spring config (optional).
	 * See http://www.grails.org/Runtime+Configuration+Plugins
	 */
	def doWithApplicationContext = { applicationContext ->
	}
	
	/**
	 * Implement additions to web.xml (optional).
	 * See http://www.grails.org/Runtime+Configuration+Plugins
	 */
	def doWithWebDescriptor = { xml ->
	}
	
	/**
	 * 
	 */
	private def addControllerMethods(c) {
	}
	
	/**
	 * Implement registering dynamic methods to classes (optional).
	 * See http://www.grails.org/Plugin+Dynamic+Methods
	 */
	def doWithDynamicMethods = { ctx ->
		// Controllers
		application.controllerClasses.each { c ->
			println "${this.class.getName()}: controller: ${c}"
			addControllerMethods(c)
		}
		// Domain Classes
		application.domainClasses.each { d ->
			println "${this.class.getName()}: domain class: ${d}"
		}
	}
	
	/**
	 * Implement code that is executed when any artefact that this plugin is
	 * watching is modified and reloaded. The event contains: event.source,
	 * event.application, event.manager, event.ctx, and event.plugin.
	 */
	def onChange = { event ->
		println "${this.class.name}: onChange: ${event}"
		// Controllers
		if (application.isArtefactOfType(ControllerArtefactHandler.TYPE, event.source)) {
			addControllerMethods(application.getControllerClass(event.source?.name))
		}
	}
	
	/**
	 * Implement code that is executed when the project configuration changes.
	 * The event is the same as for 'onChange'.
	 */
	def onConfigChange = { event ->
		println "${this.class.name}: onConfigChange: ${event}"
	}
	
}
