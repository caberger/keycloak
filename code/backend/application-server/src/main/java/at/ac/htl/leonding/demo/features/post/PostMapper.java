package at.ac.htl.leonding.demo.features.post;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import at.ac.htl.leonding.demo.features.category.CategoryRepository;
import at.ac.htl.leonding.demo.features.user.UserRepository;
import at.ac.htl.leonding.demo.model.Post;

public interface PostMapper {
    /** Map a Post to a PostDto. */
    static PostDto toResource(Post post) {
        return new PostDto(post.author().id().toString(), post.title(), post.body(), post.published(), post.createdAt(), post.category().name());
    }
    static List<PostDto> toResource(Collection<Post> posts) {
        return posts.stream().map(PostMapper::toResource).toList();
    }
    static Post fromResource(PostDto dto) {
        var user = UserRepository.find(UUID.fromString(dto.userId())).orElseThrow(() -> new RuntimeException("no such user " + dto.userId()));
        var category = CategoryRepository.find(dto.title()).orElseThrow(() -> new RuntimeException("no such category " + dto.title()));
        return new Post(user, dto.title(), dto.body(), dto.published(), dto.createdAt(), category);
    }
}
