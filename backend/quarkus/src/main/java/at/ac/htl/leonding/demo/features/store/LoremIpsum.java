package at.ac.htl.leonding.demo.features.store;


import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.github.javafaker.Faker;

import at.ac.htl.leonding.demo.features.post.Post;
import at.ac.htl.leonding.demo.features.user.User;
import at.ac.htl.leonding.demo.lib.Store;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

/** 
 * Initialize database and create some demo records if we are in debug mode.
*/
@ApplicationScoped
public class LoremIpsum {
    @Inject Logger log;
    @ConfigProperty(name="store.create-user-id", defaultValue="")
    String createUserId;
    @ConfigProperty(name="store.generation", defaultValue = "none")
    String generation;
    DataRoot createRoot() {
        return new DataRoot(demoData());
    }
    List<User> demoData() { 
        var users = new ArrayList<User>();
        if (!createUserId.isBlank()) {
            var faker = new Faker();
            var random = faker.random();
            var user = new User(UUID.fromString(createUserId));
            for (var i = 0; i < 5; i++) {
                user.posts().add(new Post(faker.hacker().verb(), faker.chuckNorris().fact(), random.nextBoolean()));
            }
            users.add(user);
        }
        return users;
    }
    void onInit(@Observes StartupEvent ev) {
        if (generation.equals("drop-and-create")) {
            Store.drop();
        }
        if (DataRoot.instance() == null) {
            log.log(Level.INFO, "empty database, create root ({0})", createUserId);
            final var initialRoot = createRoot();
            Store.set(manager -> {
                manager.setRoot(initialRoot);
                manager.storeRoot();
            });
        }
    }
    void shutdown(@Observes ShutdownEvent event) {
        Store.shutdown();
    }
}
