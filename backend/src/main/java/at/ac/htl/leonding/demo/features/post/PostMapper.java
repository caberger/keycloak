package at.ac.htl.leonding.demo.features.post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import at.ac.htl.leonding.demo.features.category.Category;

public interface PostMapper {
    /** The Data Transfer Object for a Post that avoids recursion in REST responses.
    */
    public record PostDto(
        String userId,
        String title,
        String body,
        Boolean published,
        LocalDateTime createdAt,
        Category category
    ) {} 
    /** Map a Post to a PostDto. */
    static PostDto toResource(Post post) {
        return new PostDto(post.author().id().toString(), post.title(), post.body(), post.published(), post.createdAt(), post.category());
    }
    static List<PostDto> toResource(Collection<Post> posts) {
        return posts.stream().map(PostMapper::toResource).toList();
    }
}
