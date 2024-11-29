package at.ac.htl.leonding.demo.features.post;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostMapper {
    Post toResource(TbPost post) {
        return new Post(post.id, post.title, post.body, post.published);
    }
    TbPost fromResource(Post p) {
        var post = new TbPost();
        post.id = p.id();
        post.title = p.title();
        post.body = p.body();
        post.published = p.published();
        return post;
    }
}
