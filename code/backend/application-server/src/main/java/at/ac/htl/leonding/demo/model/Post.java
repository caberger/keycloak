package at.ac.htl.leonding.demo.model;

import java.time.LocalDateTime;

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
