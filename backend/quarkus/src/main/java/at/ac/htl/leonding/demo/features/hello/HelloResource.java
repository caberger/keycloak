package at.ac.htl.leonding.demo.features.hello;

import java.time.LocalDateTime;

import at.ac.htl.leonding.demo.features.store.Database;
import io.quarkus.logging.Log;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/hello")
@PermitAll
public class HelloResource {
    public record Hello(
        String greeting,
        LocalDateTime created_at
    ) {
    }

    @Inject
    Database database;

    @GET
    public Hello hello() {
        Log.infof("Elements in root: %d", database.root().posts().size());
        return new Hello(String.format("hello, we have %s posts", database.root().posts().size()), LocalDateTime.now());
    }
}
