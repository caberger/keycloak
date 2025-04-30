package at.ac.htl.leonding.demo.features.store;

import java.util.List;

import at.ac.htl.leonding.demo.features.post.Post;

public record DataRoot(List<Post> posts) {
}
