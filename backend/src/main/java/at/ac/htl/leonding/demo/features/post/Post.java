package at.ac.htl.leonding.demo.features.post;

import java.time.LocalDateTime;

import at.ac.htl.leonding.demo.features.category.Category;
import at.ac.htl.leonding.demo.features.user.User;

/** A Post that is edited and published by an author */
public record Post(
    User author,
    String title,
    String body,
    boolean published,
    LocalDateTime createdAt,
    Category category
) {
}
