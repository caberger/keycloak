package at.aberger.demo.features.hello;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class HelloResource extends ServerResource {
    @Get
    public Hello hello() {
        return new Hello("hello, world");
    }
}
