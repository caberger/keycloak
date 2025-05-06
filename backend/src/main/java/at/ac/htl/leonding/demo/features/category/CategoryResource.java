package at.ac.htl.leonding.demo.features.category;

import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {
    @GET
    public List<Category> all() {
        return CategoryRepository.all();
    }
}
