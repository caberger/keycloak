package at.ac.htl.leonding.demo.features.store;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.serializer.collections.lazy.LazyHashMap;

import com.github.javafaker.Faker;

import at.ac.htl.leonding.demo.features.category.Category;
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
    
    private static Logger log() {
        return System.getLogger(LoremIpsum.class.getName());
    }
    private static Database.Root createRoot(DatabaseConfig.Configuration config) {
        Map<UUID, User> users = new LazyHashMap<>();
        Map<String, Category> categories = new LazyHashMap<>();

        if (!config.createUserId().isBlank()) {
            var randomCategories = createCategories();
            randomCategories.forEach(category -> categories.put(category.name(), category));
            Stream.concat(
                    Arrays.stream(config.createUserId().split(",")).map(UUID::fromString),
                    IntStream.range(0, config.additionalUsersToCreate()).mapToObj(n -> UUID.randomUUID()))
                    .map(id -> createUser(id, randomCategories))
                    .forEach(user -> users.put(user.id(), user));

            config.logger().log(Level.INFO, "add default users {0} and {1} more users with {2} posts for each...", config.createUserId(),
                    config.additionalUsersToCreate(), NUMBER_OF_DUMMY_POSTS_PER_USER);
            var count = users.size();
            config.logger().log(Level.INFO, "done adding {0} users with a total of {1} posts.", count,
                    count * NUMBER_OF_DUMMY_POSTS_PER_USER);
        }
        return new Database.Root(users, categories);
    }
    private static List<Category> createCategories() {
        var faker = Faker.instance(new Random(System.currentTimeMillis()));

        return IntStream
            .range(0, 5 * NUMBER_OF_DUMMY_POSTS_PER_USER)
            .mapToObj(n -> new Category(faker.book().genre(), faker.ancient().hero()))
            .toList();
 }
    private static User createUser(UUID userId, List<Category> randomCategories) {
        var faker = Faker.instance(new Random(System.currentTimeMillis()));
        var random = faker.random();
        var user = new User(userId);
        var generator = new Random();
        Supplier<Category> randomCategory = () -> {
            var index = generator.nextInt();
            index = (index < 0 ? -index : index) % randomCategories.size();
            return randomCategories.get(index);
        };

        for (var i = 0; i < NUMBER_OF_DUMMY_POSTS_PER_USER; i++) {
            var offset = random.nextInt(21 * 86400);
            offset = offset < 0 ? -offset : offset;
            var date = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).minusSeconds(offset);
            var post = new Post(user, faker.company().catchPhrase(), faker.chuckNorris().fact(), generator.nextInt() > 0, date, randomCategory.get());
            user.posts().add(post);
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
            final long now = System.currentTimeMillis();
            final var initialRoot = createRoot(config);
            Store.set(manager -> {
                manager.setRoot(initialRoot);
                manager.storeRoot();
            });
            long diff = System.currentTimeMillis() - now;
            var userSize = initialRoot.users().size();
            var recordCount = userSize * NUMBER_OF_DUMMY_POSTS_PER_USER + initialRoot.categories().size(); 
            log().log(Level.INFO, "Database inserts for {0} the users with a total of {1} posts took {2}s", userSize, recordCount, diff / 1000.0);
        }
    }
    static void shutdown() {
        Store.shutdown();
    }
}
