package at.ac.htl.leonding.demo.features.store;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.eclipse.serializer.collections.lazy.LazyHashMap;

import at.ac.htl.leonding.demo.features.user.User;
import at.ac.htl.leonding.demo.lib.Store;

/** the entry point to our database */
public record DataRoot(Map<UUID, User> users) {
    public DataRoot(Collection<User> users) {
        this(new LazyHashMap<UUID, User>());
        users.forEach(user -> users().put(user.id(), user));
    }
    public static DataRoot instance() {
        return (DataRoot) Store.instance().root();
    }
}
