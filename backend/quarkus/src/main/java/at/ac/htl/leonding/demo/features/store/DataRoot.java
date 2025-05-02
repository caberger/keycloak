package at.ac.htl.leonding.demo.features.store;

import java.util.Collection;
import java.util.List;

import org.eclipse.serializer.collections.lazy.LazyArrayList;
import at.ac.htl.leonding.demo.features.user.User;
import at.ac.htl.leonding.demo.lib.Store;

/** the entry point to our database */
public record DataRoot(List<User> users) {
    public DataRoot(Collection<User> users) {
        this(new LazyArrayList<User>());
        this.users.addAll(users);
    }
    public static DataRoot instance() {
        return (DataRoot) Store.instance().root();
    }
}
