package at.ac.htl.leonding.demo.features.user;

import java.util.Collection;
import java.util.List;

import at.ac.htl.leonding.demo.features.post.PostMapper;
import at.ac.htl.leonding.demo.model.User;

public interface UserMapper {
    static UserDto toResource(User user) {
        return new UserDto(user.id(), PostMapper.toResource(user.posts()));
    }
    static List<UserDto> toResource(Collection<User> users) {
        return users.stream().map(UserMapper::toResource).toList();
    }
}
