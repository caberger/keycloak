package at.ac.htl.leonding.demo.features.user;

import java.util.Optional;
import java.util.UUID;

import at.ac.htl.leonding.demo.features.store.DataRoot;
import at.ac.htl.leonding.demo.lib.Store;

public interface UserRepository {
    static Optional<User> find(UUID id) {
        return DataRoot.instance().users()
            .stream()
            .filter(user -> user.id().equals(id))
            .findAny()
        ;
    }
    static void update(User user) {
        Store.set(store -> store.store(user));
    }
    static void add(User user) {
        var root = DataRoot.instance();
        root.users().add(user);
        Store.set(store -> store.store(root));
    }
}
