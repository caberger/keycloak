package at.aberger.demo.features.user;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class UserResource extends ServerResource {
    @Get
    public User hello() {
        var user = new User(null, "John", "Doe");
        return user;
    }
}
