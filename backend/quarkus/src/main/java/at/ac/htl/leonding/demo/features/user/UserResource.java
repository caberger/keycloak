package at.ac.htl.leonding.demo.features.user;

import java.util.List;

import at.ac.htl.leonding.demo.features.store.Database;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
@PermitAll
@Produces({MediaType.APPLICATION_JSON, "text/csv"})
public class UserResource {
    @Inject
    Database database;

    @GET
    public List<User> all() {
        return database.users();
    }
}
