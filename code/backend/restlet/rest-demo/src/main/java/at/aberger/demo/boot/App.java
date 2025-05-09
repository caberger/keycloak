package at.aberger.demo.boot;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.resource.Directory;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

public class App extends Application {
	public static final Logger LOGGER = Engine.getLogger(Application.class);
	final Map<String, Class<? extends ServerResource>> resources;

	private App(Map<String, Class<? extends ServerResource>> resources) {
		this.resources = resources;
	}
	
	public static Component createApp(int port, Map<String, Class<? extends ServerResource>> resources) {
		Engine.setLogLevel(Level.FINE);
		var component = new Component();
		component.getServers().add(Protocol.HTTP, port);
		// Add CLAP (ClassLoader Access Protocol) to access to representations via classloaders
		component.getClients().add(Protocol.CLAP);
		// Then attach it to the local host
		component.getDefaultHost().attach(new App(resources));
		LOGGER.info("Application is now available on http://localhost:" + port + "/web/index.html");
		return component;
	}

	@Override
	public Restlet createInboundRoot() {
		return createApiRouter();
	}
	private Router createApiRouter() {
		var directory = new Directory(getContext(), "clap://class/META-INF/resources");
		directory.setDeeplyAccessible(true);
		directory.setIndexName("index.html");

		var router = new Router(getContext());
		resources.forEach((key, value) -> {
			router.attach(key, value); // e.g. hello will be available at /hello and /hellos 
		});
		router.attach("/web", directory);
		return router;
	}
}
