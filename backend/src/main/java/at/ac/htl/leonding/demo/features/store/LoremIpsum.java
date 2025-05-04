package at.ac.htl.leonding.demo.features.store;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.microprofile.config.ConfigProvider;

import com.github.javafaker.Faker;

import at.ac.htl.leonding.demo.features.post.Post;
import at.ac.htl.leonding.demo.features.user.User;
import at.ac.htl.leonding.demo.lib.Store;

interface DatabaseConfig {
    /**
     * the database parameters from our microprofile configuration
     * (e.g. application.properties)
     */
    record Configuration (
            Logger logger,
            String createUserId,
            String generation,
            int additionalUsersToCreate) {
    };

    static Configuration load() {
        var cfg = ConfigProvider.getConfig();
        var createUserIds = cfg.getOptionalValue("store.create.user-ids", String.class);
        var generation = cfg.getOptionalValue("store.generation", String.class);
        var additionalUsersToCreate = cfg.getOptionalValue("store.create.number-of-test-records", Integer.class);

        return new Configuration(
                System.getLogger(Configuration.class.getName()),
                createUserIds.isPresent() ? createUserIds.get() : "",
                generation.isPresent() ? generation.get() : "none",
                additionalUsersToCreate.isPresent() ? additionalUsersToCreate.get() : 0);
    }
}

/**
 * Initialize database and create some demo records if we are in debug mode.
 */
public interface LoremIpsum {
    static int NUMBER_OF_DUMMY_POSTS_PER_USER = 5;
    
    private static Database.Root createRoot(DatabaseConfig.Configuration config) {
        var users = new ArrayList<User>();
        if (!config.createUserId().isBlank()) {
            Stream.concat(
                    Arrays.stream(config.createUserId().split(",")).map(UUID::fromString),
                    IntStream.range(0, config.additionalUsersToCreate()).mapToObj(n -> UUID.randomUUID()))
                    .map(id -> createUser(id))
                    .forEach(users::add);

            config.logger().log(Level.INFO, "add default users {0} and {1} more users with {2} posts for each...", config.createUserId(),
                    config.additionalUsersToCreate(), NUMBER_OF_DUMMY_POSTS_PER_USER);
            var count = config.additionalUsersToCreate() + users.size();
            config.logger().log(Level.INFO, "done adding {0} users with a total of {1} posts.", count,
                    count * NUMBER_OF_DUMMY_POSTS_PER_USER);
        }
        return new Database.Root(users);
    }

    private static User createUser(UUID userId) {
        var faker = Faker.instance(new Random(System.currentTimeMillis()));
        var random = faker.random();
        var user = new User(userId);
        for (var i = 0; i < NUMBER_OF_DUMMY_POSTS_PER_USER; i++) {
            var offset = random.nextInt(21 * 86400);
            var date = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).minusSeconds(offset);
            user.posts().add(
                    new Post(faker.company().catchPhrase(), faker.chuckNorris().fact(), random.nextBoolean(), date));
        }
        return user;
    }

    static void start() {
        var config = DatabaseConfig.load();
        if (config.generation().equals("drop-and-create")) {
            config.logger().log(Level.INFO, "store.generation={0} found in configuration, dropping database", config.generation());
            Store.drop();
        }
        if (Database.root() == null) {
            config.logger().log(Level.INFO, "empty database found, create new");
            final var initialRoot = createRoot(config);
            Store.set(manager -> {
                manager.setRoot(initialRoot);
                manager.storeRoot();
            });
        }
    }
    static void shutdown() {
        Store.shutdown();
    }
}
