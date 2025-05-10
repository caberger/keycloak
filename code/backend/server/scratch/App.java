package at.aberger.demo;

import org.restlet.Application;
import org.restlet.routing.Router;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Protocol;
import at.aberger.demo.features.UserResource;

/*
interface Resources {
    static final int PORT = 8080;
    static List<Class<? extends ServerResource>> resources = List.of(
        UserResource.class 
    );
}
*/
public class App extends Application {
    public static void main(String[] args) throws Exception {
        var server = new Server(Protocol.HTTP, 8080);
        //server.setNext(server.createFinder(UserResource.class));
        server.setNext(new App());
        server.start();
    }
    
    @Override
    public Restlet createInboundRoot() {
        //getContext().getLogger().log(Level.INFO, "create inbound root");
        var router = new Router(getContext());
        //var userResource = new UserResource();
        
        router.attach("/users", UserResource.class);
        return router;
    }
}
