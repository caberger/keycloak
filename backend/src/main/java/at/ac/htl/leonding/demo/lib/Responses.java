package at.ac.htl.leonding.demo.lib;

import java.net.URI;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.StreamingOutput;

public interface Responses {
    static Response ok(Object entity) {
        return Response.ok(entity).build();
    }
    static Response ok() {
        return Response.ok().build();
    }
    static ResponseBuilder ok(StreamingOutput output) {
        return Response.ok(output);
    }
    static Response created(URI location) {
        return Response.created(location).build();
    }
    static Response noContent(Object entity) {
        return Response.noContent().entity(entity).build();
    }
    static Response noContent() {
        return Response.noContent().build();
    }
    static Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    static Response exists(Object entity) {
        return Response.status(Response.Status.CONFLICT).entity(entity).build();
    }
    static Response badRequest(Exception e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
    }
}
