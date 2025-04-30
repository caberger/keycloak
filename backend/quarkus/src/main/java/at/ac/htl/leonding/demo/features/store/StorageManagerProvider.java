package at.ac.htl.leonding.demo.features.store;

import org.eclipse.serializer.reflect.ClassLoaderProvider;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageFoundation;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

public class StorageManagerProvider {
    EmbeddedStorageManager storageManager;

    @Produces
    @ApplicationScoped
    EmbeddedStorageManager instance() {
        if (storageManager == null) {
            var foundation = EmbeddedStorageFoundation.New();
            foundation.onConnectionFoundation(connectionFoundation ->
                    connectionFoundation.setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader())));
            storageManager = foundation.createEmbeddedStorageManager().start();
        }
        return storageManager;
    }
}
