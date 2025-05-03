package at.ac.htl.leonding.demo.features.post;

import java.util.List;
import at.ac.htl.leonding.demo.features.store.DataRoot;

public interface PostRepository {
    static List<Post> allPublishedPosts() {
        return DataRoot.instance()
                .users()
                .values()
                .stream()
                .map(
                    user -> user.posts()
                        .stream()
                        .filter(Post::published)
                        .toList()
                )
                .flatMap(List::stream)
                .sorted((l, r) -> - l.createdAt().compareTo(r.createdAt()))
                .toList();
    }
}
