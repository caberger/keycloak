package at.ac.htl.leonding.demo.features.post;

import java.util.List;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/posts")
public class PostResource {
    @Inject PostRepository postRepository;
    @Inject PostMapper mapper;
    //@Inject JsonWebToken jwt;
    
    @GET
    @PermitAll
    public List<Post> all() {
        //var claims = jwt.getClaimNames().stream().collect(Collectors.joining(","));
        //Log.infof("claims: %s", claims);
        return postRepository.listAll().stream().map(mapper::toResource).toList();
    }
}
