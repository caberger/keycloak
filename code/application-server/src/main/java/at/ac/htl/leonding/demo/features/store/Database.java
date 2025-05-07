package at.ac.htl.leonding.demo.features.store;

import java.util.Map;
import java.util.UUID;

import at.ac.htl.leonding.demo.features.category.Category;
import at.ac.htl.leonding.demo.features.user.User;
import at.ac.htl.leonding.demo.lib.Store;

public sealed interface Database permits Database.Root {
  
    /** the entry point of our database */
    public record Root(
        Map<UUID, User> users,
        Map<String, Category> categories
    ) implements Database {}
    static Root root() {
        return (Root) Store.instance().root();
    }
}
