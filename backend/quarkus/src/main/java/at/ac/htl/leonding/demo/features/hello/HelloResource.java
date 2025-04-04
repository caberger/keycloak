package at.ac.htl.leonding.demo.features.hello;

import java.time.LocalDateTime;

import org.jboss.resteasy.spi.HttpRequest;

import io.quarkus.logging.Log;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

@Path("/hello")
@PermitAll
public class HelloResource {
    @Context
    HttpRequest request;

    @GET
    public Hello hello() {
        var message = String.format("send hello to client %s", request.getRemoteHost());
        Log.info(message);
        return new Hello("hello, world", LocalDateTime.now());
    }
}
