package at.ac.htl.leonding.demo.features.store;

import java.util.Collection;
import org.eclipse.serializer.collections.lazy.LazyArrayList;
import org.eclipse.serializer.collections.lazy.LazyList;

import at.ac.htl.leonding.demo.features.user.User;
import at.ac.htl.leonding.demo.lib.Store;

public record DataRoot(LazyList<User> users) {
    public DataRoot(Collection<User> users) {
        this(new LazyArrayList<User>());
        this.users.addAll(users);
    }
    public static DataRoot instance() {
        return (DataRoot) Store.instance().root();
    }
}
