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
    
    @GET
    @PermitAll
    public List<Post> all() {
        return postRepository.listAll().stream().map(mapper::toResource).toList();
    }
}
