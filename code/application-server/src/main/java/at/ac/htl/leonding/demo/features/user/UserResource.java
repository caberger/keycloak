package at.ac.htl.leonding.demo.features.user;

import java.util.UUID;

import org.eclipse.microprofile.jwt.JsonWebToken;

import at.ac.htl.leonding.demo.features.post.PostDto;
import at.ac.htl.leonding.demo.features.post.PostMapper;
import at.ac.htl.leonding.demo.lib.Responses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/users")
@Produces({MediaType.APPLICATION_JSON, "text/csv"})
public class UserResource {
    @Inject
    JsonWebToken jwt;

    @GET
    public Response all() {
        return Responses.ok(UserMapper.toResource(UserRepository.all()));
    }
    @GET
    @Path("/i")
    @RolesAllowed({"customer"})
    public Response me() {
        return Responses.ok(UserMapper.toResource(user()));
    }
    @GET
    @Path("/posts")
    @RolesAllowed({"admin", "customer"})
    public Response allPosts() {
        return Responses.ok(PostMapper.toResource(user().posts()));
    }
    @PUT
    @Path("/posts")
    @RolesAllowed({"editor", "customer"})
    public Response create(@Valid PostDto post) {
        var user = user();
        return user.posts().stream()
            .filter(existingPost -> existingPost.title().equalsIgnoreCase(post.title()))
            .findAny()
            .map(Responses::exists)
            .orElseGet(() -> {
                user.posts().add(PostMapper.fromResource(post));
                UserRepository.update(user);
                return Responses.created(UriBuilder.fromResource(UserResource.class).build());
            })
        ;
    }
    @DELETE
    @Path("/posts/{title}")
    public Response deletePost(@PathParam("title") String title) {
        return user().posts().stream()
            .filter(post -> post.title().equalsIgnoreCase(title))
            .findFirst()
            .map(Responses::noContent)
            .orElseGet(Responses::notFound);
    }
    User user() {
        return UserRepository
            .find(UUID.fromString(jwt.getSubject()))
            .orElseThrow() // a 401 should have happened  already.
        ;
    }
}
