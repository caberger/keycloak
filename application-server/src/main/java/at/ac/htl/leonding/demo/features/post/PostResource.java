package at.ac.htl.leonding.demo.features.post;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;
import io.quarkus.logging.Log;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.json.JsonString;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/posts")
public class PostResource {
    @Inject PostRepository postRepository;
    @Inject PostMapper mapper;
    @Claim("realm_access")
    ClaimValue<Map<String, List<JsonString>>> realmAccess;

    @GET
    @PermitAll
    public List<Post> all() {
        return postRepository
            .listAll()
            .stream()
            .map(mapper::toResource)
            .filter(this::am_I_allowedToSeeThis)
            .toList();
    }
    boolean am_I_allowedToSeeThis(Post post) {
        var roles = realmAccess
            .getValue()
            .get("roles")
            .stream()
            .map(s -> s.getString())
            .collect(Collectors.toSet());
        Log.infof("roles: %s", roles);
        return post.published() || roles.contains("editor");
    }
}
