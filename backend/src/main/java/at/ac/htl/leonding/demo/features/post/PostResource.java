package at.ac.htl.leonding.demo.features.post;

import at.ac.htl.leonding.demo.lib.CsvMessageBodyProvider;
import at.ac.htl.leonding.demo.lib.Responses;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/posts")
@Produces({MediaType.APPLICATION_JSON, CsvMessageBodyProvider.MEDIA_TYPE})
public class PostResource {
    @GET
    @Path("/published")
    public Response published() {
        return Responses.ok(PostMapper.toResource(PostRepository.allPublishedPosts()));
    }
    @GET
    public Response all() {
        return Responses.ok(PostMapper.toResource(PostRepository.all()));
    }   
}
