package at.ac.htl.leonding.demo.features.post;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** The Data Transfer Object for a Post that avoids recursion in REST responses.
*/
    public record PostDto(
        @NotNull
        String userId,
        @NotBlank
        String title,
        String body,
        boolean published,
        LocalDateTime createdAt,
        String categoryName
    ) {}
