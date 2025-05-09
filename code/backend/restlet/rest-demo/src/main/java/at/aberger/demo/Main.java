package at.aberger.demo;

import java.util.Map;

import org.restlet.resource.ServerResource;

import at.aberger.demo.boot.App;
import at.aberger.demo.features.hello.HelloResource;
import at.aberger.demo.features.user.UserResource;

interface Resources {
    static final int PORT = 8080;
    static Map<String, Class<? extends ServerResource>> resources = Map.of(
        "/users",UserResource.class,
        "/hello", HelloResource.class
    );
}

public class Main {
    public static void main(String[] args) throws Exception {
		App.createApp(Resources.PORT, Resources.resources).start();
	}
}
