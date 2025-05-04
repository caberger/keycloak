package at.ac.htl.leonding.demo.features.post;

import java.util.List;

import at.ac.htl.leonding.demo.features.store.Database;

public interface PostRepository {

    record PostDto(String userId, Post post) {
    }

    static List<PostDto> allPublishedPosts() {
        final var descending = -1;
        return Database.root()
                .users()
                .values()
                .stream()
                .map(
                        user -> user.posts().stream()
                                .filter(Post::published)
                                .map(post -> new PostDto(user.id().toString(), post)).toList())
                .flatMap(List::stream)
                .sorted((l, r) -> descending * l.post().createdAt().compareTo(r.post().createdAt()))
                .toList();
    }
}
