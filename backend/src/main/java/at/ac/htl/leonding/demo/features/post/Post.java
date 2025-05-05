package at.ac.htl.leonding.demo.features.post;

import java.time.LocalDateTime;

import at.ac.htl.leonding.demo.features.category.Category;

public record Post(
    String title,
    String body,
    boolean published,
    LocalDateTime createdAt,
    Category category
) {
}
