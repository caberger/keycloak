package at.ac.htl.leonding.demo.features.user;

import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.jwt.JsonWebToken;

import at.ac.htl.leonding.demo.features.post.Post;
import at.ac.htl.leonding.demo.features.store.DataRoot;
import at.ac.htl.leonding.demo.lib.Responses;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
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
@PermitAll
@Produces({MediaType.APPLICATION_JSON, "text/csv"})
public class UserResource {
    @Inject
    JsonWebToken jwt;

    @GET
    public List<User> all() {
        return DataRoot.instance().users();
    }
    @GET
    @Path("/i")
    public Response me() {
        return Responses.ok(user());
    }
    @GET
    @Path("/posts")
    public Response allPosts() {
        return Responses.ok(user().posts());
    }
    @PUT
    @Path("/posts")
    @RolesAllowed({"editor", "customer"})
    public Response create(Post post) {
        var user = user();
        return user.posts().stream()
            .filter(existingPost -> existingPost.title().equalsIgnoreCase(post.title()))
            .findAny()
            .map(Responses::exists)
            .orElseGet(() -> {
                user.posts().add(post);
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
