package at.ac.htl.leonding.demo.features.category;

import java.util.Collection;
import java.util.List;

import at.ac.htl.leonding.demo.features.store.Database;
import at.ac.htl.leonding.demo.lib.Store;

public interface CategoryRepository {
    static List<Category> all() {
        return Database.root().categories().values().stream().toList();
    }
    static void save(Collection<Category> categories) {
        var root = Database.root();
        var existing = Database.root().categories();
        categories.forEach(category -> existing.put(category.name(), category));
        Store.set(store -> {
            store.store(root);
        });
    }
}
