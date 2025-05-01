package at.ac.htl.leonding.demo.features.store;

import java.lang.System.Logger;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.serializer.concurrency.XThreads;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import at.ac.htl.leonding.demo.features.post.LoremIpsum;
import at.ac.htl.leonding.demo.features.user.User;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@ApplicationScoped
public class Database {
    @Inject
    EmbeddedStorageManager storageManager;
    @Inject
    Logger log;

    @ConfigProperty(name="store.number-of-demo-records", defaultValue="0")
    Integer numberOfDemoRecordsToCreate;
    @ConfigProperty(name="store.rest-api", defaultValue = "false")
    Boolean startRestApi;

    public EmbeddedStorageManager storageManager() {
        return storageManager;
    }
    public DataRoot root() {
        return (DataRoot) storageManager.root();
    }
    public void update(Consumer<EmbeddedStorageManager> recipe) {
        XThreads.executeSynchronized(() -> {
            recipe.accept(storageManager());
        });
    }
    public List<User> users() {
        return root().users();
    }   
    void onInit(@Observes StartupEvent ev) {
        var root = (DataRoot)storageManager.root();
        if (root == null) {
            root = LoremIpsum.createDemoData(numberOfDemoRecordsToCreate);
            storageManager.setRoot(root);
            storageManager.storeRoot();
        }
        /*
        if (startRestApi) {
            var service = StorageRestServiceResolver.resolve(storageManager);
            service.start();
            log.log(Level.INFO, "you can see the storage api http://localhost:4567/");
        }
            */
     }
     void shutdown(@Observes ShutdownEvent event) {
        //storageManager().shutdown();
     }
}
