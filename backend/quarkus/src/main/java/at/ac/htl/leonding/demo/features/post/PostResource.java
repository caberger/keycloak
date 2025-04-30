package at.ac.htl.leonding.demo.features.post;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;
import at.ac.htl.leonding.demo.features.store.Database;
import io.quarkus.logging.Log;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.JsonString;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {
    @Inject
    Database database;

    @Claim("realm_access")
    ClaimValue<Map<String, List<JsonString>>> realmAccess;

    @GET
    @Path("/my")
    @PermitAll
    public List<Post> onlyMyPosts() {
        return posts()
        .stream()
        .filter(this::amIallowedToSeeThis)
        .toList();
    }
    List<Post> posts() {
        return database.root().posts();
    }
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
    @PermitAll
    @Path("/all")
    /* just for demo testing, remove this! */
    public List<Post> allPosts() {
        return posts();
    }

    @GET
    @RolesAllowed("editor")
    public List<Post> all() {
        return database.root().posts();
    }
}
