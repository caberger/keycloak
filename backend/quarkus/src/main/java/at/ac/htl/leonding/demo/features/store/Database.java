package at.ac.htl.leonding.demo.features.store;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.eclipse.serializer.collections.lazy.LazyHashMap;

import at.ac.htl.leonding.demo.features.user.User;
import at.ac.htl.leonding.demo.lib.Store;

public sealed interface Database permits Database.Root {
  
    /** the entry point of our database */
    public record Root(Map<UUID, User> users) implements Database {
        public Root(Collection<User> users) {
            this(new LazyHashMap<UUID, User>());
            users.forEach(user -> users().put(user.id(), user));
        }
    }
    static Root root() {
        return (Root) Store.instance().root();
    }
}
