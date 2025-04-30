package at.ac.htl.leonding.demo.features.store;

import java.util.List;
import java.util.stream.LongStream;

import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.jboss.logging.Logger;

import com.github.javafaker.Faker;

import at.ac.htl.leonding.demo.features.post.Post;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class Database {
    @Inject
    EmbeddedStorageManager storageManager;
    @Inject
    Logger log;

    public DataRoot root() {
        return (DataRoot) storageManager.root();
    }    
    void onInit(@Observes StartupEvent ev) {
        var root = (DataRoot)storageManager.root();
        if (root == null) {
            root = new DataRoot(createDemoData());
            storageManager.setRoot(root);
            storageManager.storeRoot();
        }
     }
     /** create some demo data records.  
      * This should not be done in production, of course. */
     List<Post> createDemoData() { 
        log.info("Create demo data.");

        var faker = new Faker();
        var posts = LongStream
            .rangeClosed(1, 20)
            .mapToObj(n -> new Post(n, faker.harryPotter().quote(), faker.chuckNorris().fact(), faker.random().nextBoolean()))
            .toList();
        return posts;
    }
}
