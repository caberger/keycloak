package at.ac.htl.leonding.demo.features.user;

import java.util.List;
import java.util.UUID;

import at.ac.htl.leonding.demo.features.post.PostMapper.PostDto;

/**  A data transport object for a user. */
public record UserDto (
    UUID user,
    List<PostDto> posts) {
}
