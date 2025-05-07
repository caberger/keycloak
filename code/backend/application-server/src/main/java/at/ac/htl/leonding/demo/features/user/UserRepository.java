package at.ac.htl.leonding.demo.features.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import at.ac.htl.leonding.demo.features.store.Database;
import at.ac.htl.leonding.demo.lib.Store;
import at.ac.htl.leonding.demo.model.User;


public interface UserRepository {
    static Optional<User> find(UUID id) {
        return Optional.ofNullable(Database.root().users().get(id));
    }
    static Collection<User> all() {
        return Database.root().users().values();
    }
    static void update(User user) {
        Store.set(store -> store.store(user));
    }
    static void add(User user) {
        add(List.of(user));
    }
    static void add(Collection<User> users) {
        var root = Database.root();
        var existingUsers = root.users();
        users.forEach(user -> existingUsers.put(user.id(), user));
        Store.set(store -> store.store(root));
    }
}
