package at.ac.htl.leonding.demo.features.hello;

import java.time.LocalDateTime;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/hello")
public class HelloResource {
    @GET
    public Hello hello() {
        return new Hello("hello, world", LocalDateTime.now());
    } 
}
