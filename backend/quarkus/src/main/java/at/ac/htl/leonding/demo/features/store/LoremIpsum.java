package at.ac.htl.leonding.demo.features.store;


import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

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
    static int NUMBER_OF_DUMMY_POSTS_PER_USER = 5;

    @Inject Logger log;
    @ConfigProperty(name="store.create.user-ids", defaultValue="")
    String createUserId;
    @ConfigProperty(name="store.generation", defaultValue = "none")
    String generation;
    @ConfigProperty(name="store.create.number-of-test-records", defaultValue = "0")
    Integer additionalUsersToCreate;
    DataRoot createRoot() {
        return new DataRoot(demoData());
    }
    List<User> demoData() { 
        var users = new ArrayList<User>();
        if (!createUserId.isBlank()) {
            var defaultUserIds = createUserId.split(",");
            
            Arrays
                .stream(defaultUserIds)
                .map(id -> createUser(UUID.fromString(id)))
                .forEach(users::add);
            
            log.log(Level.INFO, "add default users {0} and {1} more users with {2} posts for each...", createUserId, additionalUsersToCreate, NUMBER_OF_DUMMY_POSTS_PER_USER);
            IntStream
                .range(0, additionalUsersToCreate)
                .mapToObj(n -> UUID.randomUUID())
                .map(this::createUser)
                .forEach(users::add);
            var count = additionalUsersToCreate + users.size();
            log.log(Level.INFO, "done adding {0} users with a total of {1} posts.", count, count * NUMBER_OF_DUMMY_POSTS_PER_USER);
        }
        return users;
    }
    User createUser(UUID userId) {
        var faker = new Faker();
        var random = faker.random();
        var user = new User(userId);
        for (var i = 0; i < NUMBER_OF_DUMMY_POSTS_PER_USER; i++) {
            user.posts().add(new Post(faker.hacker().verb(), faker.chuckNorris().fact(), random.nextBoolean()));
        }
        return user;
    }
    void onInit(@Observes StartupEvent ev) {
        if (generation.equals("drop-and-create")) {
            log.log(Level.INFO, "store.generation={0} found in configuration, dropping database", generation);
            Store.drop();
        }
        if (DataRoot.instance() == null) {
            log.log(Level.INFO, "empty database found, create new");
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
