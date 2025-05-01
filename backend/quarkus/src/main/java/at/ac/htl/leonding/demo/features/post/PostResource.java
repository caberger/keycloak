package at.ac.htl.leonding.demo.features.post;

import java.lang.System.Logger;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.serializer.concurrency.XThreads;

import at.ac.htl.leonding.demo.features.store.Database;
import io.quarkus.logging.Log;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.JsonString;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/posts")
@Produces({MediaType.APPLICATION_JSON, "text/csv"})
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {
    @Inject Database database;
    @Inject Logger log;
    @Inject JsonWebToken jwt;

    @Claim("realm_access")
    ClaimValue<Map<String, List<JsonString>>> realmAccess;

    boolean amIallowedToSeeThis(Post post) {
        var roles = realmAccess
                .getValue()
                .get("roles")
                .stream()
                .map(s -> s.getString())
                .peek(role -> Log.infof("role: %s", role))
                .collect(Collectors.toSet());

        return post.published() || roles.contains("editor");
    }
    @GET
    @RolesAllowed({"editor", "customer"})
    public List<Post> all() {
        var posts = new LinkedList<Post>();
        for (var user: database.users()) {
            posts.addAll(user.posts());
        }
        return posts.stream().sorted((l, r) -> l.title().compareTo(r.title())).toList();
    }
    @POST
    @RolesAllowed({"editor", "customer"})
    public Response create(Post post) {
        var postingUser = database.root().users().stream().filter(user -> user.id().equals(jwt.getSubject())).findFirst().orElseThrow();
        postingUser.posts().add(post);
        database.update(store -> {
            store.store(postingUser);
        });

        var location = UriBuilder.fromResource(PostResource.class).build();
        return Response.created(location).build();
    }
}
