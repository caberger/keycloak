package at.ac.htl.leonding.demo.features.post;

import java.util.List;

import at.ac.htl.leonding.demo.lib.CsvMessageBodyProvider;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/posts")
@Produces({MediaType.APPLICATION_JSON, CsvMessageBodyProvider.MEDIA_TYPE})
public class PostResource {
    @GET
    @Path("/published")
    public List<PostDto> published() {
        return PostMapper.toResource(PostRepository.allPublishedPosts());
    }
    @GET
    public List<PostDto> all() {
        return PostMapper.toResource(PostRepository.all());
    }   
}
