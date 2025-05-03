package at.ac.htl.leonding.demo.features.post;

import java.time.LocalDateTime;

public record Post(
    String title,
    String body,
    boolean published,
    LocalDateTime createdAt
) {
}
