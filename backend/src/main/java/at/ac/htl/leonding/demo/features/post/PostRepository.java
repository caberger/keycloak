package at.ac.htl.leonding.demo.features.post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

import at.ac.htl.leonding.demo.features.category.Category;
import at.ac.htl.leonding.demo.features.store.Database;

public interface PostRepository {

    static List<Post> loadFilteredPosts(Predicate<Post> filter) {
        final var descending = -1;
        return Database.root()
                .users()
                .values()
                .stream()
                .map(user -> user.posts().stream().filter(filter).toList())
                .flatMap(List::stream)
                .sorted((l, r) -> descending * l.createdAt().compareTo(r.createdAt()))
                .toList();
    }

    static List<Post> allPublishedPosts() {
        return loadFilteredPosts(Post::published);
    }

    static List<Post> all() {
        return loadFilteredPosts(post -> true);
    }
}
