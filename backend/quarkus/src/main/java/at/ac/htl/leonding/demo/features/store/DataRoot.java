package at.ac.htl.leonding.demo.features.store;

import java.util.List;

import at.ac.htl.leonding.demo.features.user.User;

public record DataRoot(List<User> users) {
}
