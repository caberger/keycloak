package at.ac.htl.leonding.demo.lib;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

import org.eclipse.serializer.concurrency.XThreads;
import org.eclipse.serializer.reflect.ClassLoaderProvider;
import org.eclipse.store.afs.nio.types.NioFileSystem;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageFoundation;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.eclipse.store.storage.types.Storage;
import org.eclipse.store.storage.types.StorageChannelCountProvider;
import org.eclipse.store.storage.types.StorageConfiguration;

class EmbeddedStore {
    static EmbeddedStorageManager storageManager;
    static final String BASE_FOLDER = "storage";
    static final int CHANNEL_COUNT = 4;
}
public interface Store {
    static String storageFolder() {
        return EmbeddedStore.BASE_FOLDER;
    }
    static EmbeddedStorageManager instance() {
        if (EmbeddedStore.storageManager == null) {
            var folder = NioFileSystem.New().ensureDirectoryPath(storageFolder());
            var foundation = EmbeddedStorageFoundation.New();
            foundation.setConfiguration(StorageConfiguration.Builder()
                .setStorageFileProvider(
                    Storage.FileProvider(folder)
                )
                .setChannelCountProvider(StorageChannelCountProvider.New(EmbeddedStore.CHANNEL_COUNT))
                .createConfiguration()
            );
            
            foundation.onConnectionFoundation(connectionFoundation -> {
                connectionFoundation
                    .setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader()))
                ;
            });

            EmbeddedStore.storageManager = foundation.createEmbeddedStorageManager().start();
        }
        return EmbeddedStore.storageManager;
    }
    static Object root() {
        return instance().root();
    }
    static void shutdown() {
        if (EmbeddedStore.storageManager != null) {
            EmbeddedStore.storageManager.shutdown();
            EmbeddedStore.storageManager = null;
        }
    }
    static void drop() {
        final var storage = Path.of(storageFolder());
        if (Files.exists(storage)) {
            try (var paths = Files.walk(storage)) {
                paths
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (Exception e) {
                            throw new CompletionException(e);
                        }
                    });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }   
        }
    }

    static void set(Consumer<EmbeddedStorageManager> recipe) {
        XThreads.executeSynchronized(() -> {
            recipe.accept(instance());
        });
    }
}
