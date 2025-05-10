package at.aberger.demo.boot;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.resource.Directory;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

import at.aberger.demo.auth.KeycloakAuthenticator;

public class App extends Application {
	public static final Logger LOGGER = Engine.getLogger(Application.class);
	final Map<String, Class<? extends ServerResource>> resources;
	final String apiRoot;

	private App(String apiRoot, Map<String, Class<? extends ServerResource>> resources) {
		this.apiRoot = apiRoot;
		this.resources = resources;
	}
	public static Component createApp(int port, String apiRoot, Map<String, Class<? extends ServerResource>> resources) {
		Engine.setLogLevel(Level.INFO);
		var component = new Component();
		component.getServers().add(Protocol.HTTP, port);
		// Add CLAP (ClassLoader Access Protocol) to access to representations via classloaders
		component.getClients().add(Protocol.CLAP);
		// Then attach it to the local host
		component.getDefaultHost().attach(new App(apiRoot, resources));
		LOGGER.info(Logo.text);
		LOGGER.info("Application is now available on http://localhost:" + port + "/");
		return component;
	}

	@Override
	public Restlet createInboundRoot() {
		return createApiRouter();
	}
	Restlet createApiRouter() {
		var directory = new Directory(getContext(), "clap://class/META-INF/resources");
		directory.setDeeplyAccessible(true);
		directory.setIndexName("index.html");
		getMetadataService().setDefaultMediaType(MediaType.APPLICATION_JSON);

		var authFilter = new KeycloakAuthenticator(getContext());
		var router = new Router(getContext());
		router.setRoutingMode(Router.MODE_BEST_MATCH);

		resources.forEach((key, value) -> {
			var root = apiRoot + key;
			router.attach(root, value); // e.g. hello will be available at /hello and /hellos 
		});
		router.attach("/", directory);
		authFilter.setNext(router);
		return authFilter;
	}
}
