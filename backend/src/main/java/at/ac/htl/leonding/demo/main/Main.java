package at.ac.htl.leonding.demo.main;


import at.ac.htl.leonding.demo.features.store.LoremIpsum;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.event.Observes;

/** Quarkus has a strange understanding of what a main function is,
 * so we have to use Event listeners
 * for Startup and Shutdown */
@QuarkusMain
public class Main {
    public static void main(String ...args) {
        Quarkus.run(args);
    }
    void startup(@Observes StartupEvent event) {
        LoremIpsum.start();
    }
    void shutdown(@Observes ShutdownEvent event) {
        LoremIpsum.shutdown();
    }
}
